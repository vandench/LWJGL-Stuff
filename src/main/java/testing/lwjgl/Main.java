package testing.lwjgl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
import testing.lwjgl.event.handler.ErrorHandler;
import testing.lwjgl.event.handler.KeyInputHandler;
import testing.lwjgl.model.GameObject;
import testing.lwjgl.model.Model;
import testing.lwjgl.player.Camera;
import testing.lwjgl.reference.Game;
import testing.lwjgl.reference.Models;
import testing.lwjgl.reference.Properties;
import testing.lwjgl.reference.Textures;
import testing.lwjgl.renderer.Renderer;
import testing.lwjgl.renderer.Scene;
import testing.lwjgl.resources.ResourceHelper;
import testing.lwjgl.shader.Light.Attenuation;
import testing.lwjgl.shader.Light.PointLight;
import testing.lwjgl.shader.Shader;
import testing.lwjgl.util.Color;
import testing.lwjgl.util.Debugging;
import testing.lwjgl.window.WindowHandler;
import testing.lwjgl.world.World;

public class Main
{
    PointLight pl;

    public Main()
    {
        try
        {
            new Config().init();
            init();
            loop();
        } finally
        {
            CleanUpHandler.cleanUp();
            GLFW.glfwTerminate();
        }
    }

    private void init()
    {
        GLFW.glfwSetErrorCallback(new ErrorHandler());
        if(GLFW.glfwInit() != GLFW.GLFW_TRUE) { throw new IllegalStateException("Unable to initialize GLFW"); }
        new WindowHandler("Hello World!");

        new Camera(new Vector3f(0.0f, 10.0f, 0.0f), new Vector3f(25.0f, 135.0f, 0.0f));
        Game.EVENT_BUS.register(new KeyInputHandler());
        Game.EVENT_BUS.register(new CameraMovementHandler());

        new Shader();
        Game.SHADER.createShader(ResourceHelper.getShaderAsString("VertexShader.glsl"), GL20.GL_VERTEX_SHADER);
        Game.SHADER.createShader(ResourceHelper.getShaderAsString("FragmentShader.glsl"), GL20.GL_FRAGMENT_SHADER);
        Game.SHADER.link();
        new Renderer();
        new World();
        Game.RENDERER.add(new GameObject(Models.DRAGON.setTexID(Textures.CUBE.getTexID())));
        
        Game.RENDERER.add(new PointLight(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 15.0f, 0.0f), 100000.0f, new Attenuation(0.0f, 0.0f, 1.0f)));
    }

    private void update()
    {
        GLFW.glfwPollEvents();
        for(GameObject obj : Game.RENDERER.getWorldObjects().values()) {}
        Game.CAMERA.update();
    }

    private void render()
    {
        Game.RENDERER.render();
    }

    private void loop()
    {
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);

        long lastTime = System.currentTimeMillis();
        float delta = 0.0f;
        double ns = 1000.0d / 60.0d;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        long now = 0;
        while(GLFW.glfwWindowShouldClose(Game.WINDOW_HANDLE) == GLFW.GLFW_FALSE)
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
                Log.debug("UPS: " + updates + "\tFPS: " + frames);
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
            if(arg.equals("debug")) { Game.DEBUG_MODE = true; }
            if(arg.equals("dev")) { Game.DEV_ENVIRONMENT = true; }
            if(arg.equals("fix")) { fixed = true; }
        }
        Log.createLogger(Game.DEBUG_MODE);
        if(!Properties.OS.IS_MAC) { fixed = true; }
        if(!fixed)
        {
            List<String> arguments = new ArrayList<String>();
            arguments.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
            arguments.add("-XstartOnFirstThread");
            arguments.add("-jar");
            arguments.add(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            arguments.add("fix");
            StringBuilder con = new StringBuilder();
            for(String arg : arguments) { con.append(arg).append(" "); }
            con.deleteCharAt(con.length() - 1);
            Log.warn("This applictation will be restarting,\n\tthis is because LWJGL 3 running on Mac OSX\n\trequires jvm argument '-XstartOnFirstThread', this will stop on console logging.\n\tIf you would like to run this application with logging run the command\n\t'" + con.toString() + "'");
            Process pro = new ProcessBuilder(arguments).start();
        } else
        {
            new Main();
        }
    }
}