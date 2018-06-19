package com.cjburkey.randomgl;

import com.cjburkey.randomgl.component.Transform;

public class ShaderTest extends ShaderProgram {
    
    public ShaderTest() {
        super(true);
    }
    
    protected void onAddShaders() {
        addVertAndFragShaders("res/shader/test/test");
    }
    
    protected void onAddAttributes() {
    }
    
    protected void onRegisterUniforms() {
    }
    
    protected void onSetRenderUniforms(Transform transform) {
    }
    
}