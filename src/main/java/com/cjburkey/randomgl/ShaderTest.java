package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL20.*;
import com.cjburkey.randomgl.component.Transform;

public class ShaderTest extends ShaderProgram {
    
    protected void onAddShaders() {
        addShader(GL_VERTEX_SHADER, IOUtil.readFile("res/shader/test/test.vsh"));
        addShader(GL_FRAGMENT_SHADER, IOUtil.readFile("res/shader/test/test.fsh"));
    }
    
    protected void onAddAttributes() {
    }
    
    protected void onRegisterUniforms() {
        registerTransformationUniforms();
    }
    
    public void setRenderUniforms(Transform transform) {
        setTransformationUniforms(transform);
    }
    
}