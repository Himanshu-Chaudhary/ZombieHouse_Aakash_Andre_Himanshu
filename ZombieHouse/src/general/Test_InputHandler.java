package general;/*----------------------------------------------------------------
    Andre' Green

    Test_InputHandler merely acts as a simple testing suite for
    InputHandler, checking that key-presses and mouse movements
    are being picked up adequately well.
 ----------------------------------------------------------------*/

import Input.InputHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

//they just made the floor a bunch of boxes, not planes.
//they do have walls as planes though, so that's alright I guess.

public class Test_InputHandler extends Application
{

    //axess
    final Xform axisGroup = new Xform();
    final Xform floorGroup = new Xform();
    double i = 0;

    PointLight light;
    PointLight light2;

    // Camera junk
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -100;
    private static final double CAMERA_INITIAL_X_ANGLE = 30.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 400.0;

    private double my_drift_copy = 0;

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

    private final Group root = new Group();
    private static Box test_box_1 = new Box(10,10,10);

    private void buildFloor() {

        final PhongMaterial grayMaterial = new PhongMaterial();
        grayMaterial.setDiffuseColor(Color.DARKGRAY);
        grayMaterial.setSpecularColor(Color.DARKGRAY);

        for(int i = 0; i < 40; i++)
        {
            for(int j = 0; j < 40; j++)
            {
                floorGroup.getChildren().add(new Box(10,10,10));
                floorGroup.getChildren().get(i*40+j).setTranslateX((i-5)*11);
                floorGroup.getChildren().get(i*40+j).setTranslateZ((j-5)*11);
                floorGroup.getChildren().get(i*40+j).setTranslateY(-10);
                ((Box) floorGroup.getChildren().get(i*40+j)).setMaterial(grayMaterial);
                //((Box) floorGroup.getChildren().get(i*40+j)).setEffect(reflection);

            }
        }
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        test_box_1.setMaterial(redMaterial);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(50, 1, 1);
        final Box yAxis = new Box(1, 50, 1);
        final Box zAxis = new Box(1, 1, 50);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        root.getChildren().addAll(axisGroup);
    }

    /*----------------------------------------------------------------
    Stage stage : The stage to which the objects in the root are rendered.
    Sets up the scene and adds the two spheres, and updates @ 60 hz.
    ----------------------------------------------------------------*/
    @Override public void start(Stage stage)
    {
        light = new PointLight();
        light2 = new PointLight();
        //root.getChildren().add(light);
        root.getChildren().add(light2);

        buildFloor();
        root.getChildren().add(floorGroup);

        root.setDepthTest(DepthTest.ENABLE);
        buildAxes();
        root.getChildren().add(test_box_1);
        buildCamera();
        Scene scene = new Scene( root, 1024, 768, true );

        // Handle the keyboard and mouse.
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

    /*----------------------------------------------------------------
    No input arguments.
    This method simply updates our scene, moving one sphere according
    to where the mouse cursor is on the screen, and the other according
    to the W A S D keys pressed by the player.
    ----------------------------------------------------------------*/
    private void update()
    {
        double dist;
        for(int i = 0; i < 40; i++)
        {
            for(int j = 0; j < 40; j++)
            {
                dist = Math.sqrt(Math.pow(floorGroup.getChildren().get(i*40+j).getTranslateX()-test_box_1.getTranslateX(),2)+
                                Math.pow(floorGroup.getChildren().get(i*40+j).getTranslateZ()-test_box_1.getTranslateZ(),2));
                //floorGroup.getChildren().get(i*40+j).setTranslateY( dist/10-15 ); //dist - 15 );
                if(j%5==0 && i%5 == 0) floorGroup.getChildren().get(i*40+j).setTranslateY( dist/5 -15 ); //dist - 15 );

                /*if(dist > 100)
                    ((Box) floorGroup.getChildren().get(i*40+j)).setMaterial( new PhongMaterial(Color.BLACK));
                else
                    ((Box) floorGroup.getChildren().get(i*40+j)).setMaterial( new PhongMaterial(Color.WHITE));
                    */
                // so this is dumb and slow, because it makes a new material for every block every frame,
                // but this idea would work to fade stuff out. Ideally don't even try to render blocks out of range
                //((Box)floorGroup.getChildren().get(i*40+j)).setMaterial( new PhongMaterial(Color.color(dist,dist,dist)){{this.setSpecularColor(Color.GRAY);}});
                if (floorGroup.getChildren().get(i*40+j).isVisible())
                {
                    ((Box) floorGroup.getChildren().get(i*40+j)).setMaterial( new PhongMaterial(Color.BLUE));
                }
                else
                {
                    ((Box) floorGroup.getChildren().get(i*40+j)).setMaterial( new PhongMaterial(Color.RED));
                    System.out.println("hey");
                }
            }
        }

        light.setTranslateX(test_box_1.getTranslateX());
        light.setTranslateZ(test_box_1.getTranslateZ());
        light.setTranslateY(-20);
        light.setColor(Color.color(0,1,0));

        if(my_drift_copy != InputHandler.getDriftPrevention())
        {
            cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 0.1 * InputHandler.getMouseDX());
            my_drift_copy = InputHandler.getDriftPrevention();
            cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 0.1 *InputHandler.getMouseDY());
        }

        cameraXform.ry.setAngle(cameraXform.ry.getAngle()%360);
        test_box_1.setRotationAxis(Rotate.Y_AXIS);
        test_box_1.setRotate(cameraXform.ry.getAngle());

        double x_component = 0;
        double y_component = 0;
        if( InputHandler.isKeyDown(KeyCode.W))
        {
            x_component += Math.cos(Math.toRadians(cameraXform.ry.getAngle()));
            y_component += Math.sin(Math.toRadians(cameraXform.ry.getAngle()));
        }
        if( InputHandler.isKeyDown(KeyCode.S))
        {
            x_component -= Math.cos(Math.toRadians(cameraXform.ry.getAngle()));
            y_component -= Math.sin(Math.toRadians(cameraXform.ry.getAngle()));
        }
        if( InputHandler.isKeyDown(KeyCode.A))
        {
            x_component += Math.cos(Math.toRadians(cameraXform.ry.getAngle()+90));
            y_component += Math.sin(Math.toRadians(cameraXform.ry.getAngle()+90));
        }
        if( InputHandler.isKeyDown(KeyCode.D))
        {
            x_component += Math.cos(Math.toRadians(cameraXform.ry.getAngle()-90));
            y_component += Math.sin(Math.toRadians(cameraXform.ry.getAngle()-90));
        }

        if( InputHandler.isKeyDown(KeyCode.SPACE))
        {
            System.out.println(InputHandler.getMouseDX());
        }


        double magnitude = Math.sqrt(x_component*x_component+y_component*y_component);
        if(magnitude != 0)
        {
            test_box_1.setTranslateZ(test_box_1.getTranslateZ() + 2 * x_component / magnitude);
            test_box_1.setTranslateX(test_box_1.getTranslateX() + 2 * y_component / magnitude);
        }

        cameraXform.setTranslateX( test_box_1.getTranslateX());
        cameraXform.setTranslateZ( test_box_1.getTranslateZ());

        light2.setTranslateX(test_box_1.getTranslateX());
        light2.setTranslateZ(test_box_1.getTranslateZ());
        light2.setTranslateY(10);
        light2.setColor(Color.color(0.3,0.3,0.3));
    }

    /*----------------------------------------------------------------
    String[] args : Does nothing at the moment, merely passed on to
    launch. This is the standard way of starting JavaFX applications.
    ----------------------------------------------------------------*/
    public static void main(String[] args){ launch(args ); }
}