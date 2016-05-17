package testing.lwjgl.renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import org.joml.Vector2f;
import org.joml.Vector3f;

import logger.Log;
import utils.input.IO;
import utils.list.Tuple;
import utils.string.StringUtil;

public class OBJParser
{
    public static Model loadOBJ(InputStream in)
    {
        Model out = null;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>>> indices = new ArrayList<Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>>>();
        String[] split, splitL, splitM, splitR;
        boolean textured = true;
        boolean tried = false;
        try
        {
            BufferedReader br = IO.createBufferedReader(in);
            String line;
            while((line = br.readLine()) != null)
            {
                if(!line.startsWith("#") && !StringUtil.isEmpty(line))
                {
                    split = line.split(" ");
                    if(split == null)
                    {
                        continue;
                    }
                    if(split[0].equalsIgnoreCase("v"))
                    {
                        vertices.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
                    } else if(split[0].equalsIgnoreCase("vt"))
                    {
                        textures.add(new Vector2f(Float.parseFloat(split[1]), Float.parseFloat(split[2])));
                    } else if(split[0].equalsIgnoreCase("vn"))
                    {
                        normals.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3])));
                    } else if(split[0].equalsIgnoreCase("f"))
                    {
                        splitL = split[1].split("/");
                        splitM = split[2].split("/");
                        splitR = split[3].split("/");
                        if(!tried)
                        {
                            tried = true;
                            textured = !StringUtil.isEmpty(splitL[1]);
                        }
                        if(!textured)
                        {
                            indices.add(new Tuple(new Tuple(Integer.parseInt(splitL[0]) - 1, null, Integer.parseInt(splitL[2]) - 1), new Tuple(Integer.parseInt(splitM[0]) - 1, null, Integer.parseInt(splitM[2]) - 1), new Tuple(Integer.parseInt(splitR[0]) - 1, null, Integer.parseInt(splitR[2]) - 1)));
                        } else
                        {
                            indices.add(new Tuple(new Tuple(Integer.parseInt(splitL[0]) - 1, Integer.parseInt(splitL[1]) - 1, Integer.parseInt(splitL[2]) - 1), new Tuple(Integer.parseInt(splitM[0]) - 1, Integer.parseInt(splitM[1]) - 1, Integer.parseInt(splitM[2]) - 1), new Tuple(Integer.parseInt(splitR[0]) - 1, Integer.parseInt(splitR[1]) - 1, Integer.parseInt(splitR[2]) - 1)));
                        }
                    }
                }
            }
            float[] verticesOut = new float[vertices.size() * 3];
            float[] texturesOut = new float[vertices.size() * 2];
            float[] normalsOut  = new float[vertices.size() * 3];

            int index = 0;
            for(Vector3f v : vertices)
            {
                verticesOut[index * 3]     = v.x;
                verticesOut[index * 3 + 1] = v.y;
                verticesOut[index * 3 + 2] = v.z;
                ++index;
            }

            List<Integer> indicesOut = new ArrayList<Integer>();
            for(Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>> ind : indices)
            {
                indicesOut.add(ind.getLeft().getLeft());
                indicesOut.add(ind.getMiddle().getLeft());
                indicesOut.add(ind.getRight().getLeft());

                if(textured)
                {
                    texturesOut[ind.getLeft().getLeft() * 2]       = textures.get(ind.getLeft().getMiddle()).x;
                    texturesOut[ind.getLeft().getLeft() * 2 + 1]   = 1 - textures.get(ind.getLeft().getMiddle()).y;
                    texturesOut[ind.getMiddle().getLeft() * 2]     = textures.get(ind.getMiddle().getMiddle()).x;
                    texturesOut[ind.getMiddle().getLeft() * 2 + 1] = 1 - textures.get(ind.getMiddle().getMiddle()).y;
                    texturesOut[ind.getRight().getLeft() * 2]      = textures.get(ind.getRight().getMiddle()).x;
                    texturesOut[ind.getRight().getLeft() * 2 + 1]  = 1 - textures.get(ind.getRight().getMiddle()).y;
                }

                normalsOut[ind.getLeft().getLeft() * 3]       = normals.get(ind.getLeft().getRight()).x;
                normalsOut[ind.getLeft().getLeft() * 3 + 1]   = normals.get(ind.getLeft().getRight()).y;
                normalsOut[ind.getLeft().getLeft() * 3 + 2]   = normals.get(ind.getLeft().getRight()).z;
                normalsOut[ind.getMiddle().getLeft() * 3]     = normals.get(ind.getMiddle().getRight()).x;
                normalsOut[ind.getMiddle().getLeft() * 3 + 1] = normals.get(ind.getMiddle().getRight()).y;
                normalsOut[ind.getMiddle().getLeft() * 3 + 2] = normals.get(ind.getMiddle().getRight()).z;
                normalsOut[ind.getRight().getLeft() * 3]      = normals.get(ind.getRight().getRight()).x;
                normalsOut[ind.getRight().getLeft() * 3 + 1]  = normals.get(ind.getRight().getRight()).y;
                normalsOut[ind.getRight().getLeft() * 3 + 2]  = normals.get(ind.getRight().getRight()).z;
            }
            if(!textured)
            {
                texturesOut = null;
            }
            out = new Model(verticesOut, texturesOut, normalsOut, indicesOut.stream().mapToInt(new ToIntFunction<Integer>() {
                @Override
                public int applyAsInt(Integer v)
                {
                    return v;
                }
            }).toArray());
            br.close();
        } catch(IOException e)
        {
            Log.trace(e);
        }
        return out;
    }
}