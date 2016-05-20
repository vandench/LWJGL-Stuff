package testing.lwjgl.reference;

import testing.lwjgl.event.EventBus;
import testing.lwjgl.player.Camera;
import testing.lwjgl.renderer.Renderer;
import testing.lwjgl.renderer.Scene;
import testing.lwjgl.shader.Shader;
import testing.lwjgl.window.WindowHandler;

public class Game
{
    private Game() {}

    public static final EventBus EVENT_BUS       = new EventBus();
    public static boolean        DEV_ENVIRONMENT = false;
    public static boolean        DEBUG_MODE      = false;
    public static WindowHandler  WINDOW;
    public static long           WINDOW_HANDLE;
    public static Renderer       RENDERER;
    public static Camera         CAMERA;
    public static Shader         SHADER;

    public static int            FPS;
    public static boolean        VSYNC;
    public static String         AA;
    public static String         RESOULUTION;
    public static int            WIDTH;
    public static int            HEIGHT;
    public static boolean        FULLSCREEN;
    public static float          FOV;
    public static float          VIEW_DISTANCE;
}