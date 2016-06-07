package testing.lwjgl.event.handler;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import logger.Log;
import testing.lwjgl.event.EventSubscription;
import testing.lwjgl.event.events.EventCursorEnterWindow;
import testing.lwjgl.event.events.EventCursorMove;
import testing.lwjgl.event.events.EventKeyInput;
import testing.lwjgl.reference.Game;
import testing.lwjgl.util.Debug;

public class CameraMovementHandler
{
    private boolean  m_isInWindow;
    private Vector2f m_previousPos;

    private boolean XPress = false;
    private boolean YPress = false;
    private boolean ZPress = false;
    
    public CameraMovementHandler()
    {
        m_isInWindow = true;
        m_previousPos = new Vector2f(-1.0f, -1.0f);
    }

    @EventSubscription
    public void enterWindow(EventCursorEnterWindow event) { m_isInWindow = event.hasEntered(); }

    @EventSubscription(priority = 1)
    public void keyPress(EventKeyInput event)
    {
        int key = event.getKey();
        int action = event.getAction();
        
        if(action == GLFW.GLFW_PRESS && (key == GLFW.GLFW_KEY_W || key == GLFW.GLFW_KEY_S)) { ZPress = true; }
        if(action == GLFW.GLFW_RELEASE && (key == GLFW.GLFW_KEY_W || key == GLFW.GLFW_KEY_S)) { ZPress = false; }

        if(action == GLFW.GLFW_PRESS && (key == GLFW.GLFW_KEY_SPACE || key == GLFW.GLFW_KEY_LEFT_SHIFT || key == GLFW.GLFW_KEY_RIGHT_SHIFT)) { YPress = true; }
        if(action == GLFW.GLFW_RELEASE && (key == GLFW.GLFW_KEY_SPACE || key == GLFW.GLFW_KEY_LEFT_SHIFT || key == GLFW.GLFW_KEY_RIGHT_SHIFT)) { YPress = false; }
        
        if(action == GLFW.GLFW_PRESS && (key == GLFW.GLFW_KEY_A || key == GLFW.GLFW_KEY_D)) { XPress = true; }
        if(action == GLFW.GLFW_RELEASE && (key == GLFW.GLFW_KEY_A || key == GLFW.GLFW_KEY_D)) { XPress = false; }

        if(key == GLFW.GLFW_KEY_W) { Game.CAMERA.moveZ(-Game.CAMERA.getMovementSpeed(), ZPress); }
        else if(key == GLFW.GLFW_KEY_S) { Game.CAMERA.moveZ(Game.CAMERA.getMovementSpeed(), ZPress); }

        if(key == GLFW.GLFW_KEY_A) { Game.CAMERA.moveX(-Game.CAMERA.getMovementSpeed(), XPress); }
        else if(key == GLFW.GLFW_KEY_D) { Game.CAMERA.moveX(Game.CAMERA.getMovementSpeed(), XPress); }

        if(key == GLFW.GLFW_KEY_RIGHT_SHIFT || key == GLFW.GLFW_KEY_LEFT_SHIFT) { Game.CAMERA.moveY(-Game.CAMERA.getMovementSpeed(), YPress); }
        else if(key == GLFW.GLFW_KEY_SPACE) { Game.CAMERA.moveY(Game.CAMERA.getMovementSpeed(), YPress); }

        if(key == GLFW.GLFW_KEY_LEFT_BRACKET && action == GLFW.GLFW_PRESS)
        {
            Game.CAMERA.incrementMovementSpeed(0.05f);
            if(Game.CAMERA.getMovementSpeed() > 10.0f) { Game.CAMERA.setMovementSpeed(10.0f); }
            Log.debug(Game.CAMERA.getMovementSpeed());
        }
        if(key == GLFW.GLFW_KEY_RIGHT_BRACKET && action == GLFW.GLFW_PRESS)
        {
            Game.CAMERA.incrementMovementSpeed(-0.05f);
            if(Game.CAMERA.getMovementSpeed() < 0.0f) { Game.CAMERA.setMovementSpeed(0.0f); }
            Log.debug(Game.CAMERA.getMovementSpeed());
        }
        
        if(key == GLFW.GLFW_KEY_I && action == GLFW.GLFW_PRESS)
        {
            Debug.debugVector3f(Game.CAMERA.getXYZ());
            Debug.debugVector3f(Game.CAMERA.getRotXYZ());
        }
    }

    @EventSubscription(priority = 1)
    public void cursorMove(EventCursorMove event)
    {
        if(m_isInWindow)
        {
            float deltaX = (float) event.getX() - m_previousPos.x;
            float deltaY = (float) event.getY() - m_previousPos.y;
            if(deltaX > 10.0f) { deltaX = 10.0f; }
            if(deltaX < -10.0f) { deltaX = -10.0f; }
            if(deltaY > 10.0f) { deltaY = 10.0f; }
            if(deltaY < -10.0f) { deltaY = -10.0f; }
            if(deltaX != 0) { Game.CAMERA.incrementRotY(deltaX); }
            if(deltaY != 0) { Game.CAMERA.incrementRotX(deltaY); }
        }
        m_previousPos.x = (float) event.getX();
        m_previousPos.y = (float) event.getY();
    }
}