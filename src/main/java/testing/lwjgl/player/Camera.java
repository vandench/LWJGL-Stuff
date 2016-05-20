package testing.lwjgl.player;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import testing.lwjgl.reference.Game;
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
    private Matrix4f       m_projectionMatrix;

    private static final float    INCREMENT = 9.0f;
    private static final float    VIEW_RANGE = INCREMENT * 1.05f;
    private static boolean        MOVE_X = false;
    private static boolean        MOVE_Y = false;
    private static boolean        MOVE_Z = false;
    
    public Camera(Vector3f pos, Vector3f rot)
    {
        m_pos = pos;
        m_rot = rot;
        m_posUpdate = new Vector3f(0.0f, 0.0f, 0.0f);
        m_rotUpdate = new Vector3f(0.0f, 0.0f, 0.0f);
        updateProjectionMatrix();
        Game.CAMERA = this;
    }
    
    public void moveX(float inc, boolean move)
    {
        MOVE_X = move;
        m_posUpdate.x = inc;
    }
    
    public void moveY(float inc, boolean move)
    {
        MOVE_Y = move;
        m_posUpdate.y = inc;
    }
    
    public void moveZ(float inc, boolean move)
    {
        MOVE_Z = move;
        m_posUpdate.z = inc;
    }
    
    public void update()
    {
        /**
         * Move along X axis while taking current rotation into account
         */
        if(m_posUpdate.x != 0)
        {
            m_pos.x += (float) Math.sin(Math.toRadians(m_rot.y - 90)) * -1.0f * m_posUpdate.x;
            m_pos.z += (float) Math.cos(Math.toRadians(m_rot.y - 90)) * m_posUpdate.x;
        }

        /**
         * Move along Z axis while taking current rotation into account
         */
        if(m_posUpdate.z != 0)
        {
            m_pos.x += (float) Math.sin(Math.toRadians(m_rot.y)) * -1.0f * m_posUpdate.z;
            m_pos.z += (float) Math.cos(Math.toRadians(m_rot.y)) * m_posUpdate.z;
        }
        
        /**
         * Move along Y axis
         */
        m_pos.y += m_posUpdate.y;
        
        /**
         * Gradually slow down
         */
        if(m_posUpdate.x < 0.0f && !MOVE_X) { m_posUpdate.x += 0.003f; } else if(m_posUpdate.x > 0.0f && !MOVE_X) { m_posUpdate.x -= 0.003f; }
        if(m_posUpdate.y < 0.0f && !MOVE_Y) { m_posUpdate.y += 0.003f; } else if(m_posUpdate.y > 0.0f && !MOVE_Y) { m_posUpdate.y -= 0.003f; }
        if(m_posUpdate.z < 0.0f && !MOVE_Z) { m_posUpdate.z += 0.003f; } else if(m_posUpdate.z > 0.0f && !MOVE_Z) { m_posUpdate.z -= 0.003f; }
        if(m_posUpdate.x > -0.03f && m_posUpdate.x < 0.03f) { m_posUpdate.x = 0.0f; }
        if(m_posUpdate.y > -0.03f && m_posUpdate.y < 0.03f) { m_posUpdate.y = 0.0f; }
        if(m_posUpdate.z > -0.03f && m_posUpdate.z < 0.03f) { m_posUpdate.z = 0.0f; }
        
        /**
         * Turn view
         */
        m_rot.x += m_rotUpdate.x;
        m_rot.y += m_rotUpdate.y;
        
        /**
         * limit the amount of degrees Up and Down the camera can turn
         */
        if(m_rot.x < -85.0f) { m_rot.x = -85.0f; }
        if(m_rot.x > 85.0f) { m_rot.x = 85.0f; }
        /**
         * Ensures the Left/Right views are always within the bounds of 0 - 360 (will not be noticeable from a players perspective)
         */
        m_rot.y %= 360;
        
        /**
         * Gradually slow down
         */
        if(m_rotUpdate.x < 0.0f) { m_rotUpdate.x += INCREMENT; } else if(m_rotUpdate.x > 0.0f) { m_rotUpdate.x -= INCREMENT; }
        if(m_rotUpdate.y < 0.0f) { m_rotUpdate.y += INCREMENT; } else if(m_rotUpdate.y > 0.0f) { m_rotUpdate.y -= INCREMENT; }
        if(m_rotUpdate.x >= -VIEW_RANGE && m_rotUpdate.x <= VIEW_RANGE) { m_rotUpdate.x = 0.0f; }
        if(m_rotUpdate.y >= -VIEW_RANGE && m_rotUpdate.y <= VIEW_RANGE) { m_rotUpdate.y = 0.0f; }
    }

    public Matrix4f updateViewMatrix()
    {
        Matrix4f viewMatrix = new Matrix4f().identity();
        viewMatrix.rotate((float) Math.toRadians(m_rot.x), new Vector3f(1.0f, 0.0f, 0.0f)).rotate((float) Math.toRadians(m_rot.y), new Vector3f(0.0f, 1.0f, 0.0f)).translate(-m_pos.x, -m_pos.y, -m_pos.z);
        return viewMatrix;
    }
    
    public void updateProjectionMatrix() { m_projectionMatrix = new Matrix4f().identity().perspective((float) Math.toRadians(Game.FOV), (float) Game.WIDTH / (float) Game.HEIGHT, 0.01f, Game.VIEW_DISTANCE); }

    public void render() { Game.SHADER.setUniform("projectionMatrix", m_projectionMatrix); }

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
    public void incrementX(float x) { m_posUpdate.x = x; }

    @Override
    public void incrementY(float y) { m_posUpdate.y = y; }

    @Override
    public void incrementZ(float z) { m_posUpdate.z = z; }

    @Override
    public void setXYZ(Vector3f xyz)
    {
        m_pos.x = xyz.x;
        m_pos.y = xyz.y;
        m_pos.z = xyz.z;
    }

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
    public void incrementRotX(float x) { m_rotUpdate.x += x; }

    @Override
    public void incrementRotY(float y) { m_rotUpdate.y += y; }

    @Override
    public void incrementRotZ(float z) { m_rotUpdate.z += z; }

    @Override
    public void setRotXYZ(Vector3f xyz)
    {
        m_rot.x = xyz.x;
        m_rot.y = xyz.y;
        m_rot.z = xyz.z;
    }

    @Override
    public Vector3f getRotXYZ() { return m_rot; }
}