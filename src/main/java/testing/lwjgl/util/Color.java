package testing.lwjgl.util;

import org.joml.Vector3f;

public class Color
{
    public static Vector3f BLACK = new Vector3f(0.0f, 0.0f, 0.0f);
    public static Vector3f WHITE = new Vector3f(1.0f, 1.0f, 1.0f);
    public static Vector3f RED = new Vector3f(1.0f, 0.0f, 0.0f);
    public static Vector3f GREEN = new Vector3f(0.0f, 1.0f, 0.0f);
    public static Vector3f BLUE = new Vector3f(0.0f, 0.0f, 1.0f);
    public static Vector3f YELLOW = new Vector3f(1.0f, 1.0f, 0.0f);
    public static Vector3f PURPLE = new Vector3f(1.0f, 0.0f, 1.0f);
    public static Vector3f CYAN = new Vector3f(0.0f, 1.0f, 1.0f);
    
    /**
     * Converts three int's (0 - 255) to a {@link org.joml.Vector3f} (values between 0.0f and 1.0f)
     */
    public static Vector3f toVec3f(int r, int g, int b)
    {
        return new Vector3f(((float) r) / 255.0f, ((float) g) / 255.0f, ((float) b) / 255.0f);
    }
    
    /**
     * Converts hex into an int[] of rgb values
     */
    public static int[] toRGB(int hex)
    {
        int[] rgb = new int[3];
        rgb[0] = (hex & 0xff0000) >> 16;
        rgb[1] = (hex & 0xff00) >> 8;
        rgb[2] = hex & 0xff;
        return rgb;
    }
    
    /**
     * Converts a {@link org.joml.Vector3f} (values between 0.0f and 1.0f) to an int[]
     */
    public static int[] toRGB(Vector3f rgb)
    {
        return new int[] { (int) (rgb.x * 255.0f), (int) (rgb.y * 255.0f), (int) (rgb.z * 255.0f) };
    }
    
    /**
     * Converts RGB values into hex
     */
    public static int toHex(int r, int g, int b)
    {
        r <<= 16;
        g <<= 8;
        return (r | g) | b;
    }
    
    /**
     * Converts an int[] into hex
     */
    public static int toHex(int[] rgb)
    {
        return toHex(rgb[0], rgb[1], rgb[2]);
    }
    
    /**
     * Converts a {@link org.joml.Vector3f} (values between 0.0f and 1.0f) to hex
     */
    public static int toHex(Vector3f rgb)
    {
        return toHex(toRGB(rgb));
    }
    
    /**
     * Inverses RGB values
     */
    public static int[] inverse(int r, int g, int b)
    {
        int[] rgb = new int[3];
        rgb[0] = 255 - r;
        rgb[1] = 255 - g;
        rgb[2] = 255 - b;
        return rgb;
    }
    
    /**
     * Inverses {@link org.joml.Vector3f} (values between 0.0f and 1.0f) RGB values
     */
    public static int[] inverse(Vector3f rgb)
    {
        return inverse(toRGB(rgb));
    }
    
    /**
     * Inverses int[] RGB values
     */
    public static int[] inverse(int[] rgb)
    {
        return inverse(rgb[0], rgb[1], rgb[2]);
    }
    
    /**
     * Inverses hex values
     */
    public static int[] inverse(int hex)
    {
        return inverse(toRGB(hex));
    }
}