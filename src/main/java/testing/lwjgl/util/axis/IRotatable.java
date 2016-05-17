package testing.lwjgl.util.axis;

import org.joml.Vector3f;

public interface IRotatable
{
    public void setRotX(float x);
    
    public void setRotY(float y);
    
    public void setRotZ(float z);
    
    public float getRotX();
    
    public float getRotY();
    
    public float getRotZ();
    
    public void incrementRotX(float x);

    public void incrementRotY(float y);

    public void incrementRotZ(float z);
    
    public void setRotXYZ(Vector3f xyz);
    
    public Vector3f getRotXYZ();
}