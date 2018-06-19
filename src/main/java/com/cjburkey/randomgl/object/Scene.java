package com.cjburkey.randomgl.object;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class Scene {
    
    private static Scene activeScene;
    
    private final List<GameObject> objects = new LinkedList<>();
    private Queue<GameObject> objectsToAdd = new LinkedList<>();
    private Queue<GameObject> objectsToRemove = new LinkedList<>();
    
    public Scene() {
        if (activeScene == null) {
            activeScene = this;
        }
    }
    
    public GameObject createObject() {
        GameObject object = new GameObject(UUID.randomUUID());
        objectsToAdd.offer(object);
        return object;
    }
    
    public void onUpdate() {
        updateObjects();
        objects.forEach((obj) -> obj.onUpdate());
    }
    
    public void onRender() {
        objects.forEach((obj) -> obj.onRender());
    }
    
    public void destroy(GameObject object) {
        if (objects.contains(object)) {
            objectsToRemove.offer(object);
        }
    }
    
    private void updateObjects() {
        while (!objectsToRemove.isEmpty()) {
            GameObject object = objectsToRemove.poll();
            if (object != null) {
                object.onDestroy();
                objects.remove(object);
            }
        }
        while (!objectsToAdd.isEmpty()) {
            GameObject object = objectsToAdd.poll();
            if (object != null) {
                objects.add(object);
                object.onCreate();
            }
        }
    }
    
    public void makeActive() {
        activeScene = this;
    }
    
    public static Scene getActiveScene() {
        return activeScene;
    }
    
}