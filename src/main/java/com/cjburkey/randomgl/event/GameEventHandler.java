package com.cjburkey.randomgl.event;

// Interfaces allow multiple inheritance
public interface GameEventHandler {
    
    // Default methods can be in interfaces with content (in this case, used to prevent the methods for causing a crash if they are not implemented in child classes)
    default void onWindowResize(int newWidth, int newHeight) {
    }
    
    default void onKeyPress(int key) {
    }
    
    default void onKeyRelease(int key) {
    }
    
    default void onKeyRepeat(int key) {
    }
    
    default void onMousePress(int button) {
    }
    
    default void onMouseRelease(int button) {
    }
    
    default void onMouseMove(float x, float y, float deltaX, float deltaY) {
    }
    
}