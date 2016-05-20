package testing.lwjgl.renderer;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.model.GameObject;
import testing.lwjgl.reference.Game;

public class Scene implements ICleanUpAble
{
    private final List<GameObject> m_worldObjects;

    public Scene()
    {
        CleanUpHandler.addCleanUpAble(this);
        m_worldObjects = new ArrayList<GameObject>();
//        Game.SCENE = this;
    }
    
    public void render()
    {
        Matrix4f viewMatrix = Game.CAMERA.updateViewMatrix();
        
        for(GameObject obj : m_worldObjects)
        {
            Game.SHADER.setUniform("modelViewMatrix", new Matrix4f(viewMatrix).mul(obj.updateObjectViewMatrix()));
            Game.SHADER.setUniform("material", obj.getModel().getMaterial());
            obj.getModel().render();
        }
    }
    
    public void add(GameObject obj)
    {
        m_worldObjects.add(obj);
    }
    
    public GameObject get(int index)
    {
        return m_worldObjects.get(index);
    }

    /**
     * Sets the {@link GameObject} at index to the {@link GameObject} at the
     * last index, which is then removed. Is more efficient because it does not
     * have shift all of the {@link GameObject}'s to the left one.
     */
    public void remove(int index)
    {
        if(m_worldObjects.size() == index)
        {
            m_worldObjects.remove(index);
            return;
        }
        m_worldObjects.set(index, m_worldObjects.remove(m_worldObjects.size() - 1));
    }
    
    public List<GameObject> getWorldObjects()
    {
        return m_worldObjects;
    }
    
    @Override
    public void cleanUp()
    {
        m_worldObjects.clear();
    }
}