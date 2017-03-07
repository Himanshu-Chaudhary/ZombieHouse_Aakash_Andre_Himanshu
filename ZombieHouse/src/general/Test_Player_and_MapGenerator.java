package general;

import Entities.Player;
import Input.InputHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Test_Player_and_MapGenerator extends Application
{
  // Camera junk
  final PerspectiveCamera camera = new PerspectiveCamera(true);
  final Xform cameraXform = new Xform();
  final Xform cameraXform2 = new Xform();
  final Xform cameraXform3 = new Xform();
  private static final double CAMERA_INITIAL_DISTANCE = -200;
  private static final double CAMERA_INITIAL_X_ANGLE = 30.0;
  private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
  private static final double CAMERA_NEAR_CLIP = 0.1;
  private static final double CAMERA_FAR_CLIP = 400.0;

  Player player;

  public static PathNode[][] board = new PathNode[50][50];
  public static Box[][] board_boxes = new Box[50][50];
  private double my_drift_copy;


  public Tile[][] map;

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
    for(int x = 0; x < map.length; x++)
    {
      for(int y = 0; y < map[0].length; y++)
      {
        board[x][y] = new PathNode(x,y,0);
        board_boxes[x][y] = new Box(9.5,9.5,9.5);
        board_boxes[x][y].setTranslateX(x*10);
        board_boxes[x][y].setTranslateZ(y*10);
        board_boxes[x][y].setTranslateY(-10);
        root.getChildren().add(board_boxes[x][y]);
        switch (map[x][y].type){
          case wall: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.BLACK));
            board_boxes[x][y].setHeight(50);
            board[x][y] = null;
            break;
          }
          case region1: {
            board_boxes[x][y].setMaterial( new PhongMaterial(Color.RED));
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
            break;
          }
        }
      }
    }
  }

  private void update()
  {
    if(my_drift_copy != InputHandler.getDriftPrevention())
    {
      cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 0.1 * InputHandler.getMouseDX());
      my_drift_copy = InputHandler.getDriftPrevention();
      if( cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() > 0 && cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() < 90)
        cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY());
    }

    cameraXform.ry.setAngle(cameraXform.ry.getAngle()%360);
    player.direction = cameraXform.ry.getAngle();
    player.update();
    cameraXform.setTranslateX(player.positionX);
    cameraXform.setTranslateZ(player.positionZ);
  }

  @Override
  public void start( Stage stage )
  {
    player = new Player();
    player.setPosition(55,0,55);
    root.getChildren().add( player.mesh );

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
