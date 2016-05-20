package testing.lwjgl.resources;

import testing.lwjgl.reference.Properties;

public class Path
{
    public static String toPath(String in)
    {
        if(Properties.OS.FILE_SEPERATOR.equals("/") || Properties.OS.FILE_SEPERATOR.equals("\\")) { return in; }
        in = in.replaceAll("/", Properties.OS.FILE_SEPERATOR);
        in = in.replaceAll("\\", Properties.OS.FILE_SEPERATOR);
        return in;
    }
}