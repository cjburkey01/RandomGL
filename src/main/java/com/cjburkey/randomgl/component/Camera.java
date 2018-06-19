package com.cjburkey.randomgl.component;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.randomgl.object.Component;

public class Camera extends Component {
    
    private static Camera mainCamera;
    
    private boolean hasProjectionUpdated = true;
    private final Matrix4f projectionMatrix = new Matrix4f().identity();
    private final Matrix4f viewMatrix = new Matrix4f().identity();
    
    private final Vector2f viewBounds = new Vector2f().zero();  // near, far
    private float fovDegrees;
    private final Vector2i windowSize = new Vector2i().zero();
    
    public Camera() {
        if (mainCamera == null) {
            mainCamera = this;
        }
        
        setFov(75.0f);
        setNearPlane(0.01f);
        setFarPlane(1000.0f);
        setWindowSize(new Vector2i(10, 10));
    }
    
    public void setFov(float fovDegrees) {
        this.fovDegrees = fovDegrees;
        hasProjectionUpdated = true;
    }
    
    public void setNearPlane(float near) {
        viewBounds.x = near;
        hasProjectionUpdated = true;
    }
    
    public void setFarPlane(float far) {
        viewBounds.y = far;
        hasProjectionUpdated = true;
    }
    
    public void setWindowSize(Vector2i windowSize) {
        this.windowSize.set(windowSize);
        hasProjectionUpdated = true;
    }
    
    public Matrix4f getProjectionMatrix() {
        if (hasProjectionUpdated) {
            hasProjectionUpdated = false;
            projectionMatrix.identity().perspective((float) Math.toRadians(fovDegrees), (float) windowSize.x / windowSize.y, viewBounds.x, viewBounds.y);
        }
        return new Matrix4f(projectionMatrix);
    }
    
    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix.identity().rotate(parent.transform.rotation.invert(new Quaternionf())).translate(parent.transform.position.negate(new Vector3f())));
    }
    
    public void makeMain() {
        mainCamera = this;
    }
    
    public static Camera getMainCamera() {
        return mainCamera;
    }
    
}