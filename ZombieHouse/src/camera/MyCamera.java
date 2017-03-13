package camera;

import general.GameMain;
import input.InputHandler;
import entities.Player;
import general.Xform;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;

public class MyCamera
{
  public final PerspectiveCamera camera = new PerspectiveCamera(true);
  public final Xform cameraXform = new Xform();
  final Xform cameraXform2 = new Xform();
  final Xform cameraXform3 = new Xform();
  private static final double CAMERA_INITIAL_DISTANCE = -25;
  private static final double CAMERA_INITIAL_X_ANGLE = 30.0;
  private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
  private static final double CAMERA_NEAR_CLIP = 10;
  private static final double CAMERA_FAR_CLIP = 400.0;
  private double my_drift_copy;

  Player player;

  public void update()
  {
    // Update the camera & player direction.
    if(my_drift_copy != InputHandler.getDriftPrevention())
    {
      cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 0.1 * InputHandler.getMouseDX());
      my_drift_copy = InputHandler.getDriftPrevention();
      if( cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() > 0 && cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() < 90)
        cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY());
    }
    cameraXform.ry.setAngle(cameraXform.ry.getAngle()%360);
    this.player.direction = cameraXform.ry.getAngle();

    // If the camera is in an invalid position, move it towards the player until it's in a valid position.
    if(InputHandler.isKeyDown(KeyCode.O)) camera.setTranslateZ(-30);

    // So this doesn't work, BUT if you also use direction of player,
    // then we can find if in wall or not. Repeat decreasing Z until we're not in a wall, or the camera's too close.
    double length = Math.cos(Math.toRadians(cameraXform.rx.getAngle()))*camera.getTranslateZ();
    // Then each iteration, back out as far as possible again. Inefficient but should work.
    int cx = (int) ((cameraXform.getTranslateX()+length*Math.sin(Math.toRadians(cameraXform.ry.getAngle())))/10);
    int cz = (int) ((cameraXform.getTranslateZ()+length*Math.cos(Math.toRadians(cameraXform.ry.getAngle())))/10);

    //GameMain.someBox.setTranslateX( ((cameraXform.getTranslateX()+length*Math.sin(Math.toRadians(cameraXform.ry.getAngle()))) ));
    //GameMain.someBox.setTranslateZ( ((cameraXform.getTranslateZ()+length*Math.cos(Math.toRadians(cameraXform.ry.getAngle()))) ));

    if(cx < 0 || cz < 0 || GameMain.board[cx][cz] == null)
    {
      camera.setTranslateZ(camera.getTranslateZ()+1);
    }
    else if (cx > 0 && cz > 0 && GameMain.board[cx][cz] != null && camera.getTranslateZ() > -30 && cameraXform.getTranslateY() < 30)
    {
      camera.setTranslateZ(camera.getTranslateZ()-1);
    }

    //else camera.setTranslateZ(-50);

  }



  public MyCamera(Player player)
  {
    this.player = player;
    cameraXform.getChildren().add(cameraXform2);
    cameraXform2.getChildren().add(cameraXform3);
    cameraXform3.getChildren().add(camera);
    cameraXform3.setRotateZ(180.0);
    cameraXform.setTranslateY(10);

    camera.setFieldOfView(50);
    camera.setNearClip(CAMERA_NEAR_CLIP);
    camera.setFarClip(CAMERA_FAR_CLIP);
    camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
    cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
  }

}
