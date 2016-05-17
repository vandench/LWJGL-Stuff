package testing.lwjgl.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.reference.Properties;

public class WindowHandler implements ICleanUpAble
{
    private long m_window;

    public WindowHandler(String name)
    {
        CleanUpHandler.getInstance().addCleanUpAble(this);
        createWindow(name);
    }

    public void createWindow(String name)
    {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, Properties.GAME.FPS);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        m_window = GLFW.glfwCreateWindow(Properties.GAME.WIDTH, Properties.GAME.HEIGHT, name, Properties.GAME.FULLSCREEN ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL, MemoryUtil.NULL);
        verify();
    }

    @Override
    public void cleanUp()
    {
        GLFW.glfwDestroyWindow(m_window);
    }

    private void verify()
    {
        if(m_window == MemoryUtil.NULL) { throw new RuntimeException("Failed to create the GLFW window"); }

        if(!Properties.GAME.FULLSCREEN)
        {
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(m_window, (vidmode.width() - Properties.GAME.WIDTH) / 2, (vidmode.height() - Properties.GAME.HEIGHT) / 2);
        }
        GLFW.glfwMakeContextCurrent(m_window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(m_window);
    }

    public long getWindowHandle()
    {
        if(m_window == MemoryUtil.NULL)
        {
            createWindow("window handle not initialized");
        }
        return m_window;
    }
}