package com.cjburkey.randomgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window {
    
    private long window = NULL;
    private String title;
    private final Vector2i size = new Vector2i().zero();
    private final Vector2i position = new Vector2i().zero();
    
    public Window() {
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
        glfwWindowHint(GLFW_SAMPLES, 4);                                // Multisampling
        Debug.info("Initialized window values");
        
        // Create the window of size 300x300 (300 is just a random number, the size will be changed next) on the primary monitor
        window = glfwCreateWindow(300, 300, "", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        Debug.info("Created GLFW window");
        
        // Make sure that the "size" variable is updated when the window is resized
        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            size.set(w, h);
            
            // OpenGL must be notified about the window size change, too
            glViewport(0, 0, w, h);
        });
        
        // Make sure that the "position" variable is updated when the window is moved
        glfwSetWindowPosCallback(window, (win, x, y) -> position.set(x, y));
        
        // Make this window the container for OpenGL
        glfwMakeContextCurrent(window);
        Debug.info("Made window context for OpenGL");
        
        // Initialize OpenGL and check version
        GL.createCapabilities();
        Debug.info("Initialized OpenGL {}", glGetString(GL_VERSION));
        
        setVsync(true);
    }
    
    public void onPreRender() {
        // Clear the frame for the next render
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        // Check for input from the player
        glfwPollEvents();
    }
    
    public void onPostRender() {
        // Switch to the next frame
        glfwSwapBuffers(window);
    }
    
    public void setSizeHalfMonitor() {
        Vector2i monSize = getMonitorSize();
        setSize(new Vector2i(monSize.x / 2, monSize.y / 2));
    }
    
    public void centerOnScreen() {
        Vector2i monSize = getMonitorSize();
        Vector2i winSize = getSize();
        setPosition(new Vector2i((monSize.x - winSize.x) / 2, (monSize.y - winSize.y) / 2));
    }
    
    public void setSize(Vector2i size) {
        glfwSetWindowSize(window, size.x, size.y);
    }
    
    public Vector2i getSize() {
        return new Vector2i(size);
    }
    
    public void setPosition(Vector2i position) {
        glfwSetWindowPos(window, position.x, position.y);
    }
    
    public Vector2i getPos() {
        return new Vector2i(position);
    }
    
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(window, title);
    }
    
    public String getTitle() {
        return title;
    }
    
    public void show() {
        glfwShowWindow(window);
    }
    
    public void hide() {
        glfwHideWindow(window);
    }
    
    public boolean getShouldClose() {
        // Check if the player is trying to close the window
        return glfwWindowShouldClose(window);
    }
    
    public long getId() {
        return window;
    }
    
    public void setVsync(boolean enabled) {
        // When the parameter is "1", every single frame is finished by the GPU before another is drawn (single vsync).
        // When it's "0", the GPU will continue drawing, even during a render if necessary (unthrottled fps)
        // When any number more than "1", that many frames will be finished before a render with vsync
        // For example, if the frequency of a card is 60GHz(U.S. default), then a value of "1" would mean 60fps, and "2" would mean 30fps, "3" would mean 20fps, etc
        glfwSwapInterval((enabled) ? 1 : 0);
    }
    
    public static Vector2i getMonitorSize(long monitor) {
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);
        return new Vector2i(vidMode.width(), vidMode.height());
    }
    
    public static Vector2i getMonitorSize() {
        return getMonitorSize(glfwGetPrimaryMonitor());
    }
    
}