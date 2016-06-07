package testing.lwjgl.resources;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import org.lwjgl.BufferUtils;

import logger.Log;
import testing.lwjgl.reference.Game;
import utils.properties.Properties;
import utils.input.IO;

public class ResourceHelper
{
    private ResourceHelper() {}
    
    public static String getGameDataDir()
    {
        String path = IO.toPath(Properties.OS.USER_HOME + "/Documents/My Games");
        if(Game.DEV_ENVIRONMENT) { path = IO.toPath(Properties.OS.USER_DIR + "/My Games"); }
        File f = new File(path);
        if(!(f.exists() && f.isDirectory())) { f.mkdir(); }
        path += IO.toPath("/Test0x00");
        f = new File(path);
        if(!(f.exists() && f.isDirectory())) { f.mkdir(); }
        return path;
    }

    public static InputStream getAsset(String resource)
    {
        try { return IO.getResource("/assets" + resource); }
        catch(FileNotFoundException e) { Log.trace(e); }
        return null;
    }
    
    public static InputStream getFont(String resource) { return getAsset("/fonts" + resource); }
    
    public static InputStream getModel(String resource) { return getAsset("/models" + resource); }
    
    public static InputStream getShader(String resource) { return getAsset("/shaders" + resource); }
    
    public static InputStream getTexture(String resource) { return getAsset("/textures" + resource); }
    
    public static String getShaderAsString(String resource)
    {
        try { return IO.getResourceAsString("/assets/shaders" + resource); }
        catch(FileNotFoundException e) { Log.trace(e); }
        return null;
    }

    public static ByteBuffer getBufferedResource(InputStream resource)
    {
        try
        {
            ReadableByteChannel channel = Channels.newChannel(resource);
            ByteBuffer buffer = BufferUtils.createByteBuffer(134217728); // Size of 4096 image with 8 bits of pixel data (less than Integer.MAX_VALUE).
            while(channel.read(buffer) != -1) {}
            resource.close();
            channel.close();
            return (ByteBuffer) buffer.flip();
        } catch(IOException e) { Log.trace(e); }
        return null;
    }

    public static ByteBuffer getBufferedResource(String resource)
    {
        try { return getBufferedResource(IO.getResource(resource)); }
        catch(FileNotFoundException e) { Log.trace(e); }
        return null;
    }
    
    public static ByteBuffer getBufferedTexture(String resource) { return getBufferedResource(getTexture(resource)); }
}