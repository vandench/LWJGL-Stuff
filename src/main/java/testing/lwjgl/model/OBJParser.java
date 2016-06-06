package testing.lwjgl.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import logger.Log;
import utils.array.ArrayUtil;
import utils.input.IO;
import utils.list.Tuple;
import utils.string.StringUtil;

public class OBJParser
{
    public static Model loadOBJ(InputStream in)
    {
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>>> indices = new ArrayList<Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>>>();
        boolean hasTextures = true;
        boolean hasNormals = true;
        try
        {
            BufferedReader br = IO.createBufferedReader(in);
            String line;
            while((line = br.readLine()) != null)
            {
                if(!line.startsWith("#") && !StringUtil.isEmpty(line))
                {
                    line = StringUtil.removeWhiteSpace(line);
                    String[] split = line.split(" ");
                    if(split == null || split.length == 0) { continue; }
                    if(split[0].equals("v")) { vertices.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]))); }
                    else if(split[0].equals("vt")) { textures.add(new Vector2f(Float.parseFloat(split[1]), Float.parseFloat(split[2]))); }
                    else if(split[0].equals("vn")) { normals.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]))); }
                    else if(split[0].equals("f"))
                    {
                        String[] splitL = split[1].split("/");
                        String[] splitM = split[2].split("/");
                        String[] splitR = split[3].split("/");
                        if(split[1].contains("//") || splitL.length == 1) { hasTextures = false; }
                        if(splitL.length <= 2) { hasNormals = false; }
                        Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>> indice = new Tuple<Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>, Tuple<Integer, Integer, Integer>>(new Tuple<Integer, Integer, Integer>(Integer.parseInt(splitL[0]) - 1, null, null), new Tuple<Integer, Integer, Integer>(Integer.parseInt(splitM[0]) - 1, null, null), new Tuple<Integer, Integer, Integer>(Integer.parseInt(splitR[0]) - 1, null, null));
                        if(hasTextures)
                        {
                            indice.getLeft().setMiddle(Integer.parseInt(splitL[1]) - 1);
                            indice.getMiddle().setMiddle(Integer.parseInt(splitM[1]) - 1);
                            indice.getRight().setMiddle(Integer.parseInt(splitR[1]) - 1);
                        }
                        
                        if(hasNormals)
                        {
                            indice.getLeft().setRight(Integer.parseInt(splitL[2]) - 1);
                            indice.getMiddle().setRight(Integer.parseInt(splitM[2]) - 1);
                            indice.getRight().setRight(Integer.parseInt(splitR[2]) - 1);
                        }
                        indices.add(indice);
                    }
                }
            }
            br.close();
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

                if(hasTextures)
                {
                    texturesOut[ind.getLeft().getLeft() * 2]       = textures.get(ind.getLeft().getMiddle()).x;
                    texturesOut[ind.getLeft().getLeft() * 2 + 1]   = 1 - textures.get(ind.getLeft().getMiddle()).y;
                    texturesOut[ind.getMiddle().getLeft() * 2]     = textures.get(ind.getMiddle().getMiddle()).x;
                    texturesOut[ind.getMiddle().getLeft() * 2 + 1] = 1 - textures.get(ind.getMiddle().getMiddle()).y;
                    texturesOut[ind.getRight().getLeft() * 2]      = textures.get(ind.getRight().getMiddle()).x;
                    texturesOut[ind.getRight().getLeft() * 2 + 1]  = 1 - textures.get(ind.getRight().getMiddle()).y;
                }

                if(hasNormals)
                {
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
            }
            if(!hasTextures) { texturesOut = null; }
            if(!hasNormals) { normalsOut = null; }
            return new Model(verticesOut, texturesOut, normalsOut, ArrayUtil.toIntArray(indicesOut));
        } catch(IOException e) { Log.trace(e); }
        return null;
    }
}