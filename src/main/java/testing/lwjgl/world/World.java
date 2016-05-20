package testing.lwjgl.world;

import org.joml.Vector3f;

import testing.lwjgl.model.GameObject;
import testing.lwjgl.reference.Game;
import testing.lwjgl.util.Color;

public class World
{
    private static final int SIZE = 40;
    
    public World()
    {
        for(int i = 0; i < SIZE; ++i)
        {
            for(int j = 0; j < SIZE; ++j)
            {
                Terrain n = new Terrain(i, j);
                n.getModel().getMaterial().color = Color.toVec3f((5 * i), (i * j) % 255, (5 * j));
                Game.RENDERER.add(new GameObject(n.getModel(), new Vector3f(n.getX(), 0, n.getZ())));
            }
        }
    }
}
