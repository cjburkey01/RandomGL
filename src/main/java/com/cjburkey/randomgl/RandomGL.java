package com.cjburkey.randomgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

/**
 * MAKE SURE TO RUN WITH THE "-XstartOnFirstThread" JVM option
 */
public final class RandomGL {
    
    public final String[] args;
    
    private long window;
    private boolean running = false;
    private float deltaTime = 0.0f;
    
    private ShaderProgram testShader;
    
    private RandomGL(String[] args) {
        this.args = args;
    }
    
    private void start() {
        // Set default exception handling
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Debug.exception(e));
        
        // Initialize GLFW
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }
        Debug.info("Initialized GLFW");
        
        // Initialize window values
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // Use up-to-date features of OpenGL only
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);          // Allow newer versions of OpenGL
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);                  // Require OpenGL 3.3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);                      // Make window resizable
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);                       // Make window hidden by default
        Debug.info("Initialized window values");
        
        // Create the window of size 300x300 (300 is just a random number, the size will be changed next) on the primary monitor
        window = glfwCreateWindow(300, 300, "RandomGL 0.0.1", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        Debug.info("Created GLFW window");
        
        // Make this window the container for OpenGL
        glfwMakeContextCurrent(window);
        Debug.info("Made window context for OpenGL");
        
        // Initialize OpenGL and check version
        GL.createCapabilities();
        Debug.info("Initialized OpenGL {}", glGetString(GL_VERSION));
        
        // Enable VSync (set to 0 to disable)
        glfwSwapInterval(1);
        Debug.info("Enabled vsync");
        
        // Initialize OpenGL shaders
        testShader = new ShaderTest();
        if (!testShader.link()) {
            Debug.error("Could not link test shader");
            return;
        }
        testShader.bind();
        Debug.info("Initialized test shader");
        
        // Make window visible
        glfwShowWindow(window);
        Debug.info("Made window visibile");
        
        // Set window to half the size of the monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int monitorWidth = vidmode.width();
        int monitorHeight = vidmode.height();
        glfwSetWindowSize(window, monitorWidth / 2, monitorHeight / 2);
        Debug.info("Set window size to half the monitor of {}x{}", monitorWidth, monitorHeight);
        
        // Center window on screen
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetFramebufferSize(window, x, y);
        int windowWidth = x[0];
        int windowHeight = y[0];
        glfwSetWindowPos(window, (monitorWidth - windowWidth) / 2, (monitorHeight - windowHeight) / 2);
        Debug.info("Set window to middle of screen with size of {}x{}", windowWidth, windowHeight);
        
        // Set the window background to a gray color
        glClearColor(0.25f, 0.25f, 0.25f, 1.0f);
        
        running = true;
        
        long tickStart = System.nanoTime();
        long lastTick = System.nanoTime();
        float timeSinceLastFPSCheck = 0.0f;
        Debug.info("Starting game loop");
        // Begin the game loop and only end it if the game is no longer running
        while (running) {
            // Update delta time
            tickStart = System.nanoTime();
            deltaTime = (float) ((tickStart - lastTick) / 1000000000.0d);   // Convert from nanoseconds to seconds (1s = 1000000000ns)
            lastTick = tickStart;
            
            // Show approximate FPS and delta time in window title every 10th of a second
            if (timeSinceLastFPSCheck >= 0.1f) {
                timeSinceLastFPSCheck = 0.0f;
                glfwSetWindowTitle(window, "RandomGL ||| Approximate Delta: " + Format.format4(deltaTime) + " | Estimated FPS: " + Format.format2(1.0f / deltaTime));
            }
            timeSinceLastFPSCheck += deltaTime;
            
            // Check for input events
            glfwPollEvents();
            
            // Clear the screen for the next render
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            testShader.bind();
            
            // -- BEGIN DRAWING -- //
            
            // TODO: Drawing goes here
            
            // -- FINISH DRAWING -- //
            
            ShaderProgram.unbind();
            
            // Check if the player tried to close the window, and if they did, exit the game
            if (glfwWindowShouldClose(window)) {
                running = false;
            }
            
            // Show the next frame and put the current to the back buffer (will wait until the GPU is ready if vsync is enabled)
            glfwSwapBuffers(window);
        }
        
        // Exit the game
        Debug.info("Closing game");
    }
    
    public static void main(String[] args) {
        new RandomGL(args).start();
    }
    
}