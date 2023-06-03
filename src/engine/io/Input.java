package engine.io;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import utils.maths.geometry.Point;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static boolean[] keys = new boolean[GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];

    // input listeners (activated by GLFW)
    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mousePos;
    private GLFWMouseButtonCallback mouseButton;

    private static Point cursorLocation = new Point(0, 0);

//    private HashMap<Integer, InputFunction> keyMap = Input.initializeMap();

    // key binds
    // TODO: add keybinds here and into the hashmap

    public Input() {
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key > keys.length || key < 0) {
                    // for some reason, FN+F3, or FN+F_i seems to give 'key' value of -1
                    System.err.println("Invalid key pressed: index " + key);
                    return;
                }

                // log whether the key is down or not
                keys[key] = (action != GLFW_RELEASE);

                // key inputs go here
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        };

        mousePos = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                // mouse movement inputs go here
                cursorLocation = new Point(xpos, ypos);
            }
        };

        mouseButton = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button > buttons.length || button < 0) {
                    System.err.println("Invalid mouse button pressed: index " + button);
                    return;
                }

                // log whether the button is down or not
                buttons[button] = (action != GLFW_RELEASE);

                // mouse button inputs go here
            }
        };
    }

    // determines if the given key is being pressed
    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static boolean isMouseButtonDown(int button) {
        return buttons[button];
    }

    public void bindCallbacks(long window) {
        glfwSetMouseButtonCallback(window, mouseButton);
        glfwSetCursorPosCallback(window, mousePos);
        glfwSetKeyCallback(window, keyboard);
    }

    // frees callbacks (removing them from the window)
    public void destroy(long window) {
        glfwSetMouseButtonCallback(window, null);
        glfwSetCursorPosCallback(window, null);
        glfwSetKeyCallback(window, null);

        this.keyboard.free();
        this.mousePos.free();
        this.mouseButton.free();
    }

    /* INITIALIZE KEY MAP */

//    private static HashMap<Integer, InputFunction> initializeMap() {
//        return new HashMap<>(); // TODO: add map.put calls
//    }

    /* GETTERS AND SETTERS */

    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public void setKeyboard(GLFWKeyCallback keyboard) {
        this.keyboard = keyboard;
    }

    public static Point getCursorLocation() {
        return cursorLocation;
    }

    public GLFWMouseButtonCallback getMouseButtonCallback() {
        return mouseButton;
    }

    public void setMouseButton(GLFWMouseButtonCallback mouseButton) {
        this.mouseButton = mouseButton;
    }

    public GLFWCursorPosCallback getCursorPosCallback() {
        return this.mousePos;
    }
}
