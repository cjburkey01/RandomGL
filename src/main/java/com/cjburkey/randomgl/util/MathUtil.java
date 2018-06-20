package com.cjburkey.randomgl.util;

import org.joml.Vector2f;
import org.joml.Vector3f;

public final class MathUtil {
    
    // Smoothdamp functions are from Unity (don't tell them we robbed them)
    public static float smoothDamp(float current, float target, Container<Float> currentVelocity, float smoothTime, float maxSpeed, float deltaTime) {
        smoothTime = Float.max(0.0001f, smoothTime);
        float num = 2f / smoothTime;
        float num2 = num * deltaTime;
        float num3 = 1f / (1f + num2 + 0.48f * num2 * num2 + 0.235f * num2 * num2 * num2);
        float num4 = current - target;
        float num5 = target;
        float num6 = maxSpeed * smoothTime;
        num4 = Float.max(-num6, Float.min(num4, num6));
        target = current - num4;
        float num7 = (currentVelocity.value + num * num4) * deltaTime;
        currentVelocity.value = (currentVelocity.value - num * num7) * num3;
        float num8 = target + (num4 + num7) * num3;
        if (num5 - current > 0f == num8 > num5) {
            num8 = num5;
            currentVelocity.value = (num8 - num5) / deltaTime;
        }
        return num8;
    }
    
    public static Vector2f smoothDamp(Vector2f current, Vector2f target, Container<Float> currentVelocityX, Container<Float> currentVelocityY, float smoothTime, float maxSpeed, float deltaTime) {
        Vector2f out = new Vector2f();
        out.x = smoothDamp(current.x, target.x, currentVelocityX, smoothTime, maxSpeed, deltaTime);
        out.y = smoothDamp(current.y, target.y, currentVelocityY, smoothTime, maxSpeed, deltaTime);
        return out;
    }
    
    public static Vector3f smoothDamp(Vector3f current, Vector3f target, Container<Float> currentVelocityX, Container<Float> currentVelocityY, Container<Float> currentVelocityZ, float smoothTime, float maxSpeed, float deltaTime) {
        Vector3f out = new Vector3f();
        out.x = smoothDamp(current.x, target.x, currentVelocityX, smoothTime, maxSpeed, deltaTime);
        out.y = smoothDamp(current.y, target.y, currentVelocityY, smoothTime, maxSpeed, deltaTime);
        out.z = smoothDamp(current.z, target.z, currentVelocityZ, smoothTime, maxSpeed, deltaTime);
        return out;
    }
    
}