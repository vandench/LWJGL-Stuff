package testing.lwjgl.reference;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import logger.Log;
import testing.lwjgl.renderer.Texture;
import testing.lwjgl.resources.ResourceHelper;

public class Textures
{
    public static final Texture GRASS_BLOCK = new Texture("assets/textures/grassblock.png", false);
    public static final Texture TEXTURE = new Texture("assets/textures/texture.png", false);
    public static final Texture CUBE = new Texture("assets/textures/cube.png", false);
    
    public static final Texture BLACK = new Texture("assets/textures/black.png", false);
    public static final Texture WHITE = new Texture("assets/textures/white.png", false);
    public static final Texture RED = new Texture("assets/textures/red.png", false);
    public static final Texture GREEN = new Texture("assets/textures/green.png", false);
    public static final Texture BLUE = new Texture("assets/textures/blue.png", false);
    public static final Texture RG = new Texture("assets/textures/red_green.png", false);
    public static final Texture RB = new Texture("assets/textures/red_blue.png", false);
    public static final Texture GB = new Texture("assets/textures/green_blue.png", false);
}