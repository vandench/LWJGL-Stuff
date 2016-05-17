package testing.lwjgl.config;

import java.io.IOException;

import testing.lwjgl.reference.Properties;
import testing.lwjgl.resources.ResourceHelper;

public class Config
{
    Configuration config = new Configuration(ResourceHelper.getGameDataDir() + Properties.OS.FILE_SEPERATOR + "settings.cfg");

    public void init()
    {
        if(config.getConfigFile().exists())
        {
            try
            {
                config.readConfig();
            } catch(IOException e)
            {
                e.printStackTrace();
            }
        } else
        {
            config.create();
            config.addSetting("Sets the maximum FPS of the game.", "FPS", 60);
            config.addSetting("Enables VSync.", "VSync", false);
            config.addSetting("Anit-Aliasing modes: NONE, MSAA, SSAA, MLAA, FXAA, SMAA", "AA", "NONE");
            config.addSetting("Sets the resolution of the screen int the format of: WIDTHxHEIGTH", "Res", "720x480");
            config.addSetting("Sets whether or not the window should be in fullscreen mode.", "Fullscreeen", false);
            config.addSetting("Sets the Field Of View.", "FOV", 60.0f);
            config.addSetting("Sets the view distance.", "ViewDist", 1000.0f);
            config.writeSettings();
        }
        Properties.GAME.FPS = (int) config.getSetting("FPS").getValue();
        Properties.GAME.VSYNC = (boolean) config.getSetting("VSync").getValue();
        Properties.GAME.AA = (String) config.getSetting("AA").getValue();
        Properties.GAME.RESOULUTION = (String) config.getSetting("Res").getValue();
        String[] tmp = Properties.GAME.RESOULUTION.split("x");
        Properties.GAME.WIDTH = Integer.parseInt(tmp[0]);
        Properties.GAME.HEIGHT = Integer.parseInt(tmp[1]);
        Properties.GAME.FULLSCREEN = (boolean) config.getSetting("Fullscreeen").getValue();
        Properties.GAME.FOV = (float) config.getSetting("FOV").getValue();
        Properties.GAME.VIEW_DISTANCE = (float) config.getSetting("ViewDist").getValue();
    }
}