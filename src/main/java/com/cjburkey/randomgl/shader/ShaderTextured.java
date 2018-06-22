package com.cjburkey.randomgl.shader;

import static org.lwjgl.opengl.GL11.*;
import com.cjburkey.randomgl.component.MeshFilter;

public abstract class ShaderTextured extends ShaderProgram {
    
    public ShaderTextured(boolean transforms) {
        super(transforms);
    }
    
    protected void onAddAttributes() {
        addAttribute(2, GL_FLOAT, 2);
    }
    
    protected void onRegisterUniforms() {
        registerUniform("tex");
    }
    
    protected void onSetRenderUniforms(MeshFilter mesh) {
        setUniform("tex", 0);
    }
    
}