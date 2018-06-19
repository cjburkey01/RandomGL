package com.cjburkey.randomgl.input;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    
    public static final InputControl horizontal = new InputControl();
    public static final InputControl vertical = new InputControl();
    
    public static void init() {
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_D, horizontal, 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_A, horizontal, -1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_RIGHT, horizontal, 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_LEFT, horizontal, -1.0f);
        
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_W, vertical, 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_S, vertical, -1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_UP, vertical, 1.0f);
        InputEventHandler.getInstance().addKeyControl(GLFW_KEY_DOWN, vertical, -1.0f);
    }
    
}