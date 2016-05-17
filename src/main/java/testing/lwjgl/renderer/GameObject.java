package testing.lwjgl.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import testing.lwjgl.util.axis.ITransformable;

public class GameObject implements ITransformable
{
    private final Model m_model;
    private Vector3f    m_pos;
    private Vector3f    m_rot;
    private float       m_scale;

    public GameObject(Model model)
    {
        this(model, new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1.0f);
    }

    public GameObject(Model model, Vector3f position, Vector3f rotation, float scale)
    {
        m_model = model;
        m_pos = position;
        m_rot = rotation;
        m_scale = scale;
    }

    public Matrix4f updateObjectViewMatrix()
    {
        Matrix4f modelViewMatrix = new Matrix4f().identity().translate(m_pos).rotateX((float) Math.toRadians(-m_rot.x)).rotateY((float) Math.toRadians(-m_rot.y)).rotateZ((float) Math.toRadians(-m_rot.z)).scale(m_scale);
        return modelViewMatrix;
    }

    public Model getModel()
    {
        return m_model;
    }

    @Override
    public void setX(float x)
    {
        m_pos.x = x;
    }

    @Override
    public void setY(float y)
    {
        m_pos.y = y;
    }

    @Override
    public void setZ(float z)
    {
        m_pos.z = z;
    }

    @Override
    public float getX()
    {
        return m_pos.x;
    }

    @Override
    public float getY()
    {
        return m_pos.y;
    }

    @Override
    public float getZ()
    {
        return m_pos.z;
    }

    @Override
    public void incrementX(float x)
    {
        m_pos.x += x;
    }

    @Override
    public void incrementY(float y)
    {
        m_pos.y += y;
    }

    @Override
    public void incrementZ(float z)
    {
        m_pos.z += z;
    }

    @Override
    public void setXYZ(Vector3f xyz)
    {
        m_pos = xyz;
    }

    @Override
    public Vector3f getXYZ()
    {
        return m_pos;
    }

    @Override
    public void setRotX(float x)
    {
        m_rot.x = x;
    }

    @Override
    public void setRotY(float y)
    {
        m_rot.y = y;
    }

    @Override
    public void setRotZ(float z)
    {
        m_rot.z = z;
    }

    @Override
    public float getRotX()
    {
        return m_rot.x;
    }

    @Override
    public float getRotY()
    {
        return m_rot.y;
    }

    @Override
    public float getRotZ()
    {
        return m_rot.z;
    }

    @Override
    public void incrementRotX(float x)
    {
        m_rot.x += x;
    }

    @Override
    public void incrementRotY(float y)
    {
        m_rot.y += y;
    }

    @Override
    public void incrementRotZ(float z)
    {
        m_rot.z += z;
    }

    @Override
    public void setRotXYZ(Vector3f xyz)
    {
        m_rot = xyz;
    }

    @Override
    public Vector3f getRotXYZ()
    {
        return m_rot;
    }

    @Override
    public void setScaleX(float x)
    {
        m_scale = x;
    }

    @Override
    public void setScaleY(float y)
    {
        m_scale = y;
    }

    @Override
    public void setScaleZ(float z)
    {
        m_scale = z;
    }

    @Override
    public float getScaleX()
    {
        return m_scale;
    }

    @Override
    public float getScaleY()
    {
        return m_scale;
    }

    @Override
    public float getScaleZ()
    {
        return m_scale;
    }

    @Override
    public void incrementScaleX(float x)
    {
        m_scale += x;
    }

    @Override
    public void incrementScaleY(float y)
    {
        m_scale += y;
    }

    @Override
    public void incrementScaleZ(float z)
    {
        m_scale += z;
    }

    @Override
    public void setScaleXYZ(Vector3f xyz)
    {
        m_scale = (xyz.x + xyz.y + xyz.z) / 3.0f;
    }

    @Override
    public Vector3f getScaleXYZ()
    {
        return new Vector3f(m_scale, m_scale, m_scale);
    }
}