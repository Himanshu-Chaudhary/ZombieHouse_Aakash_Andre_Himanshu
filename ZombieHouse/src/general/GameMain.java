package general;

import entities.PastSelf;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import map_generation.Tile;
import map_generation.ProceduralMap;
import camera.MyCamera;
import entities.Player;
import entities.Zombie;
import pathfinding.PathNode;
import input.InputHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.security.krb5.internal.PAData;

import java.util.ArrayList;
import java.util.Random;

public class GameMain extends Application
{

  private boolean lock = true;
  private static Random random = new Random(0);
  public int exit_x, exit_y;

  // Map variables.
  public static int board_size = 50;
  public static PathNode[][] board = new PathNode[board_size][board_size];
  public static Box[][] board_boxes = new Box[board_size][board_size];
  public static Tile[][] map;

  private Group game_root = new Group();
  private Group menu_root = new Group();
  PhongMaterial black = new PhongMaterial(Color.BLACK);
  public static MyCamera my_camera;

  Player player;
  ArrayList<PastSelf> pastSelves = new ArrayList<>();
  private double lives = 5;
  PointLight player_light = new PointLight();
  private boolean dead = false;

  ArrayList<Zombie> zombies = new ArrayList<>();
  private static ArrayList<Node[]> zombie_meshes = new ArrayList<>();
  public ArrayList<Integer[]> zombie_spawn_locations = new ArrayList<>();

  ArrayList<MyParticle> particles = new ArrayList<>();

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override public void start( Stage stage )
  {
    createMainMenu(stage);
  }

  private void createMainMenu(Stage stage)
  {
    //Load stuff for the game before starting up the menu.
    player = new Player();
    game_root.getChildren().addAll( player.mesh );
    player.positionX = 30;
    player.positionY = 5;
    player.positionZ = 30;

    player_light.setTranslateY(25);
    game_root.getChildren().add(player_light);
    buildBoard();
    spawnZombies();
    fadeBoard();

    Scene menu = new Scene( menu_root, 1024, 768, true );
    InputHandler.setUpInputHandler( menu );
    Text begin = new Text("press space");
    Text do_not = new Text("don't");
    begin.setFont(Font.font("Eras Light ITC", 30));

    begin.setTranslateX(400);
    begin.setTranslateY(384);
    begin.setFill( Color.WHITE );
    menu_root.getChildren().add(do_not);
    do_not.setTranslateX(325);
    do_not.setTranslateY(384);
    do_not.setFill( Color.BLACK );
    do_not.setFont(Font.font("Eras Light ITC", 30));
    menu_root.getChildren().add(begin);

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> {
      // Flicker the "don't" in "don't press space".
      if(random.nextFloat() > 0.9){ do_not.setFill(Color.WHITE); }
      else { do_not.setFill(Color.BLACK); }
      if (InputHandler.isKeyDown(KeyCode.SPACE) && lock)
      {
        lock = false;
        createGameScene(stage);
      }
      }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    menu.setFill( Color.BLACK );
    menu.setCursor(Cursor.NONE);
    stage.setScene( menu );
    stage.show();
  }

  private void createGameScene( Stage stage )
  {
    // Try to update every 60th of a second.
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Set up & show the scene.
    Scene scene = new Scene( game_root, 1024, 768, true );
    InputHandler.setUpInputHandler( scene );
    my_camera = new MyCamera( player );
    scene.setFill(Color.BLACK);
    scene.setCursor(Cursor.NONE);
    scene.setCamera(my_camera.camera);
    stage.setScene( scene );
    stage.show();
  }

  private void update()
  {
    //Remove player at the start, add 'em at the end.
    game_root.getChildren().removeAll(player.mesh);
    for (PastSelf p : pastSelves){
      game_root.getChildren().removeAll(p.mesh);
    }

    //Fade off from the player.
    fadeBoard();

    // Remove all zombie meshes from the root and from our list of meshes.
    for( Node[] mesh : zombie_meshes ){ game_root.getChildren().removeAll(mesh); }
    zombie_meshes.removeAll(zombie_meshes);

    // Add each zombie's mesh to the root.
    for( Zombie z : zombies ) {
      if( ! game_root.getChildren().contains(z.healthbar)) game_root.getChildren().add(z.healthbar);
      if(z.mesh != null)
      {
        game_root.getChildren().addAll(z.mesh);
        zombie_meshes.add(z.mesh);
      }
    }

    /* Manage the particle effects, removing any particles (or any other objects for
     that matter) that have fallen below the floor. */
    ArrayList<MyParticle> remove_list = new ArrayList<>();
    for( MyParticle mp : particles )
    {
      mp.update();
      if(!game_root.getChildren().contains(mp.mesh)){ game_root.getChildren().add(mp.mesh); }
      else if (mp.y_position<-10)
      {
        game_root.getChildren().remove(mp.mesh);
        remove_list.add(mp);
      }
    }
    particles.removeAll(remove_list);

    // Update the camera.
    my_camera.update();

    // Update the player.
    double time = System.currentTimeMillis();
    player.update(time);
    for (PastSelf p : pastSelves){
      p.update(time);
    }

    if(InputHandler.isKeyDown(KeyCode.SHIFT))
      particles.add( (new MyParticle(player.positionX+(Math.random()-0.5)*3,7+(Math.random()-0.5)*10,player.positionZ+(Math.random()-0.5)*3,
            random.nextFloat()-0.5,(random.nextFloat()-0.5)*2,random.nextFloat()-0.5) ));

    // Update the zombies.
    for(int i = 0; i < zombies.size(); i++)
    {
      zombies.get(i).update(time);

      // Spawn some particle effects when the zombies die.
      if(zombies.get(i).health <= 0)
      {
        game_root.getChildren().remove(zombies.get(i).healthbar);
        game_root.getChildren().removeAll(zombies.get(i).mesh);
        for(int p = 0; p < 200; p++){ particles.add( (new MyParticle(zombies.get(i).positionX,15,zombies.get(i).positionZ,
                random.nextFloat()-0.5,(random.nextFloat()-0.5)*2,random.nextFloat()-0.5) )); }
        zombies.remove(zombies.get(i));
      }
    }

    // On the player's death, spawn some particles.
    if(player.health<=0 && !dead)
    {
      dead = true;
      PastSelf temp = (new PastSelf(player.getWalkBehaviorsX(),player.getWalkBehaviorsZ(),player.getStateBehaviors(),player.getDirectionBehaviour()));

      pastSelves.add(temp);
      game_root.getChildren().removeAll(player.mesh);
      for (PastSelf p : pastSelves){
        game_root.getChildren().removeAll(p.mesh);
      }

//      for(int i = 0; i < 200; i++){ particles.add( (new MyParticle(player.positionX,15,player.positionZ,
//              random.nextFloat()-0.5,(random.nextFloat()-0.5)*2,random.nextFloat()-0.5) )); }

      // Reset the player.
      lives -= 1;
      System.out.println(lives);
      if(lives > 0)
      {
        System.out.println(lives);
        player.health = 30;
        player.proposeState("IDLE", 0, 10);
        player.positionX = 30;
        player.positionY = 5;
        player.positionZ = 30;
        player.reset();
        // Remove all current zombies and zombie meshes, then add them all back.
        for (Zombie z : zombies) { game_root.getChildren().remove(z.healthbar); }
        for (Node[] mesh : zombie_meshes) { game_root.getChildren().removeAll(mesh); }
        zombies.removeAll(zombies);
        for (Integer[] l : zombie_spawn_locations) { zombies.add(new Zombie(l[0], l[1], l[2], player, board)); }
        dead = false;
      }
    }
    // If the player isn't dead, then add back the mesh.
    if(!dead){ game_root.getChildren().addAll(player.mesh);
      for (PastSelf p : pastSelves){
        game_root.getChildren().addAll(p.mesh);
      }}


    // If the exit is reached, restart.
    if( Math.sqrt( Math.pow(exit_x*10-player.positionX,2) + Math.pow(exit_y*10-player.positionZ,2)) < 20)
    {
      lives = 5;
      player.health = 30;
      player.proposeState("IDLE",0,10);
      player.positionX = 30;
      player.positionY = 5;
      player.positionZ = 30;

      for( Zombie z : zombies ){ game_root.getChildren().remove(z.healthbar); }
      for( Node[] mesh : zombie_meshes ){ game_root.getChildren().removeAll(mesh); }
      zombies.removeAll(zombies);
      buildBoard();
      spawnZombies();
    }

    // Update the camera and light based on the player's new information.
    // [ This avoids stuttering ]
    my_camera.cameraXform.setTranslateX(player.positionX);
    my_camera.cameraXform.setTranslateZ(player.positionZ);

    // Update the player's light's position. I update this
    // last because if we update before the player it'll lag behind.
    player_light.setTranslateX(player.positionX);
    player_light.setTranslateZ(player.positionZ);

  }
  private void fadeBoard()
  {
    double d2e; // To exit
    double d2p; // To player
    double d;
    for(int x = 0; x < board_size-1; x++)
    {
      for(int y = 0; y < board_size-1; y++)
      {
        d2e = Math.sqrt(Math.pow(board_boxes[x][y].getTranslateX()-exit_x*10,2)+Math.pow(board_boxes[x][y].getTranslateZ()-exit_y*10,2));
        d2p = Math.sqrt(Math.pow(board_boxes[x][y].getTranslateX()-player.positionX,2)+Math.pow(board_boxes[x][y].getTranslateZ()-player.positionZ,2));

        d = Math.min(d2e,d2p);
        d /= 100;
        if (d > 1) d = 1;
        d = 1-d;
        if(d == 0) board_boxes[x][y].setMaterial(black);
        else if (map[x][y].getRegion() == 1) board_boxes[x][y].setMaterial( new PhongMaterial(Color.color(d,0,0)));
        else if (map[x][y].getRegion() == 2) board_boxes[x][y].setMaterial( new PhongMaterial(Color.color(0,d,0)));
        else if (map[x][y].getRegion() == 3) board_boxes[x][y].setMaterial( new PhongMaterial(Color.color(0,0,d)));
        else board_boxes[x][y].setMaterial( new PhongMaterial(Color.color(d,d,d)));
//          ((PhongMaterial) board_boxes[x][y].getMaterial()).setDiffuseMap(image);
//          ((PhongMaterial) board_boxes[x][y].getMaterial()).setSpecularMap(specular);
//          ((PhongMaterial) board_boxes[x][y].getMaterial()).setBumpMap(normal);

      }
    }
  }
  private void spawnZombies()
  {
    // Spawn the zombies.
    zombie_spawn_locations.removeAll(zombie_spawn_locations);
    for(int x = 1; x < board_size-2; x++)
    {
      for(int y = 1; y < board_size-2; y++)
      {
        if(!map[x][y].isObstacle && !map[x][y].isHallway && !map[x][y].isBorder && !map[x][y].isWall)
        {
          if(random.nextFloat() > 0.98)
          {
            zombies.add( new Zombie(x*10,5,y*10,player,board));
            zombies.get(zombies.size()-1).randomWalk = random.nextFloat() > 0.5;
            zombie_spawn_locations.add(new Integer[] { x*10,5,y*10 } );
          }
        }
      }
    }
  }
  private void buildBoard() {


    // First wipe out any and all boxes from the last iteration.
    ArrayList<Node> remove_list = new ArrayList<>();
    for( Node child : game_root.getChildren() )
    {
      if(child instanceof Box)
      {
        remove_list.add( child );
      }
    }
    game_root.getChildren().removeAll(remove_list);

    // Generate a new map.
    map = ProceduralMap.generateMap(board_size, board_size, 2);

    // Add the light for the exit.
    PointLight exitLight = new PointLight();
    game_root.getChildren().add(exitLight);

    // Construct the boxes necessary for the new map.
    for(int x = 0; x < map.length; x++)
    {
      for(int y = 0; y < map[0].length; y++)
      {
        board_boxes[x][y] = null;
        board[x][y] = null;
        if(x != board_size -1 && y != board_size-1){ board[x][y] = new PathNode(x,y,0); }
        board_boxes[x][y] = new Box(10,10,10); // 9.5
        board_boxes[x][y].setTranslateX(x*10);
        board_boxes[x][y].setTranslateZ(y*10);
        board_boxes[x][y].setTranslateY(-10);
        game_root.getChildren().add(board_boxes[x][y]);
        board_boxes[x][y].setHeight(10);
        switch (map[x][y].type){
          case wall: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.GRAY) );
            board_boxes[x][y].setHeight(15);
            board[x][y] = null;
            break;
          }
          case region1: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.RED) );//was RED
            break;
          }
          case region2: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.YELLOW));
            break;
          }
          case region3: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.BLUE));
            break;
          }
          case region4: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.GREEN));
            break;
          }
          case exit: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.WHITE));
            board[x][y] = null;
            exitLight.setTranslateX(x*10);
            exitLight.setTranslateZ(y*10);
            exitLight.setTranslateY(10);
            exit_x = x;
            exit_y = y;
            break;
          }
        }
      }
    }
  }

}
