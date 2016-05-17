package testing.lwjgl.renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import logger.Log;
import testing.lwjgl.cleanup.CleanUpHandler;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.resources.ResourceHelper;

public class Texture implements ICleanUpAble
{
    private int m_texID;
    
    public Texture(int texID)
    {
        m_texID = texID;
        CleanUpHandler.getInstance().addCleanUpAble(this);
    }
    
    public Texture(String resource, boolean smooth)
    {
        this(loadTexture(resource, smooth));
    }
    
    public Texture(String resource)
    {
        this(resource, true);
    }
    
    public int getTexID()
    {
        return m_texID;
    }

    @Override
    public void cleanUp()
    {
        GL11.glDeleteTextures(m_texID);
    }
    
    public static int loadTexture(String resource, boolean smooth)
    {
        ByteBuffer image = ResourceHelper.getBufferedResource(resource);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);

        if(STBImage.stbi_info_from_memory(image, width, height, components) == 0)
        {
            Log.trace(new RuntimeException("Failed to read image info: " + STBImage.stbi_failure_reason()));
        }

        ByteBuffer buffer = STBImage.stbi_load_from_memory(image, width, height, components, 0);
        if(buffer == null)
        {
            Log.trace(new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason()));
        }

        int texID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
//        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 2 - (width.get(0) & 1));
        
        if(smooth)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        } else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        }
            
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width.get(0), height.get(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        return texID;
    }
}
