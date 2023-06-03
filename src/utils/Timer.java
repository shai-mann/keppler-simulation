package utils;

import org.lwjgl.glfw.GLFW;

public class Timer {

    float last = 0;
    float now = 0;

    public Timer() {
        now = (float) GLFW.glfwGetTime();
        last = now;
    }

    public float getDelta() {
        now = (float) GLFW.glfwGetTime();
        float delta = now - last;
        last = now;
        return delta;
    }

}
