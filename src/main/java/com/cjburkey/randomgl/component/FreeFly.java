package com.cjburkey.randomgl.component;

import org.joml.Vector2f;
import com.cjburkey.randomgl.RandomGL;
import com.cjburkey.randomgl.input.Input;
import com.cjburkey.randomgl.object.Component;

public class FreeFly extends Component {
    
    private Vector2f input = new Vector2f().zero();
    
    public float speed = 5.0f;
    
    public void onUpdate() {
        input.set(Input.horizontal.getAmount(), -Input.vertical.getAmount());
        if (input.x != 0.0f || input.y != 0.0f) {
            input.normalize().mul(speed).mul(RandomGL.getDeltaTime());
        }
        
        parent.transform.position.x += input.x;
        parent.transform.position.z += input.y;
    }
    
}