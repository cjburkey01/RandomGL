package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL20.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ShaderProgram {
    
    private static ShaderProgram currentShader;
    
    private boolean linked = false;
    private boolean deleted = false;
    private int program = 0;
    private final Map<Integer, Integer> shaders = new HashMap<>();
    private final Map<String, Integer> uniforms = new HashMap<>();
    
    protected ShaderProgram() {
        // Generate the program with OpenGL
        program = glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Failed to create a shader program");
        }
        
        onAddShaders();
        onRegisterUniforms();
    }
    
    protected abstract void onAddShaders();
    protected abstract void onRegisterUniforms();
    
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
        glUniform1i(loc, value);
    }
    
    public final void setUniform(String name, float value) {
        int loc = getUniformLocation(name);
        if (loc < 0) {
            return;
        }
        glUniform1f(loc, value);
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
    
}