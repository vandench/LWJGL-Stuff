package testing.lwjgl.reference;

import testing.lwjgl.renderer.Model;
import testing.lwjgl.renderer.OBJParser;
import testing.lwjgl.resources.ResourceHelper;

public class Models
{
    public static final Model CUBE = OBJParser.loadOBJ(ResourceHelper.getResource("assets/models/Base.obj"));
//    public static final Model DRAGON = OBJParser.loadOBJ(ResourceHelper.getResource("assets/models/dragon.obj.out"));
    public static final Model BUNNY = OBJParser.loadOBJ(ResourceHelper.getResource("assets/models/bunny.obj"));
}