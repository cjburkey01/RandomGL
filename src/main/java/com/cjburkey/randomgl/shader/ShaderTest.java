package com.cjburkey.randomgl.shader;

import org.joml.Vector3f;
import com.cjburkey.randomgl.RandomGL;
import com.cjburkey.randomgl.component.Camera;
import com.cjburkey.randomgl.component.MeshFilter;
import com.cjburkey.randomgl.input.Input;

// TODO: MAKE TEXTURED BOOL WORK
public final class ShaderTest extends ShaderTextured {
    
    private final Vector3f ambientLightColor = new Vector3f(0.1f, 0.1f, 0.1f);
    private final Vector3f pointLightPos = new Vector3f(2.0f, 0.5f, 5.0f);
    private final Vector3f pointLightColor = new Vector3f(0.75f, 0.5f, 0.33f);
    
    private final TestMaterial testMaterial = new TestMaterial(0.75f, 16.0f);
    
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
        
        registerUniform("ambientLightColor");
        registerUniform("pointLightPos");
        registerUniform("pointLightColor");
        registerUniform("viewPos");
        registerUniform("material.shininess");
        registerUniform("material.specularBrightness");
    }
    
    protected void onSetRenderUniforms(MeshFilter mesh) {
        super.onSetRenderUniforms(mesh);
        
        if (Input.getAmount("f") <= 0.0f) {
            pointLightPos.z -= RandomGL.getDeltaTime();
        }
        if (Input.controlDown("r")) {
            pointLightPos.set(2.0f, 0.5f, 5.0f);
        }
        RandomGL.lightObject.transform.position.set(pointLightPos);
        
        setUniform("ambientLightColor", ambientLightColor);
        setUniform("pointLightPos", pointLightPos);
        setUniform("pointLightColor", pointLightColor);
        setUniform("viewPos", Camera.getMainCamera().parent.transform.position);
        setUniform("material.shininess", testMaterial.shininess);
        setUniform("material.specularBrightness", testMaterial.specularBrightness);
    }
    
}