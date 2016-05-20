package testing.lwjgl.event.handler;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.event.events.CursorEnterEvent;
import testing.lwjgl.event.events.CursorMoveEvent;
import testing.lwjgl.event.events.KeyInputEvent;
import testing.lwjgl.event.events.MouseButtonEvent;
import testing.lwjgl.reference.Game;

public class InputHandler
{
    public static class KeyInput extends GLFWKeyCallback implements ICleanUpAble
    {
        public KeyInput()
        {
            CleanUpHandler.addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods)
        {
            Game.EVENT_BUS.dispatch(new KeyInputEvent(key, scancode, action, mods));
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
            CleanUpHandler.addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, double xpos, double ypos)
        {
            Game.EVENT_BUS.dispatch(new CursorMoveEvent(xpos, ypos));
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
            CleanUpHandler.addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, int entered)
        {
            Game.EVENT_BUS.dispatch(new CursorEnterEvent(entered == GLFW.GLFW_TRUE ? true : false));
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
            CleanUpHandler.addCleanUpAble(this);
        }

        @Override
        public void invoke(long window, int button, int action, int mods)
        {
            Game.EVENT_BUS.dispatch(new MouseButtonEvent(button, action, mods));
        }

        @Override
        public void cleanUp()
        {
            super.release();
        }
    }
}