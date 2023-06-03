package engine.graphics.rendering;

import engine.graphics.loading.Loader;
import engine.graphics.models.TexturedModel;
import engine.graphics.shaders.StaticShader;
import entity.Camera;
import entity.GameObject;
import entity.Light;
import main.Main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjglx.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;

    StaticShader staticShader = new StaticShader();
    EntityRenderer entityRenderer;

    HashMap<TexturedModel, List<GameObject>> entities = new HashMap<>();

    public MasterRenderer(Loader loader) {
        enableCulling();

        createProjectionMatrix();

        entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
    }

    public void render(Light light, Camera camera) {
        prepare();

        staticShader.start();
        staticShader.loadLight(light);
        staticShader.loadViewMatrix(camera);

        // render entities
        entityRenderer.render(entities);

        staticShader.stop();
    }

    // called once per frame
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(1f, 0, 0, 1);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Main.WIDTH / (float) Main.HEIGHT;
        float y_scale = (float) (1f / Math.tan(Math.toRadians(MasterRenderer.FOV / 2f))) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustrum_length = MasterRenderer.FAR_PLANE - MasterRenderer.NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((MasterRenderer.FAR_PLANE + MasterRenderer.NEAR_PLANE) / frustrum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * MasterRenderer.NEAR_PLANE * MasterRenderer.FAR_PLANE) / frustrum_length);
        projectionMatrix.m33 = 0;
    }

    public void processObject(GameObject object) {
        TexturedModel model = object.getModel();
        List<GameObject> batch = entities.get(model);

        if (batch != null) {
            batch.add(object);
        } else {
            batch = new ArrayList<>();
            batch.add(object);
            entities.put(model, batch);
        }
    }

    // prevent rendering of objects not displayed by the viewport
    public static void enableCulling() {
        GL30.glEnable(GL11.GL_CULL_FACE);
        GL30.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL30.glDisable(GL11.GL_CULL_FACE);
    }

    public void clean() {
        entities.clear();
        staticShader.clean();
    }
}
