package testing.lwjgl.util;

import org.joml.Vector3f;

import logger.Log;
import testing.lwjgl.model.Model;

public class Debugging
{
    public static Model getTextureTestModel()
    {
        return new Model(new float[] { -0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f }, new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f }, new float[] { 0.0f, 0.0f, 0.0f, 0.0f }, new int[] { 0, 1, 3, 3, 1, 2 });
    }
    
    public static void debugVector3f(Vector3f in)
    {
        Log.debug("(" + in.x + ", " + in.y + ", " + in.z + ")");
    }
}
