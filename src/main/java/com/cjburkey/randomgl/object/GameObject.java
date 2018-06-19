package com.cjburkey.randomgl.object;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import com.cjburkey.randomgl.Debug;
import com.cjburkey.randomgl.component.Transform;

public final class GameObject {
    
    public final Transform transform;
    public final UUID uuid;
    private String name = "";
    
    private List<Component> components = new ArrayList<>();
    private Queue<Component> componentsToAdd = new LinkedList<>();
    private Queue<Component> componentsToRemove = new LinkedList<>();
    
    public GameObject(UUID uuid) {
        transform = new Transform();
        addComponent(transform);
        this.uuid = uuid;
    }
    
    public void onCreate() {
        updateComponents();
    }
    
    public void onDestroy() {
        removeAllComponents();
    }
    
    public void onUpdate() {
        updateComponents();
        components.forEach((c) -> c.onUpdate());
    }
    
    public void onRender() {
        components.forEach((c) -> c.onRender());
    }
    
    private void updateComponents() {
        while (!componentsToRemove.isEmpty()) {
            Component component = componentsToRemove.poll();
            if (component != null) {
                component.onRemove();
                components.remove(component);
            }
        }
        while (!componentsToAdd.isEmpty()) {
            Component component = componentsToAdd.poll();
            if (component != null) {
                components.add(component);
                setFields(component);
                component.onAdd();
            }
        }
    }
    
    private void setFields(Component component) {
        try {
            // Set the "parent" for the component even though the variable final and the object was already initialized, using reflection
            Field fieldParent = component.getClass().getField("parent");
            fieldParent.setAccessible(true);
            fieldParent.set(component, this);
            
            // Sets a theoretically unique id for the component to prevent conflicts
            Field fieldUuid = component.getClass().getField("uuid");
            fieldUuid.setAccessible(true);
            fieldUuid.set(component, UUID.randomUUID());
        } catch (Exception e) {
            Debug.error("Failed to define field in component {}", component.getClass().getSimpleName());
            Debug.exception(e);
        }
    }
    
    private void removeAllComponents() {
        componentsToAdd.clear();
        componentsToRemove.clear();
        for (Component component : components) {
            componentsToRemove.offer(component);
        }
        updateComponents();
    }
    
    public <T extends Component> boolean addComponent(T component) {
        if (getComponent(component.getClass()) != null) {
            return false;
        }
        componentsToAdd.offer(component);
        return true;
    }
    
    public <T extends Component> boolean removeComponent(Class<T> component) {
        T comp = getComponent(component);
        if (comp == null) {
            return false;
        }
        componentsToRemove.offer(comp);
        return true;
    }
    
    public <T extends Component> T getComponent(Class<T> component) {
        for (Component comp : components) {
            if (component.isAssignableFrom(comp.getClass())) {
                return component.cast(comp);
            }
        }
        return null;
    }
    
    public <T extends Component> Set<T> getComponents(Class<T> component) {
        Set<T> comps = new HashSet<>();
        for (Component comp : components) {
            if (component.isAssignableFrom(comp.getClass())) {
                comps.add(component.cast(comp));
            }
        }
        return comps;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GameObject other = (GameObject) obj;
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }
    
}