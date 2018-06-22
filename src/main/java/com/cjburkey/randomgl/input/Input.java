package com.cjburkey.randomgl.input;

import static org.lwjgl.glfw.GLFW.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.joml.Vector2f;
import com.cjburkey.randomgl.util.Debug;

public final class Input {
    
    private static final Map<String, InputControl> inputs = new HashMap<>();
    private static final Map<String, Boolean> controlsDown = new HashMap<>();
    
    public static void registerInput(String name) {
        inputs.put(name.toUpperCase(), new InputControl());
    }
    
    public static InputControl getControl(String name) {
        name = name.toUpperCase();
        if (inputs.containsKey(name)) {
            return inputs.get(name);
        }
        Debug.warn("Control not found: {}", name);
        return null;
    }
    
    public static float getAmount(String name) {
        InputControl control = getControl(name);
        if (control == null) {
            return 0;
        }
        return control.getAmount();
    }
    
    public static boolean controlDown(String name) {
        return (controlsDown.containsKey(name.toUpperCase()) && controlsDown.get(name.toUpperCase()));
    }
    
    public static void _update() {
        for (Entry<String, InputControl> input : inputs.entrySet()) {
            if (input.getValue().getAmount() != 0.0f) {
                if (controlsDown.containsKey(input.getKey())) {
                    controlsDown.put(input.getKey(), false);
                    continue;
                }
                controlsDown.put(input.getKey(), true);
            } else {
                controlsDown.remove(input.getKey());
            }
        }
    }
    
    public static void _init() {
        registerInput("horizontal");
        registerInput("vertical");
        registerInput("escape");
        registerInput("r");
        registerInput("f");
        
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_D, getControl("horizontal"), 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_A, getControl("horizontal"), -1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_RIGHT, getControl("horizontal"), 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_LEFT, getControl("horizontal"), -1.0f);
        
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_W, getControl("vertical"), 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_S, getControl("vertical"), -1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_UP, getControl("vertical"), 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_DOWN, getControl("vertical"), -1.0f);
        
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_ESCAPE, getControl("escape"), 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_R, getControl("r"), 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_F, getControl("f"), 1.0f);
    }
    
    public static Vector2f getMousePos() {
        return InputEventHandler.getInstance().getMousePos();
    }
    
    public static Vector2f getMouseDelta() {
        return InputEventHandler.getInstance().getMouseDelta();
    }
    
}