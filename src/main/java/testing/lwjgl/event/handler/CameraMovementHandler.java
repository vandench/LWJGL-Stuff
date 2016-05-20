package testing.lwjgl.event.handler;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import logger.Log;
import testing.lwjgl.event.EventSubscription;
import testing.lwjgl.event.events.CursorEnterEvent;
import testing.lwjgl.event.events.CursorMoveEvent;
import testing.lwjgl.event.events.KeyInputEvent;
import testing.lwjgl.player.Camera;
import testing.lwjgl.reference.Game;
import testing.lwjgl.util.Debugging;

public class CameraMovementHandler
{
    private Camera   m_camera;
    private boolean  m_isInWindow;
    private Vector2f m_previousPos;
    
    private float speed = 0.05f;

    public CameraMovementHandler()
    {
        m_camera = Game.CAMERA;
        m_isInWindow = true;
        m_previousPos = new Vector2f(-1.0f, -1.0f);
    }

    @EventSubscription
    public void enterWindow(CursorEnterEvent event)
    {
        m_isInWindow = event.hasEntered();
    }

    @EventSubscription
    public void keyPress(KeyInputEvent event)
    {
        int key = event.getKey();
        int action = event.getAction();
        boolean press = false;
        if(action == GLFW.GLFW_PRESS)
        {
            press = true;
        }
        if(action == GLFW.GLFW_RELEASE)
        {
            press = false;
        }

        if(key == GLFW.GLFW_KEY_W)
        {
            m_camera.moveZ(-speed, press);
        } else if(key == GLFW.GLFW_KEY_S)
        {
            m_camera.moveZ(speed, press);
        }

        if(key == GLFW.GLFW_KEY_A)
        {
            m_camera.moveX(-speed, press);
        } else if(key == GLFW.GLFW_KEY_D)
        {
            m_camera.moveX(speed, press);
        }

        if(key == GLFW.GLFW_KEY_RIGHT_SHIFT || key == GLFW.GLFW_KEY_LEFT_SHIFT)
        {
            m_camera.moveY(-speed, press);
        } else if(key == GLFW.GLFW_KEY_SPACE)
        {
            m_camera.moveY(speed, press);
        }

        if(key == GLFW.GLFW_KEY_LEFT_BRACKET && action == GLFW.GLFW_PRESS)
        {
            speed += 0.05f;
            if(speed > 10.0f) { speed = 10.0f; }
            Log.debug(speed);
        }
        if(key == GLFW.GLFW_KEY_RIGHT_BRACKET && action == GLFW.GLFW_PRESS)
        {
            speed -= 0.05f;
            if(speed < 0.0f) { speed = 0.0f; }
            Log.debug(speed);
        }
        
        if(key == GLFW.GLFW_KEY_I && action == GLFW.GLFW_PRESS)
        {
            Debugging.debugVector3f(m_camera.getXYZ());
            Debugging.debugVector3f(m_camera.getRotXYZ());
        }
    }

    @EventSubscription
    public void cursorMove(CursorMoveEvent event)
    {
        if(m_isInWindow)
        {
            float deltaX = (float) event.getX() - m_previousPos.x;
            float deltaY = (float) event.getY() - m_previousPos.y;
            if(deltaX > 10.0f) { deltaX = 10.0f; }
            if(deltaX < -10.0f) { deltaX = -10.0f; }
            if(deltaY > 10.0f) { deltaY = 10.0f; }
            if(deltaY < -10.0f) { deltaY = -10.0f; }
            if(deltaX != 0)
            {
                m_camera.incrementRotY(deltaX);
            }
            if(deltaY != 0)
            {
                m_camera.incrementRotX(deltaY);
            }
        }
        m_previousPos.x = (float) event.getX();
        m_previousPos.y = (float) event.getY();
    }
}