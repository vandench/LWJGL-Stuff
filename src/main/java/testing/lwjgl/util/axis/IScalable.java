package testing.lwjgl.util.axis;

import org.joml.Vector3f;

public interface IScalable
{
    public void setScaleX(float x);
    
    public void setScaleY(float y);
    
    public void setScaleZ(float z);
    
    public float getScaleX();
    
    public float getScaleY();
    
    public float getScaleZ();
    
    public void incrementScaleX(float x);

    public void incrementScaleY(float y);

    public void incrementScaleZ(float z);
    
    public void setScaleXYZ(Vector3f xyz);
    
    public Vector3f getScaleXYZ();
}