package com.cjburkey.randomgl.event;

@FunctionalInterface
public interface EventCall {
    
    void onCall(GameEventHandler handler);
    
}