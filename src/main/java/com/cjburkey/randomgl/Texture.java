package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;
import java.nio.ByteBuffer;
import org.apache.commons.io.IOUtils;

public class Texture {
    
    private final int texture;
    
    public final int width;
    public final int height;
    
    private Texture(int width, int height, ByteBuffer imageData) {
        this.width = width;
        this.height = height;
        
        glEnable(GL_TEXTURE_2D);
        texture = glGenTextures();
        bind();
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);   // Pixel perfect (with mipmapping)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
        glGenerateMipmap(GL_TEXTURE_2D);
        memFree(imageData);
        unbind();
    }
    
    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public static Texture createTextureFromFile(String fileName) {
        try {
            byte[] rawImgData = IOUtils.toByteArray(IOUtil.getInputStream(fileName));
            ByteBuffer rawImgBuffer = memAlloc(rawImgData.length);
            rawImgBuffer.put(rawImgData);
            rawImgBuffer.flip();
            
            int[] width = new int[1];
            int[] height = new int[1];
            int[] channels = new int[1];
            ByteBuffer imgBuffer = stbi_load_from_memory(rawImgBuffer, width, height, channels, 0);
            Debug.info("Size: {}x{}", width[0], height[0]);
            Debug.info("Channels: {}", channels[0]);
            memFree(rawImgBuffer);
            
            return new Texture(width[0], height[0], imgBuffer);
        } catch (Exception e) {
            Debug.error("Failed to read texture file: {}", fileName);
            Debug.exception(e);
        }
        return null;
    }
    
}