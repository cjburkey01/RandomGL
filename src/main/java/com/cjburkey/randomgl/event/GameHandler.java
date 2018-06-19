package com.cjburkey.randomgl.event;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    
    private static GameHandler gameHandler = new GameHandler();
    
    private final List<GameEventHandler> eventHandlers = new ArrayList<>();
    
    public void addEventHandler(GameEventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }
    
    public void call(EventCall onCall) {
        eventHandlers.forEach((eventHandler) -> onCall.onCall(eventHandler));
    }
    
    public static GameHandler getInstance() {
        return gameHandler;
    }
    
}