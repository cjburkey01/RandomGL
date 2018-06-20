package com.cjburkey.randomgl.shader;

import com.cjburkey.randomgl.component.Transform;

public final class ShaderTest extends ShaderTextured {
    
    public ShaderTest() {
        super(true);
    }
    
    protected void onAddShaders() {
        addVertAndFragShaders("res/shader/test/test");
    }
    
    protected void onAddAttributes() {
        super.onAddAttributes();
    }
    
    protected void onRegisterUniforms() {
        super.onRegisterUniforms();
    }
    
    protected void onSetRenderUniforms(Transform object) {
        super.onSetRenderUniforms(object);
    }
    
}