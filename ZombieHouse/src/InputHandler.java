// Andre

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;

public class InputHandler
{
    public static Map<KeyCode,Boolean> isDown_map = new HashMap<>();
    private static boolean mouse_left_down, mouse_right_down;
    private static double mouse_x, mouse_y;

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

    public static boolean isKeyDown( KeyCode keycode )
    {
        if( !isDown_map.containsKey(keycode) ) return false;
        return isDown_map.get(keycode);
    }

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
