package com.cjburkey.randomgl.component;

import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.randomgl.RandomGL;
import com.cjburkey.randomgl.event.GameEventHandler;
import com.cjburkey.randomgl.event.GameHandler;
import com.cjburkey.randomgl.input.Input;
import com.cjburkey.randomgl.object.Component;
import com.cjburkey.randomgl.util.Container;
import com.cjburkey.randomgl.util.MathUtil;

public final class MouseLook extends Component implements GameEventHandler {
    
    public static final float DEG_RAD = (float) Math.PI / 180.0f;
    
    public boolean lockCursor = true;
    public float xLimit = 90.0f;
    public float sensitivityX = 1.75f;
    public float sensitivityY = 1.75f;
    public float smoothing = 0.075f;
    
    private Vector2f input = new Vector2f().zero();
    private Vector3f rotationChange = new Vector3f().zero();
    private Vector3f rotation = new Vector3f().zero();
    private Container<Float> rotXVel = new Container<Float>(0.0f);
    private Container<Float> rotYVel = new Container<Float>(0.0f);
    private Container<Float> rotZVel = new Container<Float>(0.0f);
    private boolean lockLast = false;
    
    public MouseLook() {
        GameHandler.getInstance().addEventHandler(this);
    }
    
    public void onUpdate() {
        if (Input.controlDown("escape")) {
            lockLast = lockCursor;
            lockCursor = !lockCursor;
        }
        if (lockCursor != lockLast) {
            lockLast = lockCursor;
            RandomGL.getWindow().setCursorLocked(lockCursor);
        }
        
        if (!lockCursor) {
            return;
        }
        
        input.zero().set(Input.getMouseDelta()).negate();
        input.mul(sensitivityX, sensitivityY);
        rotationChange.set(input.y, input.x, 0.0f);
        rotation = MathUtil.smoothDamp(rotation, rotation.add(rotationChange, new Vector3f()), rotXVel, rotYVel, rotZVel, smoothing, 999, RandomGL.getDeltaTime());
        rotation.x = Math.max(-xLimit, Math.min(xLimit, rotation.x));
        
        parent.transform.rotation.identity().rotateY(rotation.y * DEG_RAD).rotateX(rotation.x * DEG_RAD);
    }
    
}