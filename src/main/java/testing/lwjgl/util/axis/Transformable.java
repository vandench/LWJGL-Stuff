package testing.lwjgl.util.axis;

import org.joml.Vector3f;

public class Transformable implements ITransformable
{
    private Vector3f m_pos;
    private Vector3f m_rot;
    private Vector3f m_scale;
    
    public Transformable(Vector3f pos, Vector3f rot, Vector3f scale)
    {
        m_pos = pos;
        m_rot = rot;
        m_scale = scale;
    }
    
    public Transformable(Vector3f pos, Vector3f rot) { this(pos, rot, new Vector3f(0.0f, 0.0f, 0.0f)); }
    
    public Transformable(Vector3f pos) { this(pos, new Vector3f(0.0f, 0.0f, 0.0f)); }
    
    public Transformable() { this(new Vector3f(0.0f, 0.0f, 0.0f)); }

    @Override
    public void setX(float x) { m_pos.x = x; }

    @Override
    public void setY(float y) { m_pos.y = y; }

    @Override
    public void setZ(float z) { m_pos.z = z; }

    @Override
    public float getX() { return m_pos.x; }

    @Override
    public float getY() { return m_pos.y; }

    @Override
    public float getZ() { return m_pos.z; }

    @Override
    public void incrementX(float x) { m_pos.x += x; }

    @Override
    public void incrementY(float y) { m_pos.y += y; }

    @Override
    public void incrementZ(float z) { m_pos.z += z; }

    @Override
    public void setXYZ(Vector3f xyz) { m_pos = xyz; }

    @Override
    public Vector3f getXYZ() { return m_pos; }

    @Override
    public void setRotX(float x) { m_rot.x = x; }

    @Override
    public void setRotY(float y) { m_rot.y = y; }

    @Override
    public void setRotZ(float z) { m_rot.z = z; }

    @Override
    public float getRotX() { return m_rot.x; }

    @Override
    public float getRotY() { return m_rot.y; }

    @Override
    public float getRotZ() { return m_rot.z; }

    @Override
    public void incrementRotX(float x) { m_rot.x += x; }

    @Override
    public void incrementRotY(float y) { m_rot.y += y; }

    @Override
    public void incrementRotZ(float z) { m_rot.z += z; }

    @Override
    public void setRotXYZ(Vector3f xyz) { m_rot = xyz; }

    @Override
    public Vector3f getRotXYZ() { return m_rot; }

    @Override
    public void setScaleX(float x) { m_scale.x = x; }

    @Override
    public void setScaleY(float y) { m_scale.y = y; }

    @Override
    public void setScaleZ(float z) { m_scale.z = z; }

    @Override
    public float getScaleX() { return m_scale.x; }

    @Override
    public float getScaleY() { return m_scale.y; }

    @Override
    public float getScaleZ() { return m_scale.z; }

    @Override
    public void incrementScaleX(float x) { m_scale.x = x; }

    @Override
    public void incrementScaleY(float y) { m_scale.y += y; }

    @Override
    public void incrementScaleZ(float z) { m_scale.z += z; }

    @Override
    public void setScaleXYZ(Vector3f xyz) { m_scale = xyz; }

    @Override
    public Vector3f getScaleXYZ() { return m_scale; }
}