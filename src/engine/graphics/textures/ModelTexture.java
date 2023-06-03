package engine.graphics.textures;

public class ModelTexture {

    private int textureID;

    private float shineDamper = 10;
    private float reflectivity = 1;

    boolean hasTransparency = false; // if the texture contains transparency
    boolean useFakeLighting = false; // points all normals upwards (to smooth strange lighting)

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }
}
