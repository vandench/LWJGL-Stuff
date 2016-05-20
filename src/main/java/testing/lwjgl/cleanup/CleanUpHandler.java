package testing.lwjgl.cleanup;

import java.util.ArrayList;
import java.util.List;

public class CleanUpHandler
{
    private static final List<ICleanUpAble> cleanUpAbles = new ArrayList<ICleanUpAble>();
    
    public static void addCleanUpAble(ICleanUpAble cleanUp)
    {
        cleanUpAbles.add(cleanUp);
    }
    
    public static void cleanUp()
    {
        for(ICleanUpAble cleanUpAble : cleanUpAbles)
        {
            cleanUpAble.cleanUp();
        }
    }
}