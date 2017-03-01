/*----------------------------------------------------------------
    Andre' Green

    InputHandler manages input from the keyboard and mouse.
    Because it is event based, it should update independently of
    the frame rate.
 ----------------------------------------------------------------*/

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import java.util.Random;

public class InputHandler
{
    private static Map<KeyCode,Boolean> isDown_map = new HashMap<>();
    private static boolean mouse_left_down, mouse_right_down;

    /* The absolute position for the mouse is not public, because
    the mouse is getting sent back to the center of the screen each mouse
    event, so that the change in position is accurate and so that the cursor
    doesn't leave the window. */
    private static double mouse_x, mouse_y;
    private static double mouse_dx = 0;
    private static double mouse_dy = 0;

    // This is sort of a hack. I'll try to find a better way to do this.
    private static double drift_prevention = 0;
    private static Random random = new Random();

    /*----------------------------------------------------------------
    Scene scene : The scene to which we listen for mouse & keyboard events.
    This method updates all the variables within this class according
    to the status of the keyboard and mouse.
    ----------------------------------------------------------------*/
    public static void setUpInputHandler( Scene scene )
    {
        scene.setOnKeyPressed(event -> isDown_map.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> isDown_map.put(event.getCode(), false));

        scene.setOnMousePressed( me ->
        {
            mouse_left_down = me.isPrimaryButtonDown();
            mouse_right_down = me.isSecondaryButtonDown();
        });
        scene.setOnMouseReleased( me ->
        {
            mouse_left_down = me.isPrimaryButtonDown();
            mouse_right_down = me.isSecondaryButtonDown();
        });
        scene.setOnMouseMoved(me ->
        {
            // getX, vs getScreenX
            mouse_dx = mouse_x - me.getScreenX();
            mouse_dy = mouse_y - me.getScreenY();
            mouse_x = me.getScreenX();
            mouse_y = me.getScreenY();

            // Will be different depending on screen resolution, so we'll have to
            // figure that out at some point. Just moves back to middle of screen.
            moveCursor(1920/2,1080/2);
            drift_prevention = random.nextDouble();

        });
        scene.setOnMouseDragged(me ->
        {
            mouse_x = me.getX();
            mouse_y = me.getY();
        });
    }

    /*----------------------------------------------------------------
    KeyCode keycode : The code for the key you wish to query. (e.g. KeyCode.A)
    This method just returns true/false depending on whether that key is down.
    The method also returns false if the key is not present in the map.
    ----------------------------------------------------------------*/
    public static boolean isKeyDown( KeyCode keycode )
    {
        if( !isDown_map.containsKey(keycode) ) return false;
        return isDown_map.get(keycode);
    }

    /*----------------------------------------------------------------
    No input arguments for each of these - they simply return information
    about the state of the mouse (e.g. position, which buttons are down, etc).
    ----------------------------------------------------------------*/
    public static boolean isMouseLeftDown()
    {
        return mouse_left_down;
    }
    public static boolean isMouseRightDown()
    {
        return mouse_right_down;
    }
    public static double getMouseDX()
    {
        return mouse_dx;
    }
    public static double getMouseDY()
    {
        return mouse_dy;
    }
    public static double getDriftPrevention() { return drift_prevention; }

    /*----------------------------------------------------------------
    int screenX, screenY : The location on the computer screen (not the
    JavaFX window) to which the cursor is going to move.
     ---------------------------------------------------------------*/
    private static void moveCursor(int screenX, int screenY) {
        Platform.runLater(() -> {
            try {
                Robot robot = new Robot();
                robot.mouseMove(screenX, screenY);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
    }

}
