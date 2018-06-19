package com.cjburkey.randomgl.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cjburkey.randomgl.Pair;
import com.cjburkey.randomgl.event.GameEventHandler;
import com.cjburkey.randomgl.event.GameHandler;

public class InputEventHandler implements GameEventHandler {
    
    public static final int mouseButtonOffset = 0xFFFFFF;
    private static InputEventHandler instance;
    
    private final Map<Integer, List<Pair<Float, InputControl>>> inputs = new HashMap<>();
    
    public InputEventHandler() {
        instance = this;
        GameHandler.getInstance().addEventHandler(instance);
    }
    
    public void onKeyPress(int key) {
        updateInput(key, 1.0f);
    }
    
    public void onKeyRelease(int key) {
        updateInput(key, -1.0f);
    }
    
    public void onMousePress(int button) {
        updateInput(mouseButtonOffset + button, 1.0f);
    }
    
    public void onMouseRelease(int button) {
        updateInput(mouseButtonOffset + button, -1.0f);
    }
    
    public void onMouseMove(float x, float y, float deltaX, float deltaY) {
    }
    
    public void addKeyControl(int keyCode, InputControl inputControl, float weight) {
        List<Pair<Float, InputControl>> controls = null;
        if (!inputs.containsKey(keyCode)) {
            controls = new ArrayList<>();
            inputs.put(keyCode, controls);
        } else {
            inputs.get(keyCode);
        }
        controls.add(new Pair<>(weight, inputControl));
    }
    
    public void addMouseControl(int mouseButton, InputControl inputControl, float weight) {
        List<Pair<Float, InputControl>> controls = null;
        if (!inputs.containsKey(mouseButtonOffset + mouseButton)) {
            controls = new ArrayList<>();
            inputs.put(mouseButtonOffset + mouseButton, controls);
        } else {
            inputs.get(mouseButtonOffset + mouseButton);
        }
        controls.add(new Pair<>(weight, inputControl));
    }
    
    private void updateInput(int code, float direction) {
        List<Pair<Float, InputControl>> controls = inputs.get(code);
        if (controls == null) {
            return;
        }
        for (Pair<Float, InputControl> control : controls) {
            control.b.addAmount(control.a * direction);
        }
    }
    
    public static InputEventHandler getInstance() {
        return instance;
    }
    
}