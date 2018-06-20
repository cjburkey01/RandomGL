package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import com.cjburkey.randomgl.component.Camera;
import com.cjburkey.randomgl.component.Transform;

public abstract class ShaderProgram {
    
    private static ShaderProgram currentShader;
    
    private boolean linked = false;
    private boolean deleted = false;
    private int program = 0;
    private final Map<Integer, Integer> shaders = new HashMap<>();
    private final Map<String, Integer> uniforms = new HashMap<>();
    private final Set<Attribute> attributes = new TreeSet<>();
    public final boolean transforms;
    
    public ShaderProgram(boolean transforms) {
        // Generate the program with OpenGL
        program = glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Failed to create a shader program");
        }
        this.transforms = transforms;
        
        onAddShaders();
        addAttribute(0, GL_FLOAT, 3);   // Position attribute
        addAttribute(1, GL_FLOAT, 3);   // Normal attribute
        onAddAttributes();
        
        if (!link()) {
            throw new RuntimeException("Failed to link shader");
        }
        
        if (transforms) {
            registerTransformationUniforms();
        }
        onRegisterUniforms();
    }
    
    protected abstract void onAddShaders();
    protected abstract void onAddAttributes();
    protected abstract void onRegisterUniforms();
    protected abstract void onSetRenderUniforms(Transform transform);
    
    public final void setRenderUniforms(Transform transform) {
        if (transforms) {
            setTransformationUniforms(transform);
        }
        onSetRenderUniforms(transform);
    }
    
    protected final boolean addShader(int type, String source) {
        // Shaders cannot be added once the program has been linked
        if (linked) {
            Debug.warn("Attempted to add a shader to a linked shader program");
            return false;
        }
        
        // Check if a shader of this type exists already
        if (shaders.containsKey(type)) {
            return false;
        }
        
        // Generate the shader with OpenGL
        int shader = glCreateShader(type);
        if (shader == 0) {
            return false;
        }
        
        // Give the shader its code and compile it, then check for errors
        glShaderSource(shader, source);
        glCompileShader(shader);
        String log = glGetShaderInfoLog(shader);
        if (!stringEmpty(log)) {
            Debug.error("Failed to add shader, compilation failed: {}", log.trim());
        }
        
        // Add the shaders to the map to prevent duplicates
        shaders.put(type, shader);
        return true;
    }
    
    protected final boolean addVertAndFragShaders(String path) {
        if (!addShader(GL_VERTEX_SHADER, IOUtil.readFile(path + ".vsh"))) {
            return false;
        }
        if (!addShader(GL_FRAGMENT_SHADER, IOUtil.readFile(path + ".fsh"))) {
            return false;
        }
        return true;
    }
    
    protected final boolean removeShader(int type) {
        // Shaders cannot be removed once the program has been linked
        if (linked) {
            Debug.warn("Attempted to remove a shader from a linked shader program");
            return false;
        }
        
        // Make sure the shader exists
        if (!shaders.containsKey(type)) {
            return false;
        }
        shaders.remove(type);
        return true;
    }
    
    protected final boolean link() {
        // Cannot link an already-linked program
        if (linked) {
            Debug.warn("Attempted to link a linked shader program");
            return false;
        }
        
        // Attach all shaders to the shader program
        for (Entry<Integer, Integer> shader : shaders.entrySet()) {
            glAttachShader(program, shader.getValue());
        }
        
        // Link the shaders to the program and check for errors
        glLinkProgram(program);
        String log = glGetProgramInfoLog(program);
        if (!stringEmpty(log)) {
            Debug.error("Failed to link shader program: {}", log.trim());
            return false;
        }
        
        // Detach shaders and delete them (they are not needed after they are linked to the program)
        for (Entry<Integer, Integer> shader : shaders.entrySet()) {
            glDetachShader(program, shader.getValue());
            glDeleteShader(shader.getValue());
        }
        shaders.clear();
        
        linked = true;
        return true;
    }
    
    public final void delete() {
        // Deleting an unlinked shader doesn't do much other than clear the shaders and such
        if (!linked) {
            Debug.warn("Deleting an unlinked shader program");
            shaders.clear();
        }
        
        // Delete the program (shaders have already been deleted)
        glDeleteProgram(program);
        deleted = true;
    }
    
    private final int initUniform(String name) {
        // Shaders must be linked for uniforms to be found
        if (!linked) {
            return -1;
        }
        
        int loc = -1;
        if (!uniforms.containsKey(name)) {
            // Get the location of the uniform in the shader(s)
            bind();
            loc = glGetUniformLocation(program, name);
            if (loc >= 0) {
                uniforms.put(name, loc);
            }
        } else {
            loc = uniforms.get(name);
        }
        return loc;
    }
    
    protected final boolean registerUniform(String name) {
        return initUniform(name) >= 0;
    }
    
    protected final int getUniformLocation(String name) {
        if (uniforms.containsKey(name)) {
            return uniforms.get(name);
        }
        return Integer.MIN_VALUE;
    }
    
    public final void setUniform(String name, int value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        bind();
        glUniform1i(loc, value);
    }
    
    public final void setUniform(String name, float value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        bind();
        glUniform1f(loc, value);
    }
    
    public final void setUniform(String name, Vector2f value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        bind();
        glUniform2f(loc, value.x, value.y);
    }
    
    public final void setUniform(String name, Vector3f value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        bind();
        glUniform3f(loc, value.x, value.y, value.z);
    }
    
    public final void setUniform(String name, Vector4f value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        bind();
        glUniform4f(loc, value.x, value.y, value.z, value.w);
    }
    
    public final void setUniform(String name, Matrix3f value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        bind();
        glUniformMatrix3fv(loc, false, value.get(new float[9]));
    }
    
    public final void setUniform(String name, Matrix4f value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            Debug.warn("Uniform not found: {}", name);
            return;
        }
        bind();
        glUniformMatrix4fv(loc, false, value.get(new float[16]));
    }
    
    protected final void registerTransformationUniforms() {
        registerUniform("projectionMatrix");
        registerUniform("viewMatrix");
        registerUniform("modelMatrix");
    }
    
    protected final void setTransformationUniforms(Transform transform) {
        setUniform("projectionMatrix", Camera.getMainCamera().getProjectionMatrix());
        setUniform("viewMatrix", Camera.getMainCamera().getViewMatrix());
        setUniform("modelMatrix", transform.getModelMatrix());
    }
    
    protected boolean addAttribute(int location, int type, int count) {
        if (getAttribute(location) != null) {
            return false;
        }
        return attributes.add(new Attribute(location, type, count));
    }
    
    protected boolean removeAttribute(int location) {
        Attribute at = getAttribute(location);
        if (at == null) {
            return false;
        }
        attributes.remove(at);
        return true;
    }
    
    public Attribute getAttribute(int location) {
        for (Attribute attribute : attributes) {
            if (attribute.location == location) {
                return attribute;
            }
        }
        return null;
    }
    
    public Attribute[] getAttributes() {
        return attributes.toArray(new Attribute[attributes.size()]);
    }
    
    public int getAttributeCount() {
        return attributes.size();
    }
    
    public final boolean getIsLinked() {
        return linked;
    }
    
    public final boolean getIsDeleted() {
        return deleted;
    }
    
    public final void bind() {
        if (!linked) {
            Debug.warn("Attempted to bind an unlinked shader program");
            return;
        }
        currentShader = this;
        glUseProgram(program);
    }
    
    public static void unbind() {
        // Unbinds any bound shader
        glUseProgram(0);
    }
    
    public static ShaderProgram getCurrentShader() {
        return currentShader;
    }
    
    private static boolean stringEmpty(String input) {
        return (input == null || input.trim().isEmpty());
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + (deleted ? 1231 : 1237);
        result = prime * result + (linked ? 1231 : 1237);
        result = prime * result + program;
        result = prime * result + ((shaders == null) ? 0 : shaders.hashCode());
        result = prime * result + ((uniforms == null) ? 0 : uniforms.hashCode());
        return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ShaderProgram other = (ShaderProgram) obj;
        if (attributes == null) {
            if (other.attributes != null) {
                return false;
            }
        } else if (!attributes.equals(other.attributes)) {
            return false;
        }
        if (deleted != other.deleted) {
            return false;
        }
        if (linked != other.linked) {
            return false;
        }
        if (program != other.program) {
            return false;
        }
        if (shaders == null) {
            if (other.shaders != null) {
                return false;
            }
        } else if (!shaders.equals(other.shaders)) {
            return false;
        }
        if (uniforms == null) {
            if (other.uniforms != null) {
                return false;
            }
        } else if (!uniforms.equals(other.uniforms)) {
            return false;
        }
        return true;
    }
    
}