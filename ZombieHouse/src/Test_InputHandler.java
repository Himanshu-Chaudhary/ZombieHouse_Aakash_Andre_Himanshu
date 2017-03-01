/*----------------------------------------------------------------
    Andre' Green

    Test_InputHandler merely acts as a simple testing suite for
    InputHandler, checking that key-presses and mouse movements
    are being picked up adequately well.
 ----------------------------------------------------------------*/

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Sphere;

public class Test_InputHandler extends Application
{
    private final Group root = new Group();
    private static Sphere test_sphere_1 = new Sphere(50);
    private static Sphere test_sphere_2 = new Sphere(50);

    /*----------------------------------------------------------------
    Stage stage : The stage to which the objects in the root are rendered.
    Sets up the scene and adds the two spheres, and updates @ 60 hz.
    ----------------------------------------------------------------*/
    @Override public void start(Stage stage)
    {
        root.getChildren().add(test_sphere_1);
        root.getChildren().add(test_sphere_2);
        Scene scene = new Scene( root, 1024, 768, true );

        // Handle the keyboard and mouse.
        InputHandler.setUpInputHandler( scene );

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // Show the window.
        stage.setScene( scene );
        stage.show();
    }

    /*----------------------------------------------------------------
    No input arguments.
    This method simply updates our scene, moving one sphere according
    to where the mouse cursor is on the screen, and the other according
    to the W A S D keys pressed by the player.
    ----------------------------------------------------------------*/
    private static void update()
    {
        test_sphere_2.setTranslateX( InputHandler.getMouseX() );
        test_sphere_2.setTranslateY( InputHandler.getMouseY() );
        if( InputHandler.isKeyDown(KeyCode.D)){ test_sphere_1.setTranslateX(test_sphere_1.getTranslateX()+10); }
        if( InputHandler.isKeyDown(KeyCode.A)){ test_sphere_1.setTranslateX(test_sphere_1.getTranslateX()-10); }
        if( InputHandler.isKeyDown(KeyCode.S)){ test_sphere_1.setTranslateY(test_sphere_1.getTranslateY()+10); }
        if( InputHandler.isKeyDown(KeyCode.W)){ test_sphere_1.setTranslateY(test_sphere_1.getTranslateY()-10); }
    }

    /*----------------------------------------------------------------
    String[] args : Does nothing at the moment, merely passed on to
    launch. This is the standard way of starting JavaFX applications.
    ----------------------------------------------------------------*/
    public static void main(String[] args){ launch(args ); }
}
