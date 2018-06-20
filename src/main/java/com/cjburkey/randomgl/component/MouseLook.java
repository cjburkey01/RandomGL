package com.cjburkey.randomgl.component;

import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.randomgl.MathUtil;
import com.cjburkey.randomgl.RandomGL;
import com.cjburkey.randomgl.Vector1f;
import com.cjburkey.randomgl.event.GameEventHandler;
import com.cjburkey.randomgl.event.GameHandler;
import com.cjburkey.randomgl.input.Input;
import com.cjburkey.randomgl.object.Component;

public class MouseLook extends Component implements GameEventHandler {
    
    public static final float DEG_RAD = (float) Math.PI / 180.0f;
    
    public boolean lockCursor = true;
    public float xLimit = 90.0f;
    public float sensitivityX = 1.75f;
    public float sensitivityY = 1.75f;
    public float smoothing = 0.075f;
    
    private Vector2f input = new Vector2f().zero();
    private Vector3f rotationChange = new Vector3f().zero();
    private Vector3f rotation = new Vector3f().zero();
    private Vector1f rotXVel = new Vector1f();
    private Vector1f rotYVel = new Vector1f();
    private Vector1f rotZVel = new Vector1f();
    private boolean lockLast = false;
    
    public MouseLook() {
        GameHandler.getInstance().addEventHandler(this);
    }
    
    public void onUpdate() {
        if (Input.controlDown("escape")) {
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