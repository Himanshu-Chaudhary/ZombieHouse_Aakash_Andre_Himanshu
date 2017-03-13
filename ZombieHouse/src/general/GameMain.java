package general;

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
import java.util.ArrayList;
import java.util.Random;

public class GameMain extends Application
{
  private static Random random = new Random(0);
  public int exit_x, exit_y;

  // Map variables.
  public static int board_size = 50;
  public static PathNode[][] board = new PathNode[board_size][board_size];
  public static Box[][] board_boxes = new Box[board_size][board_size];
  public static Tile[][] map;

  public static Box someBox = new Box(10,10,10);

  public Group root = new Group();
  PhongMaterial black = new PhongMaterial(Color.BLACK);
  MyCamera my_camera;

  Player player;
  PointLight player_light = new PointLight();
  private boolean dead = false;

  ArrayList<Zombie> zombies = new ArrayList<>();
  private static ArrayList<Node[]> zombie_meshes = new ArrayList<>();

  ArrayList<MyParticle> particles = new ArrayList<>();


  public static void main(String[] args)
  {
    launch(args);
  }

  @Override public void start( Stage stage )
  {

    root.getChildren().add(someBox);

    // Add the player.
    player = new Player();
    root.getChildren().addAll( player.mesh );
    player.positionX = 30;
    player.positionY = 5;
    player.positionZ = 30;

    // Add the player's light.
    player_light.setTranslateY(25);
    root.getChildren().add(player_light);

    // Build the map & board for path finding.
    map = ProceduralMap.generateMap(board_size, board_size, 2);
    buildBoard();

    // Spawn the zombies.
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
          }
        }
      }
    }

    // Try to update every 60th of a second.
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Set up & show the scene.
    Scene scene = new Scene( root, 1024, 768, true );
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
    root.getChildren().removeAll(player.mesh);

    //Fade off from the player.
    fadeBoard();

    // Remove all zombie meshes from the root and from our list of meshes.
    for( Node[] mesh : zombie_meshes ){ root.getChildren().removeAll(mesh); }
    zombie_meshes.removeAll(zombie_meshes);

    // Add each zombie's mesh to the root.
    for( Zombie z : zombies ) {
      if( ! root.getChildren().contains(z.healthbar)) root.getChildren().add(z.healthbar);
      if(z.mesh != null)
      {
        root.getChildren().addAll(z.mesh);
        zombie_meshes.add(z.mesh);
      }
    }

    /* Manage the particle effects, removing any particles (or any other objects for
     that matter) that have fallen below the floor. */
    ArrayList<MyParticle> remove_list = new ArrayList<>();
    for( MyParticle mp : particles )
    {
      mp.update();
      if(!root.getChildren().contains(mp.mesh)){ root.getChildren().add(mp.mesh); }
      else if (mp.y_position<-10)
      {
        root.getChildren().remove(mp.mesh);
        remove_list.add(mp);
      }
    }
    particles.removeAll(remove_list);

    // Update the camera.
    my_camera.update();

    // Update the player.
    double time = System.currentTimeMillis();
    player.update(time);

    // Update the zombies.
    for(int i = 0; i < zombies.size(); i++)
    {
      zombies.get(i).update(time);

      // Spawn some particle effects when the zombies die.
      if(zombies.get(i).health <= 0)
      {
        root.getChildren().remove(zombies.get(i).healthbar);
        root.getChildren().removeAll(zombies.get(i).mesh);
        for(int p = 0; p < 200; p++){ particles.add( (new MyParticle(zombies.get(i).positionX,15,zombies.get(i).positionZ,
                random.nextFloat()-0.5,(random.nextFloat()-0.5)*2,random.nextFloat()-0.5) )); }
        zombies.remove(zombies.get(i));
      }
    }

    // On the player's death, spawn some particles.
    if(player.health<=0 && !dead)
    {
      dead = true;
      root.getChildren().removeAll(player.mesh);
      for(int i = 0; i < 200; i++){ particles.add( (new MyParticle(player.positionX,15,player.positionZ,
              random.nextFloat()-0.5,(random.nextFloat()-0.5)*2,random.nextFloat()-0.5) )); }
    }
    // If the player isn't dead, then add back the mesh.
    if(!dead){ root.getChildren().addAll(player.mesh); }

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
      }
    }
  }
  private void buildBoard() {

    PointLight exitLight = new PointLight();
    root.getChildren().add(exitLight);
    for(int x = 0; x < map.length; x++)
    {
      for(int y = 0; y < map[0].length; y++)
      {
        if(x != board_size -1 && y != board_size-1){ board[x][y] = new PathNode(x,y,0); }
        board_boxes[x][y] = new Box(10,10,10); // 9.5
        board_boxes[x][y].setTranslateX(x*10);
        board_boxes[x][y].setTranslateZ(y*10);
        board_boxes[x][y].setTranslateY(-10);
        root.getChildren().add(board_boxes[x][y]);
        switch (map[x][y].type){
          case wall: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.GRAY) );
            board_boxes[x][y].setHeight(75);
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
