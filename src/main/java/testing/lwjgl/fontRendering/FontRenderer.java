package testing.lwjgl.fontRendering;

import testing.lwjgl.fontMeshCreator.GUIText;
import testing.lwjgl.cleanup.ICleanUpAble;

public class FontRenderer implements ICleanUpAble
{

    private FontShader shader;

    public FontRenderer()
    {
        shader = new FontShader();
    }

    public void cleanUp()
    {
        shader.cleanUp();
    }

    private void prepare()
    {}

    private void renderText(GUIText text)
    {}

    private void endRendering()
    {}

}