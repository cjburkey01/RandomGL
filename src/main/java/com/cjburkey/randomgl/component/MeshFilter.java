package com.cjburkey.randomgl.component;

import com.cjburkey.randomgl.Mesh;
import com.cjburkey.randomgl.object.Component;
import com.cjburkey.randomgl.shader.ShaderProgram;

public final class MeshFilter extends Component {
    
    private Mesh mesh;
    
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    
    public void onRender() {
        if (getHasMesh()) {
            mesh.shader.bind();
            mesh.shader.setRenderUniforms(parent.transform);
            renderMesh();
            ShaderProgram.unbind();
        }
    }
    
    private void renderMesh() {
        mesh.render();
    }
    
    public Mesh getMesh() {
        return mesh;
    }
    
    public boolean getHasMesh() {
        return mesh != null;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mesh == null) ? 0 : mesh.hashCode());
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
        MeshFilter other = (MeshFilter) obj;
        if (mesh == null) {
            if (other.mesh != null) {
                return false;
            }
        } else if (!mesh.equals(other.mesh)) {
            return false;
        }
        return true;
    }
    
}