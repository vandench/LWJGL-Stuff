package testing.lwjgl.event.handler;

import java.text.DecimalFormat;

import org.lwjgl.glfw.GLFW;

import logger.Log;
import testing.lwjgl.event.EventSubscription;
import testing.lwjgl.event.events.KeyInputEvent;
import testing.lwjgl.reference.Game;

public class KeyInputHandler
{
    @EventSubscription
    public void handleEvent(KeyInputEvent event)
    {
        int key = event.getKey();
        int action = event.getAction();
        
        if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
        {
            GLFW.glfwSetWindowShouldClose(Game.WINDOW_HANDLE, GLFW.GLFW_TRUE);
        }
        
        if(key == GLFW.GLFW_KEY_RIGHT_ALT && action == GLFW.GLFW_PRESS)
        {
            Log.setDebugMode(!Log.isDebugMode());
        }
    }
}