package camera;

import input.InputHandler;
import entities.Player;
import general.Xform;
import javafx.scene.PerspectiveCamera;

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
