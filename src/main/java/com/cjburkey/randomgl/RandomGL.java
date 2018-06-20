package com.cjburkey.randomgl;

import static org.lwjgl.opengl.GL11.*;
import com.cjburkey.randomgl.component.Camera;
import com.cjburkey.randomgl.component.FreeFly;
import com.cjburkey.randomgl.component.MeshFilter;
import com.cjburkey.randomgl.component.MouseLook;
import com.cjburkey.randomgl.input.InputEventHandler;
import com.cjburkey.randomgl.object.GameObject;
import com.cjburkey.randomgl.object.Scene;

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
        testMesh.setMesh(new short[] {
            2, 1, 0,
            2, 0, 3,
        }, new float[][] {
            // Vertices
            new float[] { 0.5f, 0.5f, 0.0f, },
            new float[] { 0.5f, -0.5f, 0.0f, },
            new float[] { -0.5f, -0.5f, 0.0f, },
            new float[] { -0.5f, 0.5f, 0.0f, },
        }, new float[][] {
            // Normals
            new float[] { 0.0f, 0.0f, -1.0f },
            new float[] { 0.0f, 0.0f, -1.0f },
            new float[] { 0.0f, 0.0f, -1.0f },
            new float[] { 0.0f, 0.0f, -1.0f },
        }, new float[][] {
            // UVs
            new float[] { 1.0f, 0.0f, },
            new float[] { 1.0f, 1.0f, },
            new float[] { 0.0f, 1.0f, },
            new float[] { 0.0f, 0.0f, },
        });
//        testMesh.setMesh(new short[] {
//            // Back
//            5, 2, 1,
//            6, 2, 5,
//            
//            // Front
//            0, 3, 4,
//            3, 7, 4,
//            
//            // Right
//            3, 2, 7,
//            7, 2, 6,
//            
//            // Left
//            4, 1, 0,
//            4, 5, 1,
//            
//            // Top
//            7, 5, 4,
//            6, 5, 7,
//            
//            // Bottom
//            1, 2, 3,
//            1, 3, 0,
//        }, new float[][] {
//            // Positions of vertices
//            new float[] { -0.5f, -0.5f, 0.5f, },    // 0
//            new float[] { -0.5f, -0.5f, -0.5f, },   // 1
//            new float[] { 0.5f, -0.5f, -0.5f, },    // 2
//            new float[] { 0.5f, -0.5f, 0.5f, },     // 3
//            new float[] { -0.5f, 0.5f, 0.5f, },     // 4
//            new float[] { -0.5f, 0.5f, -0.5f, },    // 5
//            new float[] { 0.5f, 0.5f, -0.5f, },     // 6
//            new float[] { 0.5f, 0.5f, 0.5f, },      // 7
//        }, new float[][] {
//            // Normals of vertices
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, 0.0f, },
//        }, new float[][] {
//            // UVs of vertices
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//            new float[] { 0.0f, 0.0f, },
//        });
        Debug.info("Initialized test mesh");
        
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
        testObject.transform.position.z = -2.0f;
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