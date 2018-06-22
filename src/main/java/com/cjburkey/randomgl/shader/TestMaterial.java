package com.cjburkey.randomgl.shader;

public class TestMaterial {
    
    public final float specularBrightness;
    public final float shininess;
    
    public TestMaterial(float specularBrightness, float shininess) {
        this.specularBrightness = specularBrightness;
        this.shininess = shininess;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(shininess);
        result = prime * result + Float.floatToIntBits(specularBrightness);
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
        TestMaterial other = (TestMaterial) obj;
        if (Float.floatToIntBits(shininess) != Float.floatToIntBits(other.shininess)) {
            return false;
        }
        if (Float.floatToIntBits(specularBrightness) != Float.floatToIntBits(other.specularBrightness)) {
            return false;
        }
        return true;
    }
    
}