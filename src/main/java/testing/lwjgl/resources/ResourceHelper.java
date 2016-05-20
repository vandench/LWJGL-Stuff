package testing.lwjgl.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import org.lwjgl.BufferUtils;

import logger.Log;
import testing.lwjgl.reference.Game;
import testing.lwjgl.reference.Properties;

public class ResourceHelper
{
    private ResourceHelper() {}
    
    public static String getGameDataDir()
    {
        String path = Path.toPath(Properties.OS.USER_HOME + "/Documents/My Games");
        if(Game.DEV_ENVIRONMENT) { path = Path.toPath(Properties.OS.USER_DIR + "/My Games"); }
        File f = new File(path);
        if(!(f.exists() && f.isDirectory())) { f.mkdir(); }
        path += Path.toPath("/Test0x00");
        f = new File(path);
        if(!(f.exists() && f.isDirectory())) { f.mkdir(); }
        return path;
    }

    public static InputStream getResource(String resource)
    {
        resource = Path.toPath(resource);
        if(Properties.JAVA.IS_JAR) { return new ResourceHelper().getClass().getResourceAsStream(resource);
        } else
        {
            try { return new FileInputStream(new File(Path.toPath(Properties.OS.USER_DIR + "/src/main/resources/" + resource)));
            } catch(FileNotFoundException e) { Log.trace(e); }
        }
        return null;
    }

    public static InputStream getAsset(String resource)
    {
        return getResource("assets/" + resource);
    }
    
    public static InputStream getModel(String resource)
    {
        return getAsset("models/" + resource);
    }
    
    public static InputStream getShader(String resource)
    {
        return getAsset("shaders/" + resource);
    }
    
    public static InputStream getTexture(String resource)
    {
        return getAsset("textures/" + resource);
    }
    
    public static String getResourceAsString(String resource)
    {
        resource = Path.toPath(resource);
        Scanner scanner = new Scanner(getResource(resource), "UTF-8");
        String out = scanner.useDelimiter("\\A").next();
        scanner.close();
        return out;
    }
    
    public static String getShaderAsString(String resource)
    {
        return getResourceAsString("assets/shaders/" + resource);
    }

    public static ByteBuffer getBufferedResource(InputStream resource)
    {
        ByteBuffer buffer = null;
        FileChannel channel = ((FileInputStream) resource).getChannel();
        try
        {
            buffer = BufferUtils.createByteBuffer((int) channel.size() + 1);
            while(channel.read(buffer) != -1) {}
            resource.close();
            channel.close();
        } catch(IOException e) { Log.trace(e); }
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer getBufferedResource(String resource)
    {
        return getBufferedResource(getResource(resource));
    }
}