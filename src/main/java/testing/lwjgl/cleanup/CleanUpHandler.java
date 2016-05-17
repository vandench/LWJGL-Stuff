package testing.lwjgl.cleanup;

import java.util.ArrayList;
import java.util.List;

public class CleanUpHandler
{
    private static final CleanUpHandler instance = new CleanUpHandler();
    private List<ICleanUpAble> cleanUpAbles = new ArrayList<ICleanUpAble>();
    
    public void addCleanUpAble(ICleanUpAble cleanUp)
    {
        cleanUpAbles.add(cleanUp);
    }
    
    public List<ICleanUpAble> getCleanUpAbles()
    {
        return cleanUpAbles;
    }
    
    public void cleanUp()
    {
        for(ICleanUpAble cleanUpAble : cleanUpAbles)
        {
            cleanUpAble.cleanUp();
        }
    }
    
    public static CleanUpHandler getInstance()
    {
        return instance;
    }
}