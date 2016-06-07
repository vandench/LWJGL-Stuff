package testing.lwjgl.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.event.handler.InputCallback;
import testing.lwjgl.reference.Game;
import testing.lwjgl.util.Logger;

public class WindowHandler implements ICleanUpAble
{
    public WindowHandler(String name)
    {
        Game.CLEAN_UP_HANDLER.addCleanUpAble(this);
        createWindow(name);
        Game.WINDOW = this;
    }

    public void createWindow(String name)
    {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, Game.FPS);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        Game.WINDOW_HANDLE = GLFW.glfwCreateWindow(Game.WIDTH, Game.HEIGHT, name, Game.FULLSCREEN ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, MemoryUtil.NULL);
        verify();
        setupSettings();
    }

    private void setupSettings()
    {
        GL.createCapabilities();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glClearColor(0.7f, 1.0f, 0.3f, 0.0f);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        
        GLFW.glfwSetKeyCallback(Game.WINDOW_HANDLE, new InputCallback.KeyInput());
        GLFW.glfwSetCursorPosCallback(Game.WINDOW_HANDLE, new InputCallback.CursorMoveInput());
        GLFW.glfwSetCursorEnterCallback(Game.WINDOW_HANDLE, new InputCallback.CursorEnterInput());
        GLFW.glfwSetMouseButtonCallback(Game.WINDOW_HANDLE, new InputCallback.MouseButtonInput());
        GLFW.glfwSetInputMode(Game.WINDOW_HANDLE, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    private void verify()
    {
        if(Game.WINDOW_HANDLE == MemoryUtil.NULL) { Logger.traceKill(new RuntimeException("Failed to create the GLFW window")); }
        if(!Game.FULLSCREEN)
        {
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(Game.WINDOW_HANDLE, (vidmode.width() - Game.WIDTH) / 2, (vidmode.height() - Game.HEIGHT) / 2);
        }
        GLFW.glfwMakeContextCurrent(Game.WINDOW_HANDLE);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(Game.WINDOW_HANDLE);
    }
    
    @Override
    public void cleanUp() { GLFW.glfwDestroyWindow(Game.WINDOW_HANDLE); }
}