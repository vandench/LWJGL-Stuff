package testing.lwjgl.player;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import logger.Log;
import testing.lwjgl.reference.Properties;
import testing.lwjgl.shader.Shader;
import testing.lwjgl.util.axis.IRotatable;
import testing.lwjgl.util.axis.ITranslatable;

public class Camera implements ITranslatable, IRotatable
{
    private final Vector3f m_pos;
    private final Vector3f m_rot;
    private final Vector3f m_posUpdate;
    private final Vector3f m_rotUpdate;

    private final float m_inc = 9.0f;
    private final float m_ran = m_inc  * 1.05f;
    private boolean m_x = false, m_y = false, m_z = false;
    
    private Matrix4f       m_projectionMatrix;

    public Camera(Vector3f pos, Vector3f rot)
    {
        m_pos = pos;
        m_rot = rot;
        m_posUpdate = new Vector3f(0.0f, 0.0f, 0.0f);
        m_rotUpdate = new Vector3f(0.0f, 0.0f, 0.0f);
        updateProjectionMatrix();
    }

    public Camera(float x, float y, float z)
    {
        this(new Vector3f(x, y, z), new Vector3f(0.0f, 0.0f, 0.0f));
    }

    public Camera()
    {
        this(0.0f, 0.0f, 0.0f);
    }

    public Matrix4f updateViewMatrix()
    {
        Matrix4f viewMatrix = new Matrix4f().identity();
        viewMatrix.rotate((float) Math.toRadians(m_rot.x), new Vector3f(1.0f, 0.0f, 0.0f)).rotate((float) Math.toRadians(m_rot.y), new Vector3f(0.0f, 1.0f, 0.0f));
        viewMatrix.translate(-m_pos.x, -m_pos.y, -m_pos.z);
        return viewMatrix;
    }
    
    public void updateProjectionMatrix()
    {
        m_projectionMatrix = new Matrix4f().identity().perspective((float) Math.toRadians(Properties.GAME.FOV), (float) Properties.GAME.WIDTH / (float) Properties.GAME.HEIGHT, 0.01f, Properties.GAME.VIEW_DISTANCE);
    }
    
    public void moveX(float inc, boolean move)
    {
        m_x = move;
        m_posUpdate.x = inc;
    }
    
    public void moveY(float inc, boolean move)
    {
        m_y = move;
        m_posUpdate.y = inc;
    }
    
    public void moveZ(float inc, boolean move)
    {
        m_z = move;
        m_posUpdate.z = inc;
    }
    
    public void update()
    {
        if(m_posUpdate.x != 0)
        {
            m_pos.x += (float) Math.sin(Math.toRadians(m_rot.y - 90)) * -1.0f * m_posUpdate.x;
            m_pos.z += (float) Math.cos(Math.toRadians(m_rot.y - 90)) * m_posUpdate.x;
        }
        
        if(m_posUpdate.z != 0)
        {
            m_pos.x += (float) Math.sin(Math.toRadians(m_rot.y)) * -1.0f * m_posUpdate.z;
            m_pos.z += (float) Math.cos(Math.toRadians(m_rot.y)) * m_posUpdate.z;
        }
        
        m_pos.y += m_posUpdate.y;
        
        if(m_posUpdate.x < 0.0f && !m_x) { m_posUpdate.x += 0.003f; } else if(m_posUpdate.x > 0.0f && !m_x) { m_posUpdate.x -= 0.003f; }
        if(m_posUpdate.y < 0.0f && !m_y) { m_posUpdate.y += 0.003f; } else if(m_posUpdate.y > 0.0f && !m_y) { m_posUpdate.y -= 0.003f; }
        if(m_posUpdate.z < 0.0f && !m_z) { m_posUpdate.z += 0.003f; } else if(m_posUpdate.z > 0.0f && !m_z) { m_posUpdate.z -= 0.003f; }
        if(m_posUpdate.x > -0.03f && m_posUpdate.x < 0.03f) { m_posUpdate.x = 0.0f; }
        if(m_posUpdate.y > -0.03f && m_posUpdate.y < 0.03f) { m_posUpdate.y = 0.0f; }
        if(m_posUpdate.z > -0.03f && m_posUpdate.z < 0.03f) { m_posUpdate.z = 0.0f; }
        
        m_rot.x += m_rotUpdate.x;
        m_rot.y += m_rotUpdate.y;
        
        if(m_rot.x < -85.0f) { m_rot.x = -85.0f; }
        if(m_rot.x > 85.0f) { m_rot.x = 85.0f; }
        m_rot.y %= 360;
        
        if(m_rotUpdate.x < 0.0f) { m_rotUpdate.x += m_inc; } else if(m_rotUpdate.x > 0.0f) { m_rotUpdate.x -= m_inc; }
        if(m_rotUpdate.y < 0.0f) { m_rotUpdate.y += m_inc; } else if(m_rotUpdate.y > 0.0f) { m_rotUpdate.y -= m_inc; }
        if(m_rotUpdate.x >= -m_ran && m_rotUpdate.x <= m_ran) { m_rotUpdate.x = 0.0f; }
        if(m_rotUpdate.y >= -m_ran && m_rotUpdate.y <= m_ran) { m_rotUpdate.y = 0.0f; }
    }

    public void render(Shader shader)
    {
        shader.setUniform("projectionMatrix", m_projectionMatrix);
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
        m_posUpdate.x = x;
    }

    @Override
    public void incrementY(float y)
    {
        m_posUpdate.y = y;
    }

    @Override
    public void incrementZ(float z)
    {
        m_posUpdate.z = z;
    }

    @Override
    public void setXYZ(Vector3f xyz)
    {
        m_pos.x = xyz.x;
        m_pos.y = xyz.y;
        m_pos.z = xyz.z;
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
        m_rotUpdate.x += x;
    }

    @Override
    public void incrementRotY(float y)
    {
        m_rotUpdate.y += y;
    }

    @Override
    public void incrementRotZ(float z)
    {
        m_rotUpdate.z += z;
    }

    @Override
    public void setRotXYZ(Vector3f xyz)
    {
        m_rot.x = xyz.x;
        m_rot.y = xyz.y;
        m_rot.z = xyz.z;
    }

    @Override
    public Vector3f getRotXYZ()
    {
        return m_rot;
    }
}