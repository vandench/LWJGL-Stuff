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
import testing.lwjgl.Game;
import testing.lwjgl.reference.Properties;
import testing.lwjgl.reference.Properties.GAME;

public class ResourceHelper
{
    private static ResourceHelper instance = new ResourceHelper();

    public static String getGameDataDir()
    {
        String fSep = Properties.OS.FILE_SEPERATOR;
        String path = Properties.OS.USER_HOME + fSep + "Documents" + fSep + "My Games";
        if(Game.getInstance().isDevEnvironment())
        {
            path = Properties.OS.USER_DIR + fSep + "My Games";
        }
        File f = new File(path);
        if(!(f.exists() && f.isDirectory()))
        {
            f.mkdir();
        }
        path += fSep + "Test0x00";
        f = new File(path);
        if(!(f.exists() && f.isDirectory()))
        {
            f.mkdir();
        }
        return path;
    }

    public static InputStream getResource(String resource)
    {
        if(Properties.JAVA.IS_JAR)
        {
            return instance.getClass().getResourceAsStream(resource);
        } else
        {
            try
            {
                return new FileInputStream(new File(Properties.OS.USER_DIR + "/src/main/resources/" + resource));
            } catch(FileNotFoundException e)
            {
                Log.trace(e);
            }
        }
        return null;
    }

    public static String getResourceAsString(String resource)
    {
        Scanner scanner = new Scanner(getResource(resource), "UTF-8");
        String out = scanner.useDelimiter("\\A").next();
        scanner.close();
        return out;
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
        } catch(IOException e)
        {
            Log.trace(e);
        }
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer getBufferedResource(String resource)
    {
        return getBufferedResource(getResource(resource));
    }
}