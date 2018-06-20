package com.cjburkey.randomgl.component;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.cjburkey.randomgl.object.Component;

public final class Transform extends Component {
    
    public final Vector3f position = new Vector3f().zero();
    public final Quaternionf rotation = new Quaternionf().identity();
    public final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    
    private final Matrix4f modelMatrix = new Matrix4f().identity();
    
    public Vector3f transformDirection(Vector3f dir, boolean normalize) {
        if (dir.x != 0.0f || dir.y != 0.0f || dir.z != 0.0f) {
            dir.normalize();
        }
        Matrix4f inv = rotation.get(new Matrix4f());
        Vector3f vec = inv.transformDirection(new Vector3f(dir));
        return (normalize && (vec.x != 0.0f || vec.y != 0.0f || vec.z != 0.0f)) ? vec.normalize() : vec;
    }
    
    public Vector3f transformDirection(Vector3f dir) {
        return transformDirection(dir, true);
    }
    
    public Matrix4f getModelMatrix() {
        return modelMatrix.identity().translate(position).rotate(rotation).scale(scale);
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
        result = prime * result + ((scale == null) ? 0 : scale.hashCode());
        return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Transform other = (Transform) obj;
        if (position == null) {
            if (other.position != null) {
                return false;
            }
        } else if (!position.equals(other.position)) {
            return false;
        }
        if (rotation == null) {
            if (other.rotation != null) {
                return false;
            }
        } else if (!rotation.equals(other.rotation)) {
            return false;
        }
        if (scale == null) {
            if (other.scale != null) {
                return false;
            }
        } else if (!scale.equals(other.scale)) {
            return false;
        }
        return true;
    }
    
}