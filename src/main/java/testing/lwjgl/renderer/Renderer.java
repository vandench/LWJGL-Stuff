package testing.lwjgl.renderer;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.model.GameObject;
import testing.lwjgl.model.Model;
import testing.lwjgl.reference.Game;
import testing.lwjgl.shader.Light;
import testing.lwjgl.shader.Light.PointLight;
import testing.lwjgl.util.MatrixUtil;
import testing.lwjgl.world.Terrain;

public class Renderer implements ICleanUpAble
{
    private final Multimap<Model, GameObject> m_worldObjects;
    private final List<Light> m_lights;
    private final List<Terrain> m_terrain;
    
    public Renderer()
    {
        Game.CLEAN_UP_HANDLER.addCleanUpAble(this);
        m_worldObjects = HashMultimap.create();
        m_lights = new ArrayList<Light>();
        m_terrain = new ArrayList<Terrain>();
        Game.RENDERER = this;
    }
    
    public void render()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Game.SHADER.bind();
        
        Game.CAMERA.render();
        Matrix4f viewMatrix = Game.CAMERA.updateViewMatrix();
        Game.SHADER.setUniform("cameraPos", Game.CAMERA.getXYZ());
        Game.SHADER.setUniform("ambientLight", new Vector3f(0.3f, 0.3f, 0.3f));
        Game.SHADER.setUniform("specularPower", 10.0f);
        for(Light l : m_lights)
        {
            if(l instanceof PointLight)
            {
                PointLight copy = new PointLight((PointLight) l);
                Vector3f lightPos = copy.position;
                Vector4f aux = new Vector4f(lightPos, 1);
                aux.mul(viewMatrix);
                lightPos.x = aux.x;
                lightPos.y = aux.y;
                lightPos.z = aux.z; 
                Game.SHADER.setUniform("pointLight", copy);
            }
        }
        Game.SHADER.setUniform("textureSampler", 0);
        for(Model m : m_worldObjects.keySet())
        {
            Game.SHADER.setUniform("material", m.getMaterial());
            m.prepareRender();
            for(GameObject obj : m_worldObjects.get(m))
            {
                Game.SHADER.setUniform("modelViewMatrix", new Matrix4f(viewMatrix).mul(MatrixUtil.getModelViewMatrix(obj)));
                m.render();
            }
            m.finishRender();
        }
        
        Game.SHADER.unbind();
        GLFW.glfwSwapBuffers(Game.WINDOW_HANDLE);
    }
    
    public void add(GameObject obj) { m_worldObjects.put(obj.getModel(), obj); }
    
    public void add(Light light) { m_lights.add(light); }
    
    public void add(Terrain terrain) { m_terrain.add(terrain); }
    
    public void removeGameObject(GameObject obj) { m_worldObjects.remove(obj.getModel(), obj); }
    
    public void removeLight(int index)
    {
        if(m_lights.size() == index)
        {
            m_lights.remove(index);
            return;
        }
        m_lights.set(index, m_lights.remove(m_lights.size() - 1));
    }
    
    public void removeTerrain(int index)
    {
        if(m_terrain.size() == index)
        {
            m_terrain.remove(index);
            return;
        }
        m_terrain.set(index, m_terrain.remove(m_terrain.size() - 1));
    }
    
    public Multimap<Model, GameObject> getWorldObjects() { return m_worldObjects; }
    
    public List<Light> getLights() { return m_lights; }
    
    public List<Terrain> getTerrain() { return m_terrain; }
    
    @Override
    public void cleanUp()
    {
        m_worldObjects.clear();
        m_lights.clear();
        m_terrain.clear();
    }
}