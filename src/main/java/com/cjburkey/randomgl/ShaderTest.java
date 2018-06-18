package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL20.*;

public class ShaderTest extends ShaderProgram {
    
    protected void onAddShaders() {
        addShader(GL_VERTEX_SHADER, IOUtil.readFile("/res/shader/test/test.vsh"));
        addShader(GL_FRAGMENT_SHADER, IOUtil.readFile("/res/shader/test/test.fsh"));
    }
    
    protected void onRegisterUniforms() {
    }
    
}