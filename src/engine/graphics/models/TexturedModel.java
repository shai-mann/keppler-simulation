package engine.graphics.models;

import engine.graphics.textures.ModelTexture;

public class TexturedModel {

    private ModelTexture texture;
    private RawModel model;

    public TexturedModel(RawModel model, ModelTexture texture) {
        this.texture = texture;
        this.model = model;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public RawModel getModel() {
        return model;
    }
}
