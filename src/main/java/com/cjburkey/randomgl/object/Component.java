package com.cjburkey.randomgl.object;

import java.util.UUID;

public abstract class Component {
    
    public final GameObject parent = null;
    public final UUID uuid = null;
    
    public void onAdd() {
    }
    
    public void onRemove() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender() {
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
        Component other = (Component) obj;
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
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