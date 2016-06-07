package testing.lwjgl.event.handler;

import static org.lwjgl.system.MemoryUtil.memDecodeUTF8;

import java.lang.reflect.Field;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.APIUtil.TokenFilter;

import logger.Log;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.reference.Game;

public class ErrorCallback extends GLFWErrorCallback implements ICleanUpAble
{
    private final Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens(new TokenFilter() {
        @Override
        public boolean accept(Field field, int value)
        {
            return 0x10000 < value && value < 0x20000;
        }
    }, null, GLFW.class);

    public ErrorCallback() { Game.CLEAN_UP_HANDLER.addCleanUpAble(this); }

    @Override
    public void invoke(int error, long description)
    {
        Log.error("[LWJGL] " + ERROR_CODES.get(error) + " error");
        Log.error("\tDescription : " + memDecodeUTF8(description));
        Log.error("\tStacktrace  :");
        Log.trace(Thread.currentThread().getStackTrace());
    }

    @Override
    public void cleanUp() { super.release(); }
}