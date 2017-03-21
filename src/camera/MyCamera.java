package camera;

import general.GameMain;
import input.InputHandler;
import general.Xform;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;

/**
 * @author Andre Green
 *
 * uses Xform file from the following Oracle's website to perform transformations
 * link : http://docs.oracle.com/javafx/8/3d_graphics/Xform.java.html
 *
 * Sets up the third person view for the camera
 */
public class MyCamera
{
  public final PerspectiveCamera camera = new PerspectiveCamera(true);
  public final Xform cameraXform = new Xform();
  final Xform cameraXform2 = new Xform();
  final Xform cameraXform3 = new Xform();
  private static final double CAMERA_INITIAL_DISTANCE = -10;
  private static final double CAMERA_INITIAL_X_ANGLE = 80;
  private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
  private static final double CAMERA_NEAR_CLIP = 2;
  private static final double CAMERA_FAR_CLIP = 400;
  private double my_drift_copy;

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

    if (GameMain.player.state.equals("RUN"))
    {
      camera.setFieldOfView((125+8*camera.getFieldOfView())/9.0);
      cameraXform.rz.setAngle((8*cameraXform.rz.getAngle()+35)/9.0);

    }
    else
    {
      camera.setFieldOfView((105+20*camera.getFieldOfView())/21.0);
      cameraXform.rz.setAngle(cameraXform.rz.getAngle()/1.05);

    }

  }

  public MyCamera()
  {
    cameraXform.getChildren().add(cameraXform2);
    cameraXform2.getChildren().add(cameraXform3);
    cameraXform3.getChildren().add(camera);
    cameraXform3.setRotateZ(180.0);
    cameraXform.setTranslateY(13);

    camera.setFieldOfView(100);
    camera.setNearClip(CAMERA_NEAR_CLIP);
    camera.setFarClip(CAMERA_FAR_CLIP);
    camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
    cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
  }

}
