package entity;

import engine.graphics.models.TexturedModel;
import engine.graphics.rendering.MasterRenderer;
import org.lwjglx.util.vector.Vector3f;

public class GameObject {

    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private float scale;

    public GameObject(TexturedModel model, Vector3f position,
                      float rx, float ry, float rz, float scale, MasterRenderer renderer) {
        this(model, position, new Vector3f(rx, ry, rz), scale, renderer);
    }

    public GameObject(TexturedModel model, Vector3f position,
                      Vector3f rotation, float scale, MasterRenderer renderer) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        renderer.processObject(this); // add self to list of objects to render
    }

    /* TRANSFORMATION ADJUSTMENT METHODS */

    public void translate(Vector3f addedPosition) {
        this.translate(addedPosition.x, addedPosition.y, addedPosition.z);
    }

    public void translate(float dx, float dy, float dz) {
        this.position.translate(dx, dy, dz);
    }

    public void rotate(float drx, float dry, float drz) {
        rotation.translate(drx, dry, drz);
    }

    public void scale(float dScale) {
        scale += dScale;
    }

    /* GETTERS AND SETTERS */

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rotation.getX();
    }

    public void setRx(float rx) {
        this.rotate(rx, 0, 0);
    }

    public float getRy() {
        return rotation.getY();
    }

    public void setRy(float ry) {
        this.rotate(0, ry, 0);
    }

    public float getRz() {
        return rotation.getZ();
    }

    public void setRz(float rz) {
        this.rotate(0, 0, rz);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
