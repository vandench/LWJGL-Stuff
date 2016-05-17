package testing.lwjgl.util.axis;

import org.joml.Vector3f;

public interface ITranslatable
{
    public void setX(float x);

    public void setY(float y);

    public void setZ(float z);

    public float getX();

    public float getY();

    public float getZ();

    public void incrementX(float x);

    public void incrementY(float y);

    public void incrementZ(float z);

    public void setXYZ(Vector3f xyz);

    public Vector3f getXYZ();
}