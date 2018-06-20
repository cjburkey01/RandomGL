package com.cjburkey.randomgl.component;

import org.joml.Vector3f;
import com.cjburkey.randomgl.RandomGL;
import com.cjburkey.randomgl.input.Input;
import com.cjburkey.randomgl.object.Component;

public final class FreeFly extends Component {
    
    private Vector3f delta = new Vector3f().zero();
    
    public float speed = 5.0f;
    
    public void onUpdate() {
        delta.set(parent.transform.transformDirection(new Vector3f(Input.getControl("horizontal").getAmount(), 0.0f, -Input.getControl("vertical").getAmount())));
        delta.mul(speed).mul(RandomGL.getDeltaTime());
        parent.transform.position.add(delta);
    }
    
}