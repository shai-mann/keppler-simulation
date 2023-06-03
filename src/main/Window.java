package main;

import engine.io.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import utils.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    int width, height;
    String title;
    boolean mouseLocked = false;

    long windowID;
    Game game;

    GLFWWindowSizeCallback sizeCallback;
    Input input = new Input();
    Timer timer;

    public Window(int width, int height, String title, Game game) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.game = game;
    }

    /* FUNCTIONALITY METHODS */

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
        {
            System.err.println("GLFW Initialization: Failed to initialize GLFW");
            System.exit(1);
        }

        windowID = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowID == NULL)
        {
            System.err.println("Window Creation: Failed to create window");
            System.exit(1);
        }

        // create Open GL context
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        // activate Open GL context
        glfwMakeContextCurrent(windowID);
        GL.createCapabilities();
        glfwSwapInterval(1);

        // initialize callbacks
        initCallbacks();

        // set callbacks
        glfwSetWindowSizeCallback(windowID, sizeCallback);

        timer = new Timer();
        invertMouseLock(); // lock mouse
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowID);
    }

    public float getDelta() {
        return timer.getDelta();
    }

    // update window drawing
    void update() {
        // Poll the events and swap the buffers
        glfwPollEvents();
        glfwSwapBuffers(windowID);

        // set background colouuuuuuur to black
        glClearColor(0, 0, 0, 0);
    }

    // destroy window and terminates GLFW context
    public void dispose() {
        removeCallbacks();

        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    private void initCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                setSize(width, height);
            }
        };

        input.bindCallbacks(windowID);
    }

    void removeCallbacks() {
        glfwSetWindowSizeCallback(windowID, null);
        sizeCallback.free();
        input.destroy(windowID);
    }

    /* WINDOW MODIFIERS */

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println(width + " : " + height);
    }

    public void invertMouseLock() {
        mouseLocked = !mouseLocked;
        mouseState(mouseLocked);
    }

    private void mouseState(boolean lock) {
        GLFW.glfwSetInputMode(windowID, GLFW_CURSOR,
                lock ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

}
