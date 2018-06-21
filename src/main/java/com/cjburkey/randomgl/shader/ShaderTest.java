package com.cjburkey.randomgl.shader;

import org.joml.Vector3f;
import com.cjburkey.randomgl.RandomGL;
import com.cjburkey.randomgl.component.Camera;
import com.cjburkey.randomgl.component.Transform;
import com.cjburkey.randomgl.input.Input;

public final class ShaderTest extends ShaderTextured {
    
    private final Vector3f ambientLightColor = new Vector3f(0.1f, 0.1f, 0.1f);
    private final Vector3f pointLightPos = new Vector3f(2.0f, 0.25f, 5.0f);
    private final Vector3f pointLightColor = new Vector3f(1.0f, 1.0f, 1.0f);
    private float specularIntensity = 0.5f;
    
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
        registerUniform("specularIntensity");
        registerUniform("viewPos");
    }
    
    protected void onSetRenderUniforms(Transform object) {
        super.onSetRenderUniforms(object);
        
        pointLightPos.z -= RandomGL.getDeltaTime();
        if (Input.controlDown("r")) {
            pointLightPos.set(2.0f, 0.25f, 5.0f);
        }
        RandomGL.lightObject.transform.position.set(pointLightPos);
        
        setUniform("ambientLightColor", ambientLightColor);
        setUniform("pointLightPos", pointLightPos);
        setUniform("pointLightColor", pointLightColor);
        setUniform("specularIntensity", specularIntensity);
        setUniform("viewPos", Camera.getMainCamera().parent.transform.position);
    }
    
}