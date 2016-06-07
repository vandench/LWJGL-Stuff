package testing.lwjgl.event.handler;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.event.events.EventCursorEnterWindow;
import testing.lwjgl.event.events.EventCursorMove;
import testing.lwjgl.event.events.EventKeyInput;
import testing.lwjgl.event.events.EventMouseButton;
import testing.lwjgl.reference.Game;

public class InputCallback
{
    public static class KeyInput extends GLFWKeyCallback implements ICleanUpAble
    {
        public KeyInput() { Game.CLEAN_UP_HANDLER.addCleanUpAble(this); }

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) { Game.EVENT_BUS.dispatch(new EventKeyInput(key, scancode, action, mods)); }

        @Override
        public void cleanUp() { super.release(); }
    }

    public static class CursorMoveInput extends GLFWCursorPosCallback implements ICleanUpAble
    {
        public CursorMoveInput() { Game.CLEAN_UP_HANDLER.addCleanUpAble(this); }

        @Override
        public void invoke(long window, double xpos, double ypos) { Game.EVENT_BUS.dispatch(new EventCursorMove(xpos, ypos)); }

        @Override
        public void cleanUp() { super.release(); }
    }
    
    public static class CursorEnterInput extends GLFWCursorEnterCallback implements ICleanUpAble
    {
        public CursorEnterInput() { Game.CLEAN_UP_HANDLER.addCleanUpAble(this); }

        @Override
        public void invoke(long window, int entered) { Game.EVENT_BUS.dispatch(new EventCursorEnterWindow(entered == GLFW.GLFW_TRUE ? true : false)); }

        @Override
        public void cleanUp() { super.release(); }
    }

    public static class MouseButtonInput extends GLFWMouseButtonCallback implements ICleanUpAble
    {
        public MouseButtonInput() { Game.CLEAN_UP_HANDLER.addCleanUpAble(this); }

        @Override
        public void invoke(long window, int button, int action, int mods) { Game.EVENT_BUS.dispatch(new EventMouseButton(button, action, mods)); }

        @Override
        public void cleanUp() { super.release(); }
    }
}