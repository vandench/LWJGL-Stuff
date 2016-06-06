package testing.lwjgl.util;

import logger.Log;
import testing.lwjgl.event.events.ExitEvent;
import testing.lwjgl.reference.Game;

public class Logger
{
    public static void traceKill(Exception e)
    {
        Log.trace(e);
        Game.EVENT_BUS.dispatch(new ExitEvent(-1));
    }
}
