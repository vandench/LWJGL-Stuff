package testing.lwjgl.reference;

import testing.lwjgl.model.Model;
import testing.lwjgl.model.OBJParser;
import testing.lwjgl.resources.ResourceHelper;

public class Models
{
    public static final Model CUBE = OBJParser.loadOBJ(ResourceHelper.getModel("/Base.obj"));
    public static final Model DRAGON = OBJParser.loadOBJ(ResourceHelper.getModel("/dragon.obj"));
    public static final Model BUNNY = OBJParser.loadOBJ(ResourceHelper.getModel("/bunny.obj"));
    public static final Model JAR = OBJParser.loadOBJ(ResourceHelper.getModel("/JarPot.obj"));
}