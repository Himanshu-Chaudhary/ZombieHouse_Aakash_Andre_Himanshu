package input;/*----------------------------------------------------------------
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

/**
 * @author Andre' Green
 *
 * handles input which is used to update the game
 */

public class InputHandler
{
    private static Map<KeyCode,Boolean> isDown_map = new HashMap<>();
    private static boolean mouse_left_down, mouse_right_down;

    /* The absolute position for the mouse is not used, because
    the mouse is getting sent back to the center of the screen each mouse
    event, so that the change in position is accurate and so that the cursor
    doesn't leave the window. */
    private static double mouse_dx = 0;
    private static double mouse_dy = 0;

    /*  A flip-flop mechanism is uses to switch between true and false.
     if the flip-flop value in memory is the same, don't update. If it isn't, do update. */
    private static double drift_prevention = 0;
    private static Random random = new Random();


    /**
     * @param scene
     * The scene to which we listen for mouse & keyboard events.
     *
     * This method updates all the variables within this class according
     * to the status of the keyboard and mouse.
     */
    public static void setUpInputHandler( Scene scene )
    {
        scene.setOnKeyPressed(event -> isDown_map.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> isDown_map.put(event.getCode(), false));

        // move into seperate function to avoid repeated code
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

            // Avoid triggering twice because of moveCursor's instruction.
            // If a perfect match to the center, no movement is updated.
            if(me.getScreenX()-960 != 0)
                mouse_dx = me.getScreenX()-960;
            if(me.getScreenY()-540 != 0)
                mouse_dy = me.getScreenY()-540;

            moveCursor(960,540);
            drift_prevention = random.nextDouble();

        });
        scene.setOnMouseDragged(me ->
        {

        });
    }


    /**
     * @param keycode
     * The code for the key you wish to query. (e.g. KeyCode.A)
     * @return
     *  This method just returns true/false depending on whether that key is down.
     *  The method also returns false if the key is not present in the map.
     */
    public static boolean isKeyDown( KeyCode keycode )
    {
        if( !isDown_map.containsKey(keycode) ) return false;
        return isDown_map.get(keycode);
    }

    /* No input arguments for each of these - they simply return information
    about the state of the mouse (e.g. position, which buttons are down, etc).*/
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

    /* int screenX, screenY : The location on the computer screen (not the
    JavaFX window) to which the cursor is going to move.
    This itself counts as a mouse movement, though, so we have to be
    careful not to count it at being an actual motion from the user. */
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
