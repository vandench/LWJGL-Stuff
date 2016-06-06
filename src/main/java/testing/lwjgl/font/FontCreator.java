package testing.lwjgl.font;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import logger.Log;
import testing.lwjgl.font.FontCreator.FontFile.Char;
import testing.lwjgl.font.FontCreator.FontFile.Page;
import testing.lwjgl.model.Model;
import testing.lwjgl.model.Texture;
import testing.lwjgl.reference.Textures;
import testing.lwjgl.util.Debug;
import utils.array.ArrayUtil;
import utils.input.IO;

public class FontCreator
{
    public static Model[] genModels(FontFile in)
    {
        Model[] models = new Model[in.chars.length];
        final int[] indices = new int[] { 0, 1, 3, 3, 1, 2 };
        int index = 0;
        List<Texture> textureList = new ArrayList<Texture>();
        for(Page p : in.pages) { textureList.add(new Texture(p.texName, false)); }
        Debug.debugTexture(textureList.get(0).getTexID());
        for(Char c : in.chars)
        {
            float texX = (float) c.x / (float)(in.texW - 1);
            float texY = (float)c.y / (float)(in.texH - 1);
            float texWidth = texX + (float)(c.width / (float)(in.texW - 1));
            float texHeight = texY +  (float)(c.height / (float)(in.texH - 1));
            float verW = (c.width / (in.texW - 1)) * 3;
            float verH = (c.height / (in.texH - 1)) * 3;
            verW = c.width / 1;
            verH = c.height / 1;
            float[] vertices = new float[] { 0, verH, 0, 0, verW, 0, verW, verH };
            float[] textures = new float[] { texX, texY, texX, texHeight, texWidth, texHeight, texWidth, texY };
            models[index] = new Model(vertices, 2, textures, 2, null, 2, indices);
            models[index].getMaterial().useLight = false;
            models[index].setTexID(textureList.get(c.page).getTexID());
            models[index].getMaterial().color = new Vector4f(1.0f, 0.0f, 0.0f, 0.5f);
            models[index].getMaterial().useColor = true;
            ++index;
        }
        return models;
    }

    public static FontFile loadCharacters(InputStream in) throws IOException
    {
        FontFile out = new FontFile();
        int charIndex = 0;
        for(List<String> lineSet : IO.readFile(in))
        {
            for(String line : lineSet)
            {
                if(line.startsWith("info "))
                {
                    for(String s : line.split(" "))
                    {
                        String[] split = s.split("=");
                        switch(split[0])
                        {
                            case "face":
                                out.name = split[1].replaceAll("\"", "");
                                break;
                            case "size":
                                out.size = Integer.parseInt(split[1]);
                                break;
                            case "bold":
                                out.bold = Boolean.parseBoolean(split[1]);
                                break;
                            case "italic":
                                out.italic = Boolean.parseBoolean(split[1]);
                                break;
                            case "charset":
                                out.charset = split[1].replaceAll("\"", "");
                                break;
                            case "unicode":
                                out.unicode = Boolean.parseBoolean(split[1]);
                                break;
                            case "stretchH":
                                out.fontHeightPercent = Integer.parseInt(split[1]);
                                break;
                            case "smooth":
                                out.smooth = Boolean.parseBoolean(split[1]);
                                break;
                            case "aa":
                                out.superSampling = Integer.parseInt(split[1]);
                                break;
                            case "padding":
                                split = split[1].split(",");
                                out.padding = ArrayUtil.toInt(split[0], split[1], split[2], split[3]);
                                break;
                            case "spacing":
                                split = split[1].split(",");
                                out.spacing = ArrayUtil.toInt(split[0], split[1]);
                                break;
                            case "outline":
                                out.outline = Integer.parseInt(split[1]);
                                break;
                        }
                    }
                } else if(line.startsWith("common "))
                {
                    for(String s : line.split(" "))
                    {
                        String[] split = s.split("=");
                        switch(split[0])
                        {
                            case "lineHeight":
                                out.lineHeight = Integer.parseInt(split[1]);
                                break;
                            case "base":
                                out.base = Integer.parseInt(split[1]);
                                break;
                            case "scaleW":
                                out.texW = Integer.parseInt(split[1]);
                                break;
                            case "scaleH":
                                out.texH = Integer.parseInt(split[1]);
                                break;
                            case "pages":
                                out.numPages = Integer.parseInt(split[1]);
                                break;
                            case "packed":
                                out.packed = Boolean.parseBoolean(split[1]);
                                break;
                        }
                    }
                } else if(line.startsWith("page "))
                {
                    Page page = new Page();
                    for(String s : line.split(" "))
                    {
                        String[] split = s.split("=");
                        switch(split[0])
                        {
                            case "id":
                                page.id = Integer.parseInt(split[1]);
                                break;
                            case "file":
                                page.texName = split[1].replaceAll("\"", "");
                                break;
                        }
                    }
                    if(out.pages == null || out.pages.length == 0) { out.pages = new Page[] { page }; }
                    else { ArrayUtil.append(out.pages, page); }
                } else if(line.startsWith("chars "))
                {
                    for(String s : line.split(" "))
                    {
                        String[] split = s.split("=");
                        switch(split[0])
                        {
                            case "count":
                                out.numChars = Integer.parseInt(split[1]);
                                out.chars = new Char[out.numChars];
                                break;
                        }
                    }
                } else if(line.startsWith("char "))
                {
                    Char c = new Char();
                    for(String s : line.split(" "))
                    {
                        String[] split = s.split("=");
                        switch(split[0])
                        {
                            case "id":
                                c.id = Integer.parseInt(split[1]);
                                break;
                            case "x":
                                c.x = Integer.parseInt(split[1]);
                                break;
                            case "y":
                                c.y = Integer.parseInt(split[1]);
                                break;
                            case "width":
                                c.width = Integer.parseInt(split[1]);
                                break;
                            case "height":
                                c.height = Integer.parseInt(split[1]);
                                break;
                            case "xoffset":
                                c.xOffset = Integer.parseInt(split[1]);
                                break;
                            case "yoffset":
                                c.yOffset = Integer.parseInt(split[1]);
                                break;
                            case "xadvance":
                                c.xAdvance = Integer.parseInt(split[1]);
                                break;
                            case "page":
                                c.page = Integer.parseInt(split[1]);
                                break;
                            case "chnl":
                                c.channel = Integer.parseInt(split[1]);
                                break;
                        }
                    }
                    out.chars[charIndex] = c;
                    ++charIndex;
                }
            }
        }
        return out;
    }

    public static class FontFile
    {
        // Info
        public String  name;
        public int     size;
        public boolean bold;
        public boolean italic;
        public String  charset;
        public boolean unicode;
        public int     fontHeightPercent;
        public boolean smooth;
        public int     superSampling;
        public int[]   padding;
        public int[]   spacing;
        public int     outline;

        // Common
        public int     lineHeight;
        public int     base;
        public int     texW;
        public int     texH;
        public int     numPages;
        public boolean packed;

        // Page
        public Page[]  pages;

        // Char
        public int     numChars;
        public Char[]  chars;

        public static class Page
        {
            public int    id;
            public String texName;
        }

        public static class Char
        {
            public int id;
            public int x;
            public int y;
            public int width;
            public int height;
            public int xOffset;
            public int yOffset;
            public int xAdvance;
            public int page;
            public int channel;
        }
    }
}