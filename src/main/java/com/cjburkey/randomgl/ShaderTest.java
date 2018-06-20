package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import com.cjburkey.randomgl.component.Transform;

public class ShaderTest extends ShaderProgram {
    
    public ShaderTest() {
        super(true);
    }
    
    protected void onAddShaders() {
        addVertAndFragShaders("res/shader/test/test");
    }
    
    protected void onAddAttributes() {
        addAttribute(2, GL_FLOAT, 2);
    }
    
    protected void onRegisterUniforms() {
        registerUniform("tex");
    }
    
    protected void onSetRenderUniforms(Transform transform) {
        setUniform("tex", 0);
    }
    
}