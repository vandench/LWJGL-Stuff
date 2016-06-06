package testing.lwjgl.cleanup;

import java.util.ArrayList;
import java.util.List;

public class CleanUpHandler
{
    private final List<ICleanUpAble> cleanUpAbles;
    
    public CleanUpHandler() { cleanUpAbles = new ArrayList<ICleanUpAble>(); }
    
    public void addCleanUpAble(ICleanUpAble cleanUp) { cleanUpAbles.add(cleanUp); }
    
    public void cleanUp()
    {
        for(ICleanUpAble cleanUpAble : cleanUpAbles)
        {
            cleanUpAble.cleanUp();
        }
    }
}