package testing.lwjgl;

import static org.lwjgl.system.MemoryUtil.memDecodeUTF8;

import java.lang.reflect.Field;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.APIUtil.TokenFilter;

import logger.Log;
import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;

public class ErrorHandler extends GLFWErrorCallback implements ICleanUpAble
{
    private final Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens(new TokenFilter() {
        @Override
        public boolean accept(Field field, int value)
        {
            return 0x10000 < value && value < 0x20000;
        }
    }, null, GLFW.class);

    public ErrorHandler()
    {
        CleanUpHandler.getInstance().addCleanUpAble(this);
    }

    @Override
    public void invoke(int error, long description)
    {
        String msg = memDecodeUTF8(description);

        Log.error("[LWJGL] " + ERROR_CODES.get(error) + " error");
        Log.error("\tDescription : " + msg);
        Log.error("\tStacktrace  :");
        Log.trace(Thread.currentThread().getStackTrace());
    }

    @Override
    public void cleanUp()
    {
        super.release();
    }
}