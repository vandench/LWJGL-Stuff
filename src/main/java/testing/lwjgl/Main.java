package testing.lwjgl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import logger.Log;
import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.config.Config;
import testing.lwjgl.event.handler.CameraMovementHandler;
import testing.lwjgl.event.handler.KeyInputHandler;
import testing.lwjgl.player.Camera;
import testing.lwjgl.reference.Properties;
import testing.lwjgl.reference.Textures;
import testing.lwjgl.renderer.GameObject;
import testing.lwjgl.renderer.Model;
import testing.lwjgl.renderer.Scene;
import testing.lwjgl.resources.ResourceHelper;
import testing.lwjgl.shader.Shader;
import testing.lwjgl.window.WindowHandler;

public class Main
{
    private GLFWErrorCallback       m_errorCallback;
    private GLFWKeyCallback         m_keyCallback;
    private GLFWCursorPosCallback   m_cursorPosCallback;
    private GLFWCursorEnterCallback m_cursorEnterCallback;
    private GLFWMouseButtonCallback m_mouseButtonCallback;

    private WindowHandler           m_window;
    private Shader                  m_shader;
    private Camera                  m_camera;
    private Scene                   m_scene;

    public void run()
    {
        try
        {
            new Config().init();
            init();
            loop();
        } finally
        {
            CleanUpHandler.getInstance().cleanUp();
            GLFW.glfwTerminate();
        }
    }

    private void init()
    {
        GLFW.glfwSetErrorCallback(m_errorCallback = new ErrorHandler());

        if(GLFW.glfwInit() != GLFW.GLFW_TRUE) { throw new IllegalStateException("Unable to initialize GLFW"); }
        m_window = new WindowHandler("Hello World!");
        Game.getInstance().setWindow(m_window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

        m_camera = new Camera(0.0f, 0.0f, 0.0f);
        Game.getInstance().getEventBus().register(new KeyInputHandler());
        Game.getInstance().getEventBus().register(new CameraMovementHandler(m_camera));
        GLFW.glfwSetKeyCallback(m_window.getWindowHandle(), m_keyCallback = new InputHandler.KeyInput());
        GLFW.glfwSetCursorPosCallback(m_window.getWindowHandle(), m_cursorPosCallback = new InputHandler.CursorMoveInput());
        GLFW.glfwSetCursorEnterCallback(m_window.getWindowHandle(), m_cursorEnterCallback = new InputHandler.CursorEnterInput());
        GLFW.glfwSetMouseButtonCallback(m_window.getWindowHandle(), m_mouseButtonCallback = new InputHandler.MouseButtonInput());
        GLFW.glfwSetInputMode(m_window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        m_shader = new Shader();
        m_shader.createShader(ResourceHelper.getResourceAsString("/assets/shaders/VertexShader.glsl"), GL20.GL_VERTEX_SHADER);
        m_shader.createShader(ResourceHelper.getResourceAsString("/assets/shaders/FragmentShader.glsl"), GL20.GL_FRAGMENT_SHADER);
        m_shader.link();
        m_scene = new Scene(m_shader, m_camera);

        float back = -3.0f;
        Model a = mod();
        Model b = mod();
        Model c = mod();
        Model d = mod();
        Model e = mod();
        Model f = mod();
        Model g = mod();
        Model h = mod();

        Model x = mod();

        GameObject nb = new GameObject(x.setTexID(Textures.GRASS_BLOCK.getTexID()));
        nb.setZ(-back);
        m_scene.add(nb);

        GameObject tmp = new GameObject(a.setTexID(Textures.BLACK.getTexID()));
        tmp.setZ(back);
        m_scene.add(tmp);

        tmp = new GameObject(b.setTexID(Textures.WHITE.getTexID()));
        tmp.setZ(back * 2);
        m_scene.add(tmp);

        tmp = new GameObject(c.setTexID(Textures.RED.getTexID()));
        tmp.setZ(back * 3);
        m_scene.add(tmp);

        tmp = new GameObject(d.setTexID(Textures.GREEN.getTexID()));
        tmp.setZ(back * 4);
        m_scene.add(tmp);

        tmp = new GameObject(e.setTexID(Textures.BLUE.getTexID()));
        tmp.setZ(back * 5);
        m_scene.add(tmp);

        tmp = new GameObject(f.setTexID(Textures.RG.getTexID()));
        tmp.setZ(back * 6);
        m_scene.add(tmp);

        tmp = new GameObject(g.setTexID(Textures.RB.getTexID()));
        tmp.setZ(back * 7);
        m_scene.add(tmp);

        tmp = new GameObject(h.setTexID(Textures.GB.getTexID()));
        tmp.setZ(back * 8);
        m_scene.add(tmp);
    }

    public Model mod()
    {
        return new Model(new float[] { -0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f }, new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f }, new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, new int[] { 0, 1, 3, 3, 1, 2 });
    }

    private void update()
    {
        GLFW.glfwPollEvents();
        for(GameObject obj : m_scene.getWorldObjects())
        {
            // obj.incrementRotX(0.5f);
            // obj.incrementRotY(0.5f);
            // obj.incrementRotZ(0.5f);
            // if(obj.getRotX() > 360)
            // {
            // obj.setRotX(0);
            // }
            // if(obj.getRotY() > 360)
            // {
            // obj.setRotY(0);
            // }
            // if(obj.getRotZ() > 360)
            // {
            // obj.setRotZ(0);
            // }
        }
        m_camera.update();
    }

    private void render()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        m_shader.bind();
        m_camera.render(m_shader);
        m_shader.setUniform("textureSampler", 0);
        m_scene.render();
        m_shader.unbind();
        GLFW.glfwSwapBuffers(m_window.getWindowHandle());
    }

    private void loop()
    {
        GL11.glClearColor(1.0f, 0.5f, 0.1f, 0.0f);

        long lastTime = System.currentTimeMillis();
        float delta = 0.0f;
        double ns = 1000.0d / 60.0d;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        long now = 0;
        while(GLFW.glfwWindowShouldClose(m_window.getWindowHandle()) == GLFW.GLFW_FALSE)
        {
            now = System.currentTimeMillis();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1.0f)
            {
                update();
                ++updates;
                --delta;
            }
            render();
            ++frames;
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                Log.info("UPS: " + updates + "\tFPS: " + frames);
                updates = 0;
                frames = 0;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException
    {
        boolean fixed = false;
        for(String arg : args)
        {
            if(arg.equals("debug"))
            {
                Game.getInstance().setDebugMode(true);
            }
            if(arg.equals("dev"))
            {
                Game.getInstance().setDevEnvironment(true);
            }
            if(arg.equals("fix"))
            {
                fixed = true;
            }
        }
        Log.createLogger(Game.getInstance().isDevEnvironment());
        if(!Properties.OS.IS_MAC)
        {
            fixed = true;
        }
        if(!fixed)
        {

            List<String> arguments = new ArrayList<String>();
            arguments.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
            arguments.add("-XstartOnFirstThread");
            arguments.add("-jar");
            arguments.add(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            arguments.add("fix");
            StringBuilder con = new StringBuilder(arguments.get(0)).append(" ");
            for(int i = 1; i < arguments.size(); ++i)
            {
                con.append(arguments.get(i)).append(" ");
            }
            con.deleteCharAt(con.length() - 1);
            Log.warn("This applictation will be restarting,\n\tthis is because LWJGL 3 running on Mac OSX\n\t" + "requires jvm argument '-XstartOnFirstThread', this will stop on console logging.\n\t" + "If you would like to run this application with logging run the command\n\t'" + con.toString() + "'");
            Process pro = new ProcessBuilder(arguments).start();
        } else
        {
            new Main().run();
        }
    }
}