package testing.lwjgl.shader;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import logger.Log;
import testing.lwjgl.cleanup.ICleanUpAble;
import testing.lwjgl.event.events.ExitEvent;
import testing.lwjgl.reference.Game;
import testing.lwjgl.shader.Light.Attenuation;
import testing.lwjgl.shader.Light.Material;
import testing.lwjgl.shader.Light.PointLight;
import utils.string.StringUtil;

public class Shader implements ICleanUpAble
{
    private final int                  m_programID;
    private int                        m_vertexShaderID;
    private int                        m_fragmentShaderID;
    private final Map<String, Integer> m_uniforms;

    public Shader()
    {
        Game.CLEAN_UP_HANDLER.addCleanUpAble(this);
        m_programID = GL20.glCreateProgram();
        if(m_programID == 0)
        {
            Log.trace(new Exception("Unable to create shader."));
            Game.EVENT_BUS.dispatch(new ExitEvent(-1));
        }
        m_uniforms = new HashMap<String, Integer>();
        Game.SHADER = this;
    }

    public void createShader(String path, int type)
    {
        if(!(type == GL20.GL_VERTEX_SHADER || type == GL20.GL_FRAGMENT_SHADER)) { Log.trace(new Exception("Invalid shader type, must be GL20.GL_VERTEX_SHADER or GL20.GL_FRAGMENT_SHADER")); }

        int shaderID = GL20.glCreateShader(type);
        if(shaderID == 0) { Log.trace(new Exception("Error creating shader.")); }

        GL20.glShaderSource(shaderID, path);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) { Log.trace(new Exception("Error compiling shader, ShaderID: " + shaderID + ",\n\tShader Type: " + (type == GL20.GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT") + ", Error:" + GL20.glGetShaderInfoLog(shaderID))); }

        GL20.glAttachShader(m_programID, shaderID);

        if(type == GL20.GL_VERTEX_SHADER) { m_vertexShaderID = shaderID; }
        else { m_fragmentShaderID = shaderID; }
    }

    public void createUniform(String name)
    {
        int uniformLocation = GL20.glGetUniformLocation(m_programID, name);
        if(uniformLocation < 0) { Log.trace(new Exception("Could not find uniform variable with name " + name)); }

        m_uniforms.put(name, uniformLocation);
    }

    public void setUniform(String name, Matrix4f value)
    {
        if(!m_uniforms.containsKey(name)) { createUniform(name); }
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        GL20.glUniformMatrix4fv(m_uniforms.get(name), false, buffer);
    }
    
    public void setUniform(String name, Vector4f value)
    {
        if(!m_uniforms.containsKey(name)) { createUniform(name); }
        GL20.glUniform4f(m_uniforms.get(name), value.x, value.y, value.z, value.w);
    }
    
    public void setUniform(String name, Vector3f value)
    {
        if(!m_uniforms.containsKey(name)) { createUniform(name); }
        GL20.glUniform3f(m_uniforms.get(name), value.x, value.y, value.z);
    }
    
    public void setUniform(String name, int value)
    {
        if(!m_uniforms.containsKey(name)) { createUniform(name); }
        GL20.glUniform1i(m_uniforms.get(name), value);
    }
    
    public void setUniform(String name, float value)
    {
        if(!m_uniforms.containsKey(name)) { createUniform(name); }
        GL20.glUniform1f(m_uniforms.get(name), value);
    }
    
    public void setUniform(String name, boolean value)
    {
        setUniform(name, (value ? 1 : 0));
    }

    public void setUniform(String name, Attenuation att)
    {
        setUniform(name + ".constant", att.constant);
        setUniform(name + ".linear", att.linear);
        setUniform(name + ".exponent", att.exponent);
    }
    
    public void setUniform(String name, PointLight light)
    {
        setUniform(name + ".color", light.color);
        setUniform(name + ".position", light.position);
        setUniform(name + ".intensity", light.intensity);
        setUniform(name + ".att", light.att);
    }
    
    public void setUniform(String name, Material mat)
    {
        setUniform(name + ".color", mat.color);
        setUniform(name + ".useColor", mat.useColor);
        setUniform(name + ".useTexture", mat.useTexture);
        setUniform(name + ".useLight", mat.useLight);
        setUniform(name + ".reflectance", mat.reflectance);
    }
    
    public void link()
    {
        GL20.glLinkProgram(m_programID);
        if(GL20.glGetProgrami(m_programID, GL20.GL_LINK_STATUS) == 0) { Log.trace(new Exception("Error linking program: " + GL20.glGetShaderInfoLog(m_programID, 1024))); }

        GL20.glValidateProgram(m_programID);
        if(GL20.glGetProgrami(m_programID, GL20.GL_VALIDATE_STATUS) == 0)
        {
            String tmp = GL20.glGetShaderInfoLog(m_programID);
            if(StringUtil.isEmpty(tmp)) { tmp = "No log, you can probably ignore this."; }
            Log.warn("Warning validating program: " + tmp);
        }
    }

    public void bind() { GL20.glUseProgram(m_programID); }

    public void unbind() { GL20.glUseProgram(0); }

    @Override
    public void cleanUp()
    {
        if(this != null)
        {
            unbind();
            if(m_programID != 0)
            {
                if(m_vertexShaderID != 0) { GL20.glDetachShader(m_programID, m_vertexShaderID); }
                if(m_fragmentShaderID != 0) { GL20.glDetachShader(m_programID, m_fragmentShaderID); }
                GL20.glDeleteProgram(m_programID);
            }
        }
    }
}