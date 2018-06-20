package com.cjburkey.randomgl.input;

public final class InputControl {
    
    private float amount;
    
    public InputControl() {
        amount = 0.0f;
    }
    
    public void addAmount(float amount) {
        this.amount += amount;
    }
    
    public float getAmount() {
        return Float.max(-1.0f, Float.min(1.0f, amount));
    }
    
}