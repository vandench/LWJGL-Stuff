package testing.lwjgl.event.handler;

import org.lwjgl.glfw.GLFW;

import logger.Log;
import testing.lwjgl.Game;
import testing.lwjgl.event.EventSubscription;
import testing.lwjgl.event.events.KeyInputEvent;
import testing.lwjgl.player.Camera;
import testing.lwjgl.reference.Properties;

public class KeyInputHandler
{
    @EventSubscription
    public void handleEvent(KeyInputEvent event)
    {
        if(event.getKey() == GLFW.GLFW_KEY_ESCAPE && event.getAction() == GLFW.GLFW_RELEASE)
        {
            GLFW.glfwSetWindowShouldClose(Game.getInstance().getWindow().getWindowHandle(), GLFW.GLFW_TRUE);
        }
    }
}