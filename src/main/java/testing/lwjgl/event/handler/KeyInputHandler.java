package testing.lwjgl.event.handler;

import org.lwjgl.glfw.GLFW;

import logger.Log;
import testing.lwjgl.event.EventSubscription;
import testing.lwjgl.event.events.ExitEvent;
import testing.lwjgl.event.events.KeyInputEvent;
import testing.lwjgl.reference.Game;

public class KeyInputHandler
{
    @EventSubscription(priority = 1)
    public void onKeyPress(KeyInputEvent event)
    {
        int key = event.getKey();
        int action = event.getAction();
        
        if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) { Game.EVENT_BUS.dispatch(new ExitEvent()); }
        
        if(key == GLFW.GLFW_KEY_RIGHT_ALT && action == GLFW.GLFW_PRESS) { Log.setDebugMode(!Log.isDebugMode()); }
    }
    
    @EventSubscription
    public void onExit(ExitEvent event)
    {
        Log.info("Exiting with status code " + event.getStatusCode());
        GLFW.glfwSetWindowShouldClose(Game.WINDOW_HANDLE, GLFW.GLFW_TRUE);
    }
}