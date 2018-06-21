package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import com.cjburkey.randomgl.component.Camera;
import com.cjburkey.randomgl.component.FreeFly;
import com.cjburkey.randomgl.component.MeshFilter;
import com.cjburkey.randomgl.component.MouseLook;
import com.cjburkey.randomgl.input.InputEventHandler;
import com.cjburkey.randomgl.object.GameObject;
import com.cjburkey.randomgl.object.Scene;
import com.cjburkey.randomgl.shader.ShaderProgram;
import com.cjburkey.randomgl.shader.ShaderTest;
import com.cjburkey.randomgl.util.Debug;
import com.cjburkey.randomgl.util.Format;

/**
 * <b>MAKE SURE TO RUN WITH THE <code>-XstartOnFirstThread</code> JVM option (especially on Mac)</b>
 */
public final class RandomGL {
    
    private static RandomGL instance;
    
    public final String[] args;
    
    private boolean running = false;
    private float deltaTime = 0.0f;
    
    private Window window;
    private ShaderProgram testShader;
    private Scene testScene;
    private Mesh testMesh;
    private GameObject testObject;
    private Texture testTexture;
    
    public static GameObject lightObject;
    public static Mesh lightMesh;
    
    private RandomGL(String[] args) {
        this.args = args;
        new InputEventHandler();
    }
    
    private void start() {
        // Set default exception handling
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Debug.exception(e));
        
        // Initialize GLFW and OpenGL
        window = new Window();
        
        // Initialize OpenGL shaders
        testShader = new ShaderTest();
        testShader.bind();
        Debug.info("Initialized test shader");
        
        // Create a test scene
        testScene = new Scene();
        Debug.info("Initialized test scene");
        
        // Create a test mesh
        testMesh = new Mesh(testShader);
        setMesh();
        Debug.info("Initialized test mesh");
        
        lightMesh = new Mesh(testShader);
        lightMesh.setMesh(new short[] {
            0, 1, 2,
        }, new float[][] {
            new float[] { 0.0f, 0.5f, 0.0f },
            new float[] { -0.5f, -0.5f, 0.0f },
            new float[] { 0.5f, -0.5f, 0.0f },
        }, new float[][] {
            new float[] { 0.0f, 0.0f, 1.0f },
            new float[] { 0.0f, 0.0f, 1.0f },
            new float[] { 0.0f, 0.0f, 1.0f },
        }, new float[][] {
            new float[] { 0.0f, 0.0f },
            new float[] { 0.0f, 0.0f },
            new float[] { 0.0f, 0.0f },
        });
        Debug.info("Initialized light mesh");
        
        GameObject testCamera = testScene.createObject();
        testCamera.addComponent(new Camera());
        testCamera.addComponent(new FreeFly());
        testCamera.addComponent(new MouseLook());
        Debug.info("Initialized test camera");
        
        // Create a test object
        testObject = testScene.createObject();
        MeshFilter filter = new MeshFilter();
        filter.setMesh(testMesh);
        testObject.addComponent(filter);
        Debug.info("Initialized test object");
        
        lightObject = testScene.createObject();
        MeshFilter ffilter = new MeshFilter();
        ffilter.setMesh(lightMesh);
        lightObject.addComponent(ffilter);
        Debug.info("Initialized test object");
        
        testTexture = Texture.createTextureFromFile("res/texture/test.png");
        
        Debug.info("Initialized test texture");
        
        // Show the window
        window.show();
        window.setSizeHalfMonitor();
        window.centerOnScreen();
        window.setVsync(true);
        
        Camera.getMainCamera().setWindowSize(window.getSize());
        
        // Set the window background to a gray color
        glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
        
        long tickStart = System.nanoTime();
        long lastTick = System.nanoTime();
        float timeSinceLastFPSCheck = 0.0f;
        Debug.info("Starting game loop");
        
        // Begin the game loop and only end it if the game is no longer marked as running
        running = true;
        while (running) {
            // Update delta time
            tickStart = System.nanoTime();
            deltaTime = (float) ((tickStart - lastTick) / 1000000000.0d);   // Convert from nanoseconds to seconds (1s = 1000000000ns)
            lastTick = tickStart;
            
            // Show approximate FPS and delta time in window title every 45th of a second
            if (timeSinceLastFPSCheck >= 1.0f / 30.0f) {
                timeSinceLastFPSCheck = 0.0f;
                window.setTitle("RandomGL ||| Frametime: " + (Format.format2(deltaTime * 1000.0f)) + "ms | Approximate FPS: " + Format.format2(1.0f / deltaTime));
            }
            timeSinceLastFPSCheck += deltaTime;
            
            update();
            
            // Rendering
            window.onPreRender();
            
            testShader.bind();
            render();
            ShaderProgram.unbind();
            
            // Show the next frame and put the current to the back buffer (will wait until the GPU is ready if vsync is enabled)
            window.onPostRender();
            
            // Check if the player tried to close the window, and if they did, exit the game
            if (window.getShouldClose()) {
                running = false;
                break;     // Skip the sleep timer, the game should close anyway
            }
            
            // If the FPS is over 120, then we will throttle the game a little to prevent overusage of the GPU
            while (((System.nanoTime() - tickStart) / 1000000000.0d) < 1.0f / 120.0f) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    Debug.exception(e);
                }
            }
        }
        
        // Exit the game
        Debug.info("Closing game");
        exit();
    }
    
    // Called once per frame; represents the "physical" side of the game, such as objects and such in the world
    private void update() {
        testScene.onUpdate();
    }
    
    // Called once per frame; used to render meshes in the world
    private void render() {
        testTexture.bind();
        testScene.onRender();
        Texture.unbind();
    }
    
    // Called when the game loop is no longer running
    private void exit() {
        
    }
    
    private void setMesh() {
        List<Short> indices = new ArrayList<>();
        List<List<Float>> vertices = new ArrayList<>();
        List<List<Float>> normals = new ArrayList<>();
        List<List<Float>> texs = new ArrayList<>();
        
        createCube(vertices, indices, normals, texs);
        
        short[] inds = decodeShort(indices);
        float[][] verts = decode(vertices);
        float[][] norms = decode(normals);
        float[][] uvs = decode(texs);
        
        testMesh.setMesh(inds, verts, norms, uvs);
    }
    
    private static void createCube(List<List<Float>> vertices, List<Short> indices, List<List<Float>> normals, List<List<Float>> uvs) {
        createQuad(new Vector3f(), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), vertices, indices, normals, uvs);
        createQuad(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(1.0f, 0.0f, 0.0f), vertices, indices, normals, uvs);
        createQuad(new Vector3f(1.0f, 0.0f, -1.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, -1.0f), vertices, indices, normals, uvs);
        createQuad(new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(-1.0f, 0.0f, 0.0f), vertices, indices, normals, uvs);
        createQuad(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f), vertices, indices, normals, uvs);
        createQuad(new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f), vertices, indices, normals, uvs);
    }
    
    private static void createQuad(Vector3f corner, Vector3f up, Vector3f right, Vector3f normal, List<List<Float>> vertices, List<Short> indices, List<List<Float>> normals, List<List<Float>> uvs) {
        int index = vertices.size();
        
        List<Float> verta = new ArrayList<>();
        List<Float> vertb = new ArrayList<>();
        List<Float> vertc = new ArrayList<>();
        List<Float> vertd = new ArrayList<>();
        // 0
        verta.add(corner.x);
        verta.add(corner.y);
        verta.add(corner.z);
        // 1
        vertb.add(corner.x + up.x);
        vertb.add(corner.y + up.y);
        vertb.add(corner.z + up.z);
        // 2
        vertc.add(corner.x + up.x + right.x);
        vertc.add(corner.y + up.y + right.y);
        vertc.add(corner.z + up.z + right.z);
        // 3
        vertd.add(corner.x + right.x);
        vertd.add(corner.y + right.y);
        vertd.add(corner.z + right.z);
        
        vertices.add(verta);
        vertices.add(vertb);
        vertices.add(vertc);
        vertices.add(vertd);
        
        for (int i = 0; i < 4; i ++) {
            List<Float> normala = new ArrayList<>();
            normala.add(normal.x);
            normala.add(normal.y);
            normala.add(normal.z);
            normals.add(normala);
        }
        
        List<Float> uva = new ArrayList<>();
        List<Float> uvb = new ArrayList<>();
        List<Float> uvc = new ArrayList<>();
        List<Float> uvd = new ArrayList<>();
        uva.add(0.0f);
        uva.add(1.0f);
        uvb.add(0.0f);
        uvb.add(0.0f);
        uvc.add(1.0f);
        uvc.add(0.0f);
        uvd.add(1.0f);
        uvd.add(1.0f);
        uvs.add(uva);
        uvs.add(uvb);
        uvs.add(uvc);
        uvs.add(uvd);
        
        indices.add((short) (index + 2));
        indices.add((short) (index + 1));
        indices.add((short) (index));
        indices.add((short) (index));
        indices.add((short) (index + 3));
        indices.add((short) (index + 2));
    }
    
    private static float[][] decode(List<List<Float>> input) {
        if (input.size() < 1) {
            return new float[0][0];
        }
        float[][] a = new float[input.size()][input.get(0).size()];
        for (int i = 0; i < input.size(); i ++) {
            for (int j = 0; j < input.get(i).size(); j ++) {
                a[i][j] = input.get(i).get(j);
            }
        }
        return a;
    }
    
    private static short[] decodeShort(List<Short> input) {
        short[] out = new short[input.size()];
        for (int i = 0; i < input.size(); i ++) {
            out[i] = input.get(i);
        }
        return out;
    }
    
    public static float getDeltaTime() {
        return instance.deltaTime;
    }
    
    public static Window getWindow() {
        return instance.window;
    }
    
    public static void main(String[] args) {
        instance = new RandomGL(args);
        instance.start();
    }
    
}