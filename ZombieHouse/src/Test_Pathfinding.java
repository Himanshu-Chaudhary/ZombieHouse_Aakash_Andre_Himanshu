/*----------------------------------------------------------------
    Andre' Green

    A test class for Pathfinding class.

    Finds and lights up the shortest path to 20,20 on the board from the
    player's current position, avoiding walls on the way.

    Not up to standard, so it's ugly code, but seeing as it's just for
    fiddling around with I don't think we need to worry about it for now.
 ----------------------------------------------------------------*/

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;
import java.util.Random;

//they just made the floor a bunch of boxes, not planes.
//they do have walls as planes though, so that's alright I guess.

public class Test_Pathfinding extends Application
{

    public static PathNode[][] board = new PathNode[40][40];
    Random random = new Random();

    PhongMaterial stone = new PhongMaterial();

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
    private static final double CAMERA_INITIAL_DISTANCE = -50;
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

        for(int x = 0; x < 40; x++)
        {
            for(int y = 0; y < 40; y++)
            {
                board[x][y] = new PathNode(x,y,0);
                if(x==39 || y == 39 || x == 0 || y == 0) board[x][y] = null;
                if( x > 15 && y > 15 && x%6 == 0 && y%6 == 0) board[x][y] = null;
            }
        }
        for(int i = 1; i < 20; i++)
        {
            board[10][i] = null;
        }

        final PhongMaterial grayMaterial = new PhongMaterial();
        grayMaterial.setDiffuseColor(Color.DARKGRAY);
        grayMaterial.setSpecularColor(Color.DARKGRAY);
        Reflection reflection = new Reflection();

        for(int i = 0; i < 40; i++)
        {
            for(int j = 0; j < 40; j++)
            {
                floorGroup.getChildren().add(new Box(10,10,10));
                floorGroup.getChildren().get(i*40+j).setTranslateX((i)*10);
                floorGroup.getChildren().get(i*40+j).setTranslateZ((j)*10);
                floorGroup.getChildren().get(i*40+j).setTranslateY(-10);
                ((Box) floorGroup.getChildren().get(i*40+j)).setMaterial(grayMaterial);
                //((Box) floorGroup.getChildren().get(i*40+j)).setEffect(reflection);

            }
        }
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        //redMaterial.setDiffuseColor(Color.DARKRED);
        //redMaterial.setSpecularColor(Color.RED);

        Image image = new Image(getClass().getResourceAsStream("stonebricks1.jpg"));
        Image image_s = new Image(getClass().getResourceAsStream("stonebricks1_s.jpg"));
        Image image_n = new Image(getClass().getResourceAsStream("parlor_wall_n.jpg"));
        stone.setSpecularMap(image_s);
        stone.setBumpMap(image_n);
        stone.setDiffuseMap(image);
        stone.setSpecularColor(Color.color(0.8,0.25,0));
        stone.setDiffuseColor(Color.color(0.3,0.2,0.3));
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
        root.getChildren().removeAll(root.getChildren()); // wipe em out
        light = new PointLight();
        light2 = new PointLight();
        //root.getChildren().add(light);
        root.getChildren().add(light2);

        test_box_1.setTranslateX(10);
        test_box_1.setTranslateZ(10);

        buildFloor();
        root.getChildren().add(floorGroup);

        root.setDepthTest(DepthTest.ENABLE);
        buildAxes();
        root.getChildren().add(test_box_1);
        buildCamera();
        Scene scene2 = new Scene( root, 1024, 768, true );

        // Handle the keyboard and mouse.
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

    /*----------------------------------------------------------------
    No input arguments.
    This method simply updates our scene, moving one sphere according
    to where the mouse cursor is on the screen, and the other according
    to the W A S D keys pressed by the player.
    ----------------------------------------------------------------*/
    private void update()
    {

        test_box_1.setEffect(new GaussianBlur());

        int x = (int) (test_box_1.getTranslateX()/10);
        int z = (int) (test_box_1.getTranslateZ()/10);
        //so long as x and z are valid, then we can make the move. Otherwise don't.


        /* do the pathfinding */

        double dist;
        for(int i = 0; i < 40; i++)
        {
            for(int j = 0; j < 40; j++)
            {
                dist = Math.sqrt(Math.pow(floorGroup.getChildren().get(i*40+j).getTranslateX()-test_box_1.getTranslateX(),2)+
                        Math.pow(floorGroup.getChildren().get(i*40+j).getTranslateZ()-test_box_1.getTranslateZ(),2));
                dist = dist/100;
                if(dist > 1) dist = 1;
                dist = (1-dist)/2;

                ((Box) floorGroup.getChildren().get(i+40*j)).setMaterial( stone );

                // we should have an array of phong materials for all the tiles.
                //((Box)floorGroup.getChildren().get(i*40+j)).setMaterial( new PhongMaterial(Color.color(dist,dist,dist)){{this.setSpecularColor(Color.BLACK);}});
                if( board[i][j] != null)
                {
                    //((Box) floorGroup.getChildren().get(i * 40 + j)).setMaterial(new PhongMaterial(Color.GREEN));
                    floorGroup.getChildren().get(i*40+j).setTranslateY(-10);
                }
                else
                {
                    //((Box) floorGroup.getChildren().get(i * 40 + j)).setMaterial(new PhongMaterial(Color.WHITE));
                    floorGroup.getChildren().get(i*40+j).setTranslateY(0);
                    ((Box) floorGroup.getChildren().get(i*40+j)).setHeight(100);
                }

            }
        }

        lightPath(Pathfinding.getHeading(board, board[x+1][z+1], board[20][20]), board[20][20]);

        //System.out.println(x + " " + z);
        ((Box) floorGroup.getChildren().get(x*40+z)).setMaterial( new PhongMaterial(Color.MAGENTA));



        Bloom bloom = new Bloom();
        bloom.setThreshold(0.1);
        test_box_1.setEffect(bloom);

        light.setTranslateX(test_box_1.getTranslateX());
        light.setTranslateZ(test_box_1.getTranslateZ());
        light.setTranslateY(-20);
        light.setColor(Color.WHITE);

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

        double x_component = 0;
        double y_component = 0;
        double speed = 1.23;
        if( InputHandler.isKeyDown(KeyCode.SHIFT))
        {
            speed = 3;
        }
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
            //System.out.println(((test_box_1.getTranslateZ() + 2 * x_component / magnitude)));
            if( (test_box_1.getTranslateX() + speed * y_component / magnitude) > 10 &&
                    (test_box_1.getTranslateX() + speed * y_component / magnitude) < 420)
            {
                test_box_1.setTranslateX(test_box_1.getTranslateX() + speed * y_component / magnitude);
            }
            if( (test_box_1.getTranslateZ() + speed * x_component / magnitude) > 10 &&
                    (test_box_1.getTranslateZ() + speed * x_component / magnitude) < 420)
            {
                test_box_1.setTranslateZ(test_box_1.getTranslateZ() + speed * x_component / magnitude);
            }
        }

        cameraXform.setTranslateX( test_box_1.getTranslateX());
        cameraXform.setTranslateZ( test_box_1.getTranslateZ());

        light2.setTranslateX(test_box_1.getTranslateX());
        light2.setTranslateZ(test_box_1.getTranslateZ());
        light2.setTranslateY(10);
        light2.setColor(Color.color(0.8,0.8,0.8));
    }

    /*----------------------------------------------------------------
    String[] args : Does nothing at the moment, merely passed on to
    launch. This is the standard way of starting JavaFX applications.
    ----------------------------------------------------------------*/
    public static void main(String[] args){ launch(args ); }

    private void lightPath(Map<PathNode, PathNode> m, PathNode n)
    {
        if (m == null) return;
        if (n == null) return;
        lightPath( m, m.get(n));
        ((Box) floorGroup.getChildren().get(n.x*40+n.y)).setMaterial( new PhongMaterial(Color.CYAN));
        //System.out.println("--" + n);
    }


}
