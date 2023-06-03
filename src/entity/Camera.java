package entity;

import engine.io.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

public class Camera {

    private final Vector3f position = new Vector3f(25, 1, -18);
    private final Vector3f rotation = new Vector3f(-10, 223, 0);

    private double oldMouseX = 0, oldMouseY = 0;

    private static final float MOVE_SPEED = 0.5f;
    private static final float TURN_SPEED = 0.5f;

    // camera movement (based on keyboard and mouse inputs)
    public void move() {
        // mouse movement
        double newMouseX = Input.getCursorLocation().x;
        double newMouseY = Input.getCursorLocation().y;

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        rotation.translate(dy * Camera.TURN_SPEED, dx * Camera.TURN_SPEED, 0);
        if (rotation.getX() > 90) {
            rotation.setX(90);
        } else if (rotation.getX() < -90) {
            rotation.setX(-90);
        }
        if (rotation.getY() > 360) {
            rotation.setY(rotation.getY() % 360);
        } else if (rotation.getY() < 0) {
            rotation.setY(rotation.getY() % -360);
        }

        oldMouseX = newMouseX;
        oldMouseY = newMouseY;

        // preparation for movement
        float sin = (float) Math.sin(Math.toRadians(rotation.getY())) * Camera.MOVE_SPEED;
        float cos = (float) Math.cos(Math.toRadians(rotation.getY())) * Camera.MOVE_SPEED;
        float deltaY = (float) Math.sin(Math.toRadians(rotation.getX())) * Camera.MOVE_SPEED;

        // WASD keys
        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            position.translate(-cos, 0, -sin);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            position.translate(cos, 0, sin);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
            position.translate(sin, -deltaY, -cos);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
            position.translate(-sin, deltaY, cos);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            position.translate(0, MOVE_SPEED, 0);
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            position.translate(0, -MOVE_SPEED, 0);
        }
    }

    /* GETTER AND SETTER METHODS */

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return rotation.getX();
    }

    public float getYaw() {
        return rotation.getY();
    }

    public float getRoll() {
        return rotation.getZ();
    }

    public void setRotation(Vector3f v) {
        this.rotation.set(v.x, v.y, v.z);
    }
}
