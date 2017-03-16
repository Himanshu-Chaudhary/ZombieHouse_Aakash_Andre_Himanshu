package general;

import entities.Entity;
import entities.PastSelf;
import entities.Zombie;
import entities.Player;
import camera.MyCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import map_generation.BoardManager;
import map_generation.Tile;
import map_generation.ProceduralMap;
import input.InputHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import pathfinding.PathNode;
import java.util.ArrayList;

public class GameMain extends Application
{
  private static PointLight light = new PointLight();
  public static Group game_root = new Group();
  public static int board_size = 50;
  public static Tile[][] map;
  public static PathNode[][] path_nodes = new PathNode[board_size][board_size];
  public static Box[][][] board_boxes = new Box[2][board_size][board_size];

  public static ArrayList<Entity> players = new ArrayList<>(); // Past & present.
  public static ArrayList<Zombie> zombies = new ArrayList<>();

  public static int exit_x;
  public static int exit_z;

  private static Timeline menu_timeline;
  private static Group menu_root;

  Scene menu;

  public static Player player;
  MyCamera my_camera;

  @Override public void start( Stage stage )
  {
    my_camera = new MyCamera();

    menu_root = new Group();
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

    menu_timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> {
      // Flicker the "don't" in "don't press space".
      if(Math.random() > 0.9){ do_not.setFill(Color.WHITE); }
      else { do_not.setFill(Color.BLACK); }
      if (InputHandler.isKeyDown(KeyCode.SPACE)) { startGame(stage); }
    }));
    menu_timeline.setCycleCount(Animation.INDEFINITE);
    menu_timeline.play();

    menu.setFill( Color.BLACK );
    menu.setCursor(Cursor.NONE);
    stage.setScene( menu );
    stage.show();
  }


  public void startGame( Stage stage )
  {
    menu_timeline.stop();
    game_root.getChildren().add(light);

    // Update our game (or try to) every 1/60th of a second.
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update( stage, false )));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Configure the map for our game, including appearance and path-finding.
    map = ProceduralMap.generateMap( board_size, board_size, 1 );
    BoardManager.removeMapMeshes( game_root );
    BoardManager.addMapMeshes( board_boxes, map, game_root );
    BoardManager.configurePathNodes( map, path_nodes );

    player = new Player(20,40,20);
    players.add( player );
    spawnZombies();

    my_camera = new MyCamera();
    Scene scene = new Scene( game_root, 1024, 768, true );
    InputHandler.setUpInputHandler( scene );
    scene.setFill(Color.BLACK);
    scene.setCursor(Cursor.NONE);
    scene.setCamera(my_camera.camera);
    stage.setScene( scene );
    stage.show();
  }

  private void spawnZombies()
  {
    for(int x = 0; x < board_size-1; x++)
    {
      for(int y = 0; y < board_size-1; y++)
      {
        if(map[x][y].isWall || map[x][y].isHallway || map[x][y].isBorder || map[x][y].isObstacle){}
        else if (Math.random() > 0.95){ zombies.add( new Zombie(x*10,40,y*10)); }
      }
    }
  }

  // Fade out the brightness of the board, depending on proximity to player and past players.
  private void fadeBoard()
  {
    double d; // Distance to player.
    double dist;
    double min_dist;
    for(int x = 0; x < board_size; x++)
    {
      for(int y = 0; y < board_size; y++)
      {
        min_dist = 999;
        if(board_boxes[0][x][y] != null && board_boxes[0][x][y].getMaterial() != null )
        {
          for( Entity p : players )
          {
            dist = Math.sqrt(Math.pow(p.position_x-x*10,2)+Math.pow(p.position_z-y*10,2));
            if(dist < min_dist) min_dist = dist;
          }

          d = min_dist;
          d = d/100;
          if ( d > 1) d = 1;
          d = 1-d;
          ((PhongMaterial) board_boxes[0][x][y].getMaterial()).setDiffuseColor( Color.color(d,d/1.2,d/1.2) );
          ((PhongMaterial) board_boxes[0][x][y].getMaterial()).setSpecularColor( Color.color(d,d,d/1.2) );
          if( board_boxes[1][x][y] != null && board_boxes[1][x][y].getMaterial() != null)
          {
            ((PhongMaterial) board_boxes[1][x][y].getMaterial()).setDiffuseColor(Color.color(d/6, d/6, d/6));
          }
        }
      }
    }
  }

  private void update( Stage stage, boolean override )
  {
    if(stage.getScene().equals( menu ) && !override ) return;

    if( player.health <= 0){ playerDied(); }
    for( Zombie z : zombies)
    {
      if (z.health <= 0)
      {
        game_root.getChildren().remove( z.meshview );
        game_root.getChildren().remove( z.healthbar );
      }
    }
    if( Math.sqrt(Math.pow(player.position_x-exit_x,2)+Math.pow(player.position_z-exit_z,2)) < 15){ exitReached(); }

    fadeBoard();

    // Simulate death and/or reaching the exit.
    if(InputHandler.isKeyDown(KeyCode.O)) { playerDied(); }
    if( InputHandler.isKeyDown(KeyCode.P)) { exitReached(); }

    if(InputHandler.isKeyDown(KeyCode.U))
    {
      menu = new Scene(new Group(), 1024,768,true);
      InputHandler.setUpInputHandler( menu );
      stage.setScene( menu );
    }

    my_camera.update(); // Update *before* player to avoid latency issues.
    player.direction = my_camera.cameraXform.ry.getAngle()+180;

    // Update players, past players, and zombies.
    for( Entity p : players ){ p.update( System.currentTimeMillis() ); }
    for( Zombie z: zombies ){ z.update( System.currentTimeMillis() ); }

    // Update *after* player to avoid stuttering latency issues.
    my_camera.cameraXform.setTranslateX( player.position_x );
    my_camera.cameraXform.setTranslateZ( player.position_z );

    light.setTranslateX( player.position_x - 2* Math.sin( Math.toRadians( player.direction )));
    light.setTranslateZ( player.position_z - 2* Math.cos( Math.toRadians( player.direction )));
    light.setColor( Color.color(0.5,0.5,0.5) );
  }

  private void playerDied()
  {
    ArrayList<Integer[]> start_positions = new ArrayList<>();

    // Remove all zombies.
    for (Zombie z : zombies){
      start_positions.add( z.position_start );
      game_root.getChildren().remove(z.meshview);
      game_root.getChildren().remove(z.healthbar);
    }
    zombies.removeAll(zombies);

    // Add the zombies back in.
    for( Integer[] sp : start_positions ){ zombies.add( new Zombie(sp[0], sp[1], sp[2] )); }

    for( Entity p : players ){ game_root.getChildren().remove( p.meshview ); }
    players.remove( player );

    // Reset all past players.
    for( Entity p : players ) { if( p instanceof PastSelf ) { ((PastSelf) p).currentStep = 0; }}

    // Add the new past player.
    players.add( new PastSelf( player.past_position_x,
                              player.past_position_z,
                              player.past_states,
                              player.past_direction) );

    player = new Player(20,40,20);
    players.add( player );
    my_camera.cameraXform.ry.setAngle(45);
  }
  private void exitReached()
  {
    //game_root.getChildren().removeAll(game_root.getChildren());
    // Remove all zombies.
    for (Zombie z : zombies)
    {
      game_root.getChildren().remove(z.meshview);
      game_root.getChildren().remove(z.healthbar);
    }
    zombies.removeAll(zombies);

    // Remove all player meshes and player lives.
    // The past lives don't persist beyond a single level.
    for( Entity p : players ){ game_root.getChildren().remove( p.meshview ); }
    players.removeAll(players);

    // Create a new map.
    map = ProceduralMap.generateMap( board_size, board_size, 1 );
    BoardManager.removeMapMeshes( game_root );
    BoardManager.addMapMeshes( board_boxes, map, game_root );
    BoardManager.configurePathNodes( map, path_nodes );

    // Create a fresh player.
    player = new Player(20,40,20);
    players.add( player );
    my_camera.cameraXform.ry.setAngle(45);

    // Add the new zombies.
    spawnZombies();
  }

}
