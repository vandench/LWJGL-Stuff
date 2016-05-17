package testing.lwjgl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import logger.Log;
import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.event.events.MouseButtonEvent;
import testing.lwjgl.event.events.CursorEnterEvent;
import testing.lwjgl.event.events.CursorMoveEvent;
import testing.lwjgl.event.events.KeyInputEvent;

public class InputHandler
{
    public static class KeyInput extends GLFWKeyCallback implements ICleanUpAble
    {
        public KeyInput()
        {
            CleanUpHandler.getInstance().addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            Game.getInstance().getEventBus().dispatch(new KeyInputEvent(key, scancode, action, mods));
        }

        @Override
        public void cleanUp()
        {
            super.release();
        }
    }

    public static class CursorMoveInput extends GLFWCursorPosCallback implements ICleanUpAble
    {
        public CursorMoveInput()
        {
            CleanUpHandler.getInstance().addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, double xpos, double ypos)
        {
            Game.getInstance().getEventBus().dispatch(new CursorMoveEvent(xpos, ypos));
        }

        @Override
        public void cleanUp()
        {
            super.release();
        }
    }
    
    public static class CursorEnterInput extends GLFWCursorEnterCallback implements ICleanUpAble
    {
        public CursorEnterInput()
        {
            CleanUpHandler.getInstance().addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, int entered)
        {
            Game.getInstance().getEventBus().dispatch(new CursorEnterEvent(entered == GLFW.GLFW_TRUE ? true : false));
        }

        @Override
        public void cleanUp()
        {
            super.release();
        }
    }

    public static class MouseButtonInput extends GLFWMouseButtonCallback implements ICleanUpAble
    {
        public MouseButtonInput()
        {
            CleanUpHandler.getInstance().addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            Game.getInstance().getEventBus().dispatch(new MouseButtonEvent(button, action, mods));
        }

        @Override
        public void cleanUp()
        {
            super.release();
        }
    }
}