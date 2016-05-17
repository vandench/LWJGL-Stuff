package testing.util;

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

import testing.lwjgl.Game;
import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.reference.Properties;
import testing.lwjgl.renderer.Model;

public class TextureDebugger extends Model
{
    private static final float[] vertices = new float[] { -0.5f, 0.5f, 0.0f, 
                                                          -0.5f, -0.5f, 0.0f, 
                                                          0.5f, -0.5f, 0.0f, 
                                                          0.5f,  0.5f, 0.0f };
    private static final float[] textCoords = new float[] { 0.0f, 1.0f, 
                                                            0.0f, 0.0f, 
                                                            1.0f, 0.0f,
                                                            1.0f, 1.0f };
    private static final int[] indices = new int[] { 0, 1, 3, 3, 1, 2 };
    
    public TextureDebugger(int texID)
    {
        super(indices.length, texID);
        
        int vboID = GL15.glGenBuffers();
        m_vbos.add(vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        vboID = GL15.glGenBuffers();
        m_vbos.add(vboID);
        buffer = BufferUtils.createFloatBuffer(textCoords.length);
        buffer.put(textCoords).flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        
        vboID = GL15.glGenBuffers();
        m_vbos.add(vboID);
        IntBuffer iBuffer = BufferUtils.createIntBuffer(indices.length);
        iBuffer.put(indices).flip();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
    
    @Override
    public void render()
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_texID);
        
        GL30.glBindVertexArray(m_vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawElements(GL11.GL_TRIANGLES, m_vertexCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}