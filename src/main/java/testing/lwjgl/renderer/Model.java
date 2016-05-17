package testing.lwjgl.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;

public class Model implements ICleanUpAble
{
    protected Vector3f m_color;
    protected final int m_vertexCount;
    protected int m_texID;
    protected final List<Integer> m_vbos;
    protected final int m_vaoID;
    
    protected Model(int vertexCount, int texID)
    {
        CleanUpHandler.getInstance().addCleanUpAble(this);
        m_color = new Vector3f(1.0f, 1.0f, 1.0f);
        m_vertexCount = vertexCount;
        m_vbos = new ArrayList<Integer>();
        m_vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(m_vaoID);
    }
    
    public Model(float[] positions, float[] textures, float[] normals, int[] indices)
    {
        CleanUpHandler.getInstance().addCleanUpAble(this);
        m_color = new Vector3f(1.0f, 1.0f, 1.0f);
        m_vertexCount = indices.length;
        m_vbos = new ArrayList<Integer>();
        
        m_vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(m_vaoID);
        
        int vboID = GL15.glGenBuffers();
        m_vbos.add(vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
        buffer.put(positions).flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        
        if(textures != null)
        {
            vboID = GL15.glGenBuffers();
            m_vbos.add(vboID);
            buffer = BufferUtils.createFloatBuffer(textures.length);
            buffer.put(textures).flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        }
        
        vboID = GL15.glGenBuffers();
        m_vbos.add(vboID);
        buffer = BufferUtils.createFloatBuffer(normals.length);
        buffer.put(normals).flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        m_vbos.add(vboID);
        IntBuffer iBuffer = BufferUtils.createIntBuffer(indices.length);
        iBuffer.put(indices).flip();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
    public int getVaoID()
    {
        return m_vaoID;
    }

    public int getVertexCount()
    {
        return m_vertexCount;
    }
    
    public Model setTexID(int texID)
    {
        m_texID = texID;
        return this;
    }
    
    public int getTexID()
    {
        return m_texID;
    }
    
    public boolean isTextured()
    {
        return m_texID != MemoryUtil.NULL;
    }
    
    public Model setColor(Vector3f color)
    {
        m_color = color;
        return this;
    }
    
    public Vector3f getColor()
    {
        return m_color;
    }
    
    public List<Integer> getVbos()
    {
        return m_vbos;
    }
    
    public void render()
    {
        if(isTextured())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_texID);
        }
        
        GL30.glBindVertexArray(m_vaoID);
        GL20.glEnableVertexAttribArray(0);
        if(isTextured()) { GL20.glEnableVertexAttribArray(1); }
        GL20.glEnableVertexAttribArray(2);

        GL11.glDrawElements(GL11.GL_TRIANGLES, m_vertexCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        if(isTextured()) { GL20.glDisableVertexAttribArray(1); }
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void cleanUp()
    {
        GL20.glDisableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        for(int vbo : m_vbos)
        {
           GL15.glDeleteBuffers(vbo); 
        }
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(m_vaoID);
    }
}