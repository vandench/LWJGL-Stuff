package testing.lwjgl.util;

import org.joml.Vector3f;
import org.joml.Vector4f;

import logger.Log;
import testing.lwjgl.model.GameObject;
import testing.lwjgl.model.Model;
import testing.lwjgl.reference.Game;
import testing.lwjgl.reference.Models;
import testing.lwjgl.reference.Textures;

public class Debug
{
    private static float[] vertices = new float[] { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f };
    private static float[] textures = new float[] { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f };
    private static int[] indices = new int[] { 0, 1, 3, 3, 1, 2 };
        
    public static Model getTextureTestModel() { return new Model(vertices, 2, textures, 2, null, 2, indices); }
    
    public static void debugTexture(int textureID) { Game.RENDERER.add(new GameObject(new Model(getTextureTestModel()).setTexID(textureID), new Vector3f(0.0f, 0.0f, -3.0f))); }
    
    public static void debugVector3f(Vector3f in) { Log.debug("(" + in.x + ", " + in.y + ", " + in.z + ")"); }
    
    public static void debugVector4f(Vector4f in) { Log.debug("(" + in.x + ", " + in.y + ", " + in.z + ", " + in.w + ")"); }
    
    public static void addDebugObjects(int radius, int numObjects)
    {
        float r = 1;
        for(int i = 0; i < numObjects; ++i)
        {
            float x = (float) (Math.random() * radius * r);
            r = updateRandom(r);
            float y = (float) (Math.random() * radius * r);
            r = updateRandom(r);
            float z = (float) (Math.random() * radius * r);
            r = updateRandom(r);
            
            int rx = (int) (Math.random() * 500) % 360;
            int ry = (int) (Math.random() * 500) % 360;
            int rz = (int) (Math.random() * 500) % 360;
            
//            rx = 0; ry = 0; rz = 0;
            
            Game.RENDERER.add(new GameObject(new Model(Models.CUBE).setTexID(Textures.CUBE.getTexID()), new Vector3f(x, y, z), new Vector3f(rx, ry, rz)));
        }
    }
    
    private static float updateRandom(float r)
    {
        if(Math.random() * 10 > 5) { r *= -1; }
        return r;
    }
}