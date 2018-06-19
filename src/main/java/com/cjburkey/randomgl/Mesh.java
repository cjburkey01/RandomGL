package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Mesh {
    
    public final ShaderProgram shader;
    private int indices = 0;
    private int vao = -1;
    private int ebo = -1;
    private Map<Integer, Integer> buffers = new LinkedHashMap<>();  // (location, pointer)
    
    // Creates a new mesh with the specified shader
    public Mesh(ShaderProgram shader) {
        this.shader = shader;
        vao = glGenVertexArrays();
        ebo = glGenBuffers();
        for (Attribute attribute : shader.getAttributes()) {
            buffers.put(attribute.location, glGenBuffers());
        }
    }
    
    // Everything except for indices are sent through the "data" parameter
    // Defines what the mesh is
    public boolean setMesh(int drawType, short[] indices, Object[]... data) {
        glBindVertexArray(vao);
        try {
            // Get a list of attributes that the shader will expect and verify that the data has been past
            Attribute[] attributes = shader.getAttributes();
            if (data.length != attributes.length) {
                throw new Exception("Data size did not match attribute count");
            }
            
            // Buffer data for each expected attribute
            for (int i = 0; i < attributes.length; i ++) {
                ByteBuffer buffer = memAlloc(data[i].length * attributes[i].getSize());
                if (buffer == null || buffer.capacity() < 1) {
                    throw new Exception("Buffer could not be created for attribute at location: " + attributes[i].location);
                }
                attributes[i].addToBuffer(buffer, data[i]);
                buffer.flip();
                
                // Send data to OpenGL
                glBindBuffer(GL_ARRAY_BUFFER, buffers.get(attributes[i].location));
                glBufferData(GL_ARRAY_BUFFER, buffer, drawType);
                if (attributes[i].location >= 0) {
                    glVertexAttribPointer(attributes[i].location, attributes[i].count, attributes[i].type, false, 0, 0);
                }
                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }
            
            // Send elements to OpenGL
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, drawType);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            
            this.indices = indices.length;
            
            return true;
        } catch (Exception e) {
            Debug.error("Failed to set mesh");
            Debug.exception(e);
        } finally {
            glBindVertexArray(0);
        }
        return false;
    }
    
    // Performs a "setMesh" with "GL_STATIC_DRAW"
    public boolean setMesh(short[] indices, Object[]... data) {
        return setMesh(GL_STATIC_DRAW, indices, data);
    }
    
    public void render() {
        if (vao > 0) {
            Attribute[] attributes = shader.getAttributes();
            
            glBindVertexArray(vao);
            for (Attribute attribute : attributes) {
                if (attribute.location >= 0) {
                    glEnableVertexAttribArray(attribute.location);
                }
            }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
            glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_SHORT, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            for (Attribute attribute : attributes) {
                if (attribute.location >= 0) {
                    glDisableVertexAttribArray(attribute.location);
                }
            }
            glBindVertexArray(0);
        }
    }
    
    public void destroy() {
        for (Integer buffer : buffers.values()) {
            glDeleteBuffers(buffer);
        }
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }
    
}