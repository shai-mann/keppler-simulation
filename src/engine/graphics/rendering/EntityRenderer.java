package engine.graphics.rendering;

import engine.graphics.models.RawModel;
import engine.graphics.models.TexturedModel;
import engine.graphics.shaders.StaticShader;
import entity.GameObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjglx.util.vector.Matrix4f;
import utils.maths.MathUtils;

import java.util.HashMap;
import java.util.List;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    // renders a list of entities
    public void render(HashMap<TexturedModel, List<GameObject>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<GameObject> batch = entities.get(model);
            for (GameObject object : batch) {
                prepareInstance(object);
                // render model
                GL11.glDrawElements(
                        GL11.GL_TRIANGLES,
                        model.getModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);

            }
            unbindTexturedModel(); // reset texture model binding for next batch
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel model = texturedModel.getModel();

        GL30.glBindVertexArray(model.getVaoID()); // bind VAO
        GL30.glEnableVertexAttribArray(0); // enable vertex data
        GL30.glEnableVertexAttribArray(1); // enable texture data
        GL30.glEnableVertexAttribArray(2); // enable normals data
        GL30.glEnableVertexAttribArray(3); // enable shineDamper data
        GL30.glEnableVertexAttribArray(4); // enable reflectivity data

        if (texturedModel.getTexture().hasTransparency()) {
            MasterRenderer.disableCulling(); // culling is broken for transparent rendering
        }

        // load specular lighting information
        shader.loadSpecularLighting(texturedModel.getTexture());

        // load useFakeLighting value (as uniform)
        shader.loadFakeLighting(texturedModel.getTexture().useFakeLighting());

        // activate texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        GL30.glDisableVertexAttribArray(4); // disable reflectivity data
        GL30.glDisableVertexAttribArray(3); // disable shineDamper data
        GL30.glDisableVertexAttribArray(2); // disable normals data
        GL30.glDisableVertexAttribArray(1); // disable texture data
        GL30.glDisableVertexAttribArray(0); // disable vertex data
        GL30.glBindVertexArray(0); // unbind VAO

        MasterRenderer.enableCulling();
    }

    private void prepareInstance(GameObject object) {
        // construct and load transformation matrix into shader
        Matrix4f transformationMatrix =
                MathUtils.createTransformationMatrix(
                        object.getPosition(),
                        object.getRx(), object.getRy(), object.getRz(),
                        object.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
