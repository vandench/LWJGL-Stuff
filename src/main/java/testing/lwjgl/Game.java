package testing.lwjgl;

import testing.lwjgl.event.EventBus;
import testing.lwjgl.window.WindowHandler;

public class Game
{
    private static final Game instance        = new Game();

    private final EventBus    EVENT_BUS       = new EventBus();
    private boolean           DEV_ENVIRONMENT = false;
    private boolean           DEBUG_MODE      = false;
    private WindowHandler WINDOW;

    public EventBus getEventBus()
    {
        return EVENT_BUS;
    }

    public boolean isDevEnvironment()
    {
        return DEV_ENVIRONMENT;
    }

    public void setDevEnvironment(boolean isDev)
    {
        DEV_ENVIRONMENT = isDev;
    }

    public boolean isDebugMode()
    {
        return DEBUG_MODE;
    }

    public void setDebugMode(boolean isDebug)
    {
        DEBUG_MODE = isDebug;
    }
    
    public WindowHandler getWindow()
    {
        return WINDOW;
    }
    
    public void setWindow(WindowHandler window)
    {
        WINDOW = window;
    }

    public static Game getInstance()
    {
        return instance;
    }
}