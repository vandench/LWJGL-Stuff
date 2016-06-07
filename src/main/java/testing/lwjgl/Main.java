package testing.lwjgl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;

import logger.Log;
import testing.lwjgl.config.Config;
import testing.lwjgl.event.handler.CameraMovementHandler;
import testing.lwjgl.event.handler.ErrorHandler;
import testing.lwjgl.event.handler.KeyInputHandler;
import testing.lwjgl.font.FontCreator;
import testing.lwjgl.font.FontCreator.FontFile;
import testing.lwjgl.model.GameObject;
import testing.lwjgl.model.Model;
import testing.lwjgl.player.Camera;
import testing.lwjgl.reference.Game;
import testing.lwjgl.renderer.Renderer;
import testing.lwjgl.resources.ResourceHelper;
import testing.lwjgl.shader.Light.Attenuation;
import testing.lwjgl.shader.Light.PointLight;
import testing.lwjgl.shader.Shader;
import testing.lwjgl.util.Color;
import testing.lwjgl.util.Debug;
import testing.lwjgl.window.WindowHandler;
import testing.lwjgl.world.World;
import utils.input.IO;
import utils.properties.Properties;

public class Main
{
    public Main()
    {
        try
        {
            new Config().init();
            init();
            loop();
        } finally
        {
            Game.CLEAN_UP_HANDLER.cleanUp();
            GLFW.glfwTerminate();
        }
    }
    
    private void init()
    {
        GLFW.glfwSetErrorCallback(new ErrorHandler());
        if(GLFW.glfwInit() != GLFW.GLFW_TRUE) { throw new IllegalStateException("Unable to initialize GLFW"); }
        new WindowHandler("Hello World!");

//        new Camera(new Vector3f(0.0f, 10.0f, 0.0f), new Vector3f(25.0f, 135.0f, 0.0f));
        new Camera(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f));
        Game.EVENT_BUS.register(new KeyInputHandler());
        Game.EVENT_BUS.register(new CameraMovementHandler());

        new Shader();
        Game.SHADER.createShader(ResourceHelper.getShaderAsString("/VertexShader.glsl"), GL20.GL_VERTEX_SHADER);
        Game.SHADER.createShader(ResourceHelper.getShaderAsString("/FragmentShader.glsl"), GL20.GL_FRAGMENT_SHADER);
        Game.SHADER.link();
        new Renderer();
        new World();
        
        Debug.addDebugObjects(25, 250);
//        Debug.debugTexture(Textures.CUBE.getTexID());
        try
        {
            FontFile ff = FontCreator.loadCharacters(ResourceHelper.getFont("/verdana.fnt"));
            Model[] ms = FontCreator.genModels(ff);
//            for(Model m : ms)
            {
                Game.RENDERER.add(new GameObject(ms[82], new Vector3f(1, 10, -20)));
            }
        } catch(IOException e) { Log.trace(e); }
        Game.RENDERER.add(new PointLight(new Vector3f(Color.RED), new Vector3f(0.0f, 15.0f, 0.0f), 500.0f, new Attenuation(0.0f, 0.0f, 1.0f)));
    }

    private void update()
    {
        GLFW.glfwPollEvents();
        Game.CAMERA.update();
    }

    private void render() { Game.RENDERER.render(); }
    
    private void loop()
    {
        long lastTime = System.currentTimeMillis();
        float delta = 0.0f;
        float ns = 1000.0f / 60.0f;
        long timer = lastTime;
        int updates = 0;
        int frames = 0;
        long now;

        while(GLFW.glfwWindowShouldClose(Game.WINDOW_HANDLE) == GLFW.GLFW_FALSE)
        {
            now = System.currentTimeMillis();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1.0f && updates < 60)
            {
                update();
                ++updates;
                --delta;
            }
            while(delta <= 1.0f)
            {
                now = System.currentTimeMillis();
                delta += (now - lastTime) / ns;
                lastTime = now;
                render();
                ++frames;
            }
            if(lastTime - timer > 1000)
            {
                timer = lastTime;
                Log.debug("UPS: " + updates + "\tFPS: " + frames);
                Log.debug("Memory Usage (MiB): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
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
        Log.createLogger(Game.DEBUG_MODE, IO.toPath(ResourceHelper.getGameDataDir() + "/logs/"));
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
        Log.close();
    }
}