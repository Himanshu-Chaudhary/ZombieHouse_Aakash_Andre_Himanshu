/*----------------------------------------------------------------
    Andre' Green

    InputHandler manages input from the keyboard and mouse.
    Because it is event based, it should update independently of
    the frame rate.
 ----------------------------------------------------------------*/

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;

public class InputHandler
{
    private static Map<KeyCode,Boolean> isDown_map = new HashMap<>();
    private static boolean mouse_left_down, mouse_right_down;
    private static double mouse_x, mouse_y;

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
            mouse_x = me.getX();
            mouse_y = me.getY();
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
    public static double getMouseX()
    {
        return mouse_x;
    }
    public static double getMouseY()
    {
        return mouse_y;
    }

}
