import Entities.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Test_Entities extends Application
{
    public static Player player = new Player();
    public Group root = new Group();

    // Camera junk
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -50;
    private static final double CAMERA_INITIAL_X_ANGLE = 30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 400.0;

    private static Box test_box_1 = new Box(10,10,10);
    private static Box ref_box = new Box(10,10,10);

    public static WalkingBehavior playerWalk = new WalkingBehavior()
    {
        @Override
        public void walk()
        {
            double x_component = 0;
            double z_component = 0;

            //just back and forth for now
            if( InputHandler.isKeyDown(KeyCode.A))
            {
                x_component += Math.sin(Math.toRadians(player.direction+90));
                z_component += Math.cos(Math.toRadians(player.direction+90));
            }
            if( InputHandler.isKeyDown(KeyCode.D))
            {
                x_component += Math.sin(Math.toRadians(player.direction-90));
                z_component += Math.cos(Math.toRadians(player.direction-90));
            }
            if( InputHandler.isKeyDown(KeyCode.W))
            {
                x_component += Math.sin(Math.toRadians(player.direction));
                z_component += Math.cos(Math.toRadians(player.direction));
            }
            if( InputHandler.isKeyDown(KeyCode.S))
            {
                x_component -= Math.sin(Math.toRadians(player.direction));
                z_component -= Math.cos(Math.toRadians(player.direction));
            }
            double magnitude = Math.sqrt(x_component*x_component+z_component*z_component);
            if(magnitude != 0)
            {
                x_component = x_component/magnitude*player.speed;
                z_component = z_component/magnitude*player.speed;
                player.setPosition( player.positionX+x_component, player.positionY, player.positionZ+z_component );
            }
        }
    };
    private double my_drift_copy;

    public static void main (String[] args)
    {
        //So we'll show the player as a block in the scene.
        //Try to replicate what we had in demo, but more generally.
        // Get the zombies to just follow the player, too.
        launch(args);

    }

    @Override public void start( Stage stage )
    {

        ref_box.setTranslateY(-10);
        player.setWalkingBehavior( playerWalk );
        player.setSpeed(1);

        buildCamera();
        root.getChildren().add(ref_box);
        root.getChildren().add(test_box_1);
        Scene scene2 = new Scene( root, 1024, 768, true );
        InputHandler.setUpInputHandler( scene2 );

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Show the window.
        scene2.setFill(Color.BLACK);
        scene2.setCursor(Cursor.NONE);
        scene2.setCamera(camera);
        stage.setScene( scene2 );
        stage.show();
    }

    public void update()
    {
        test_box_1.setTranslateX(player.positionX);
        test_box_1.setTranslateZ(player.positionZ);
        test_box_1.setTranslateY(player.positionY);

        cameraXform.setTranslateX(player.positionX);
        cameraXform.setTranslateY(player.positionY);
        cameraXform.setTranslateZ(player.positionZ);

        if(my_drift_copy != InputHandler.getDriftPrevention())
        {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 0.1 * InputHandler.getMouseDX());
            my_drift_copy = InputHandler.getDriftPrevention();
            if( cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() > 0 && cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY() < 90)
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY());
        }

        cameraXform.ry.setAngle(cameraXform.ry.getAngle()%360);
        test_box_1.setRotationAxis(Rotate.Y_AXIS);
        test_box_1.setRotate(cameraXform.ry.getAngle());
        player.direction = cameraXform.ry.getAngle();

        player.performWalk();
    }

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

}
