package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.ByteBuffer;
import org.apache.commons.io.IOUtils;
import com.cjburkey.randomgl.util.Debug;
import com.cjburkey.randomgl.util.IOUtil;

public final class Texture {
    
    private final int texture;
    
    public final int width;
    public final int height;
    
    private Texture(int width, int height) {
        this.width = width;
        this.height = height;
        
        glEnable(GL_TEXTURE_2D);
        texture = glGenTextures();
    }
    
    private void generateMipMappedTexture(ByteBuffer imageData) {
        bind();
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
        glGenerateMipmap(GL_TEXTURE_2D);
        memFree(imageData);
        unbind();
    }
    
    public void destroy() {
        unbind();
        glDeleteTextures(texture);
    }
    
    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + texture;
        result = prime * result + width;
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
        Texture other = (Texture) obj;
        if (height != other.height) {
            return false;
        }
        if (texture != other.texture) {
            return false;
        }
        if (width != other.width) {
            return false;
        }
        return true;
    }
    
    public static Texture createTextureFromRawBytes(byte[] rawImgData) {
        try {
            ByteBuffer rawImgBuffer = memAlloc(rawImgData.length);
            rawImgBuffer.put(rawImgData);
            rawImgBuffer.flip();
            
            int[] width = new int[1];
            int[] height = new int[1];
            ByteBuffer imgBuffer = stbi_load_from_memory(rawImgBuffer, width, height, new int[1], 0);
            memFree(rawImgBuffer);
            
            Texture texture = new Texture(width[0], height[0]);
            texture.generateMipMappedTexture(imgBuffer);
            return texture;
        } catch (Exception e) {
            Debug.error("Failed to load texture from raw bytes");
            Debug.exception(e);
        }
        return null;
    }
    
    public static Texture createTextureFromFile(String fileName) {
        try {
            return createTextureFromRawBytes(IOUtils.toByteArray(IOUtil.getInputStream(fileName)));
        } catch (Exception e) {
            Debug.error("Failed to load texture from file: {}", fileName);
            Debug.exception(e);
        }
        return null;
    }
    
}