package testing.lwjgl.util;

import org.joml.Matrix4f;

import testing.lwjgl.util.axis.ITransformable;

public class MatrixUtil
{
    public static Matrix4f getModelViewMatrix(ITransformable obj) { return new Matrix4f().identity().translate(obj.getXYZ()).rotateX((float) Math.toRadians(-obj.getRotX())).rotateY((float) Math.toRadians(-obj.getRotY())).rotateZ((float) Math.toRadians(-obj.getRotZ())).scale(obj.getScaleXYZ()); }
}