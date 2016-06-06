package testing.lwjgl.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.event.handler.InputHandler;
import testing.lwjgl.reference.Game;

public class WindowHandler implements ICleanUpAble
{
    private long m_windowHandle;

    public WindowHandler(String name)
    {
        Game.CLEAN_UP_HANDLER.addCleanUpAble(this);
        createWindow(name);
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
        m_windowHandle = GLFW.glfwCreateWindow(Game.WIDTH, Game.HEIGHT, name, Game.FULLSCREEN ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, MemoryUtil.NULL);
        verify();
        Game.WINDOW = this;
        Game.WINDOW_HANDLE = m_windowHandle;
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
        
        GLFW.glfwSetKeyCallback(m_windowHandle, new InputHandler.KeyInput());
        GLFW.glfwSetCursorPosCallback(m_windowHandle, new InputHandler.CursorMoveInput());
        GLFW.glfwSetCursorEnterCallback(m_windowHandle, new InputHandler.CursorEnterInput());
        GLFW.glfwSetMouseButtonCallback(m_windowHandle, new InputHandler.MouseButtonInput());
        GLFW.glfwSetInputMode(m_windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    private void verify()
    {
        if(m_windowHandle == MemoryUtil.NULL) { throw new RuntimeException("Failed to create the GLFW window"); }
        if(!Game.FULLSCREEN)
        {
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(m_windowHandle, (vidmode.width() - Game.WIDTH) / 2, (vidmode.height() - Game.HEIGHT) / 2);
        }
        GLFW.glfwMakeContextCurrent(m_windowHandle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(m_windowHandle);
    }
    
    @Override
    public void cleanUp() { GLFW.glfwDestroyWindow(m_windowHandle); }
}