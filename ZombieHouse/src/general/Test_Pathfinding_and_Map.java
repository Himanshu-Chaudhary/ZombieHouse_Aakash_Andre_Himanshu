package general;

import Entities.Player;
import Entities.Zombie;
import Input.InputHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class Test_Pathfinding_and_Map extends Application
{
  // Camera junk
  final PerspectiveCamera camera = new PerspectiveCamera(true);
  final Xform cameraXform = new Xform();
  final Xform cameraXform2 = new Xform();
  final Xform cameraXform3 = new Xform();
  private static final double CAMERA_INITIAL_DISTANCE = -100;
  private static final double CAMERA_INITIAL_X_ANGLE = 30.0;
  private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
  private static final double CAMERA_NEAR_CLIP = 10;
  private static final double CAMERA_FAR_CLIP = 400.0;

  PhongMaterial white = new PhongMaterial(Color.WHITE);
  PhongMaterial lime = new PhongMaterial(Color.GREENYELLOW);

  Player player;
  Zombie zombie;
  Box current = new Box(3,3,3);
  Box target = new Box(3,3,3);

  public int zx = 5;
  public int zy = 5;

  public static PathNode[][] board = new PathNode[50][50];
  public static Box[][] board_boxes = new Box[50][50];
  private double my_drift_copy;
  PointLight player_light = new PointLight();


  public static Tile[][] map;

  private void buildCamera() {
    System.out.println("buildCamera()");
    root.getChildren().add(cameraXform);
    cameraXform.getChildren().add(cameraXform2);
    cameraXform2.getChildren().add(cameraXform3);
    cameraXform3.getChildren().add(camera);
    cameraXform3.setRotateZ(180.0);

    camera.setFieldOfView(50);
    camera.setNearClip(CAMERA_NEAR_CLIP);
    camera.setFarClip(CAMERA_FAR_CLIP);
    camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
    cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
  }

  public Group root = new Group();

  public static void main(String[] args)
  {
    launch(args);
  }

  private void buildBoard() {

    PhongMaterial stone = new PhongMaterial( Color.DARKSLATEGRAY );
    stone.setSpecularColor(Color.DARKGRAY);
    Image image = new Image(getClass().getResourceAsStream("tile.png"));
    Image image_s = new Image(getClass().getResourceAsStream("tile_bump.png"));
    Image image_n = new Image(getClass().getResourceAsStream("tile_normal.png"));
    stone.setSpecularMap(image_s);
    stone.setBumpMap(image_n);
    stone.setDiffuseMap(image);

    for(int x = 0; x < map.length; x++)
    {
      for(int y = 0; y < map[0].length; y++)
      {
        board[x][y] = new PathNode(x,y,0);
        board_boxes[x][y] = new Box(10,10,10); // 9.5
        board_boxes[x][y].setTranslateX(x*10);
        board_boxes[x][y].setTranslateZ(y*10);
        board_boxes[x][y].setTranslateY(-10);
        root.getChildren().add(board_boxes[x][y]);
        switch (map[x][y].type){
          case wall: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.BLACK) );
            board_boxes[x][y].setHeight(75);
            board[x][y] = null;
            break;
          }
          case region1: {
            board_boxes[x][y].setMaterial( stone );//was RED
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
            break;
          }
        }
      }
    }
  }

  private void update()
  {

    for(int x = 0; x < 50; x++)
    {
      for(int y = 0; y < 50; y++)
      {
        if(board_boxes[x][y] != null)
          board_boxes[x][y].setMaterial( white );
      }
    }

    current.setTranslateX(zombie.positionX);
    current.setTranslateZ(zombie.positionZ);

    // Draw the box at the center of our player model.
    board_boxes[(int)((player.positionX+5)/10)][(int)((player.positionZ+5)/10)].setMaterial( lime );

    zx =(int) (zombie.positionX+5)/10;
    zy =(int) (zombie.positionZ+5)/10;

    if(my_drift_copy != InputHandler.getDriftPrevention())
    {
      cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 0.1 * InputHandler.getMouseDX());
      my_drift_copy = InputHandler.getDriftPrevention();
      if( cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() > 0 && cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() < 90)
        cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY());
    }

    zombie.update();

    cameraXform.ry.setAngle(cameraXform.ry.getAngle()%360);
    player.direction = cameraXform.ry.getAngle();
    player.update();
    cameraXform.setTranslateX(player.positionX);
    cameraXform.setTranslateZ(player.positionZ);
    player_light.setTranslateX(player.positionX+5);
    player_light.setTranslateZ(player.positionZ+5);

    //Light up the zx block
    board_boxes[zx][zy].setMaterial( new PhongMaterial(Color.CYAN));
    // light up the path to the player.
    PathNode next = board[(int)((player.positionX+5)/10)][(int)((player.positionZ+5)/10)];
    Map<PathNode,PathNode> path = Pathfinding.getPath(board, next, board[zx][zy] );

    next = board[zx][zy];
    while(next != null && path != null)
    {
      board_boxes[next.x][next.y].setMaterial( lime );
      next = path.get(next);
    }

    if(path != null && path.get(board[zx][zy]) != null)
    {
      board_boxes[path.get(board[zx][zy]).x][path.get(board[zx][zy]).y].setMaterial(new PhongMaterial(Color.RED));
      target.setTranslateX( path.get(board[zx][zy]).x * 10 );
      target.setTranslateZ( path.get(board[zx][zy]).y * 10 );
      // set the bearing for the zombie: atan(dy,dx)
      double dy = zy-path.get(board[zx][zy]).y;
      double dx = zx-path.get(board[zx][zy]).x;
      zombie.direction = Math.toDegrees(Math.atan2( dy, dx));
      //System.out.println(zombie.direction);
    }

  }

  @Override
  public void start( Stage stage )
  {

    target.setMaterial(white);
    root.getChildren().add(target);
    current.setMaterial(white);
    root.getChildren().add(current);
    current.setTranslateY(15);
    target.setTranslateY(10);


    player_light.setTranslateY(15);
    root.getChildren().add(player_light);
    player = new Player();
    player.setPosition(55,0,55);
    root.getChildren().add( player.mesh );

    zombie = new Zombie();
    zombie.player = player;
    zombie.setPosition(50,3,50);
    root.getChildren().add(zombie.mesh);

    //Build the map:
    map = ProceduralMap.generateMap(50, 50, 1);

    buildCamera();
    buildBoard();
    root.setDepthTest(DepthTest.ENABLE);
    Scene scene = new Scene( root, 1024, 768, true );
    InputHandler.setUpInputHandler( scene );

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Show the window.
    scene.setFill(Color.BLACK);
    scene.setCursor(Cursor.NONE);
    scene.setCamera(camera);
    stage.setScene( scene );
    stage.show();
  }


}
