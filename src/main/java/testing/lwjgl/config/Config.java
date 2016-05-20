package testing.lwjgl.config;

import java.io.IOException;

import testing.lwjgl.reference.Game;
import testing.lwjgl.reference.Properties;
import testing.lwjgl.resources.Path;
import testing.lwjgl.resources.ResourceHelper;

public class Config
{
    Configuration config = new Configuration(ResourceHelper.getGameDataDir() + "/settings.cfg");

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
            config.addSetting("Anti-Aliasing modes: NONE, MSAA, SSAA, MLAA, FXAA, SMAA", "AA", "NONE");
            config.addSetting("Sets the resolution of the screen int the format of: WIDTHxHEIGTH", "Res", "720x480");
            config.addSetting("Sets whether or not the window should be in fullscreen mode.", "Fullscreeen", false);
            config.addSetting("Sets the Field Of View.", "FOV", 60.0f);
            config.addSetting("Sets the view distance.", "ViewDist", 1000.0f);
            config.writeSettings();
        }
        Game.FPS = (int) config.getSetting("FPS").getValue();
        Game.VSYNC = (boolean) config.getSetting("VSync").getValue();
        Game.AA = (String) config.getSetting("AA").getValue();
        Game.RESOULUTION = (String) config.getSetting("Res").getValue();
        String[] tmp = Game.RESOULUTION.split("x");
        Game.WIDTH = Integer.parseInt(tmp[0]);
        Game.HEIGHT = Integer.parseInt(tmp[1]);
        Game.FULLSCREEN = (boolean) config.getSetting("Fullscreeen").getValue();
        Game.FOV = (float) config.getSetting("FOV").getValue();
        Game.VIEW_DISTANCE = (float) config.getSetting("ViewDist").getValue();
    }
}