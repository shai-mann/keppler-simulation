package engine.graphics.loading;

import engine.graphics.models.RawModel;
import engine.graphics.textures.TextureData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13C.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

public class Loader {

    private static List<Integer> VAOS = new ArrayList<>();
    private static List<Integer> VBOS = new ArrayList<>();
    private static List<Integer> TEXTURE_IDS = new ArrayList<>();

    int i = 5;

    // turns a list of positions into a RawModel (using VAO storage style)
    public RawModel loadToVAO(float[] positions, int[] indices, float[] textureCoords,
                              float[] normalCoords) {
        int vaoID = this.createVAO();
        bindIndicesBuffer(indices); // bind indices to new VAO
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normalCoords);
        this.unbindVAO();

        // return RawModel (divide positions count by 3 to turn it into vertex count)
        return new RawModel(vaoID, indices.length);
    }

    // loads model without texture coordinates given
    public RawModel loadToVAONT(float[] positions, int[] indices, float[] normalCoords) {
        int vaoID = this.createVAO();
        bindIndicesBuffer(indices); // bind indices to new VAO
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 3, normalCoords);
        this.unbindVAO();

        // return RawModel (divide positions count by 3 to turn it into vertex count)
        return new RawModel(vaoID, indices.length);
    }

    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();

        return new RawModel(vaoID, positions.length / dimensions);
    }

    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture(fileName.split("[.]")[1],
                    Loader.class.getResourceAsStream(fileName));
        } catch (IOException e) {
            System.err.println("Texture Loading: Invalid texture path provided (" + fileName + ")");
            e.printStackTrace();
            System.exit(-1); // crash game - texture failed to load
        }

        Loader.TEXTURE_IDS.add(texture.getTextureID());
        return texture.getTextureID();
    }

    /*
    EXPECTED TEXTURE ORDERING: (UN-MUTABLE)
     - right face
     - left face
     - top face
     - bottom face
     - back face
     - front face
     */
    public int loadCubeMap(String[] textureFiles) {
        int textureID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

        for (int i = 0; i < textureFiles.length; i++) {
            TextureData data = decodeTextureFile(textureFiles[i]);
            GL11.glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
                    data.getWidth(), data.getHeight(), 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        // smooth out texture
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL20.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL20.GL_TEXTURE_WRAP_R, GL20.GL_CLAMP_TO_EDGE);

        TEXTURE_IDS.add(textureID); // keep track of this new texture
        return textureID;
    }

    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;

        try {
            InputStream in = Loader.class.getResourceAsStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);

            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            System.err.println("Texture Data Loading: Failed to load Texture Data");
            e.printStackTrace();
            System.exit(-1); // crash
        }

        return new TextureData(buffer, width, height);
    }

    // creates and binds a VAO ID
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        VAOS.add(vaoID); // to keep track of all created VAOs
        return vaoID;
    }

    // generate and bind VBO, then store data in it
    private void storeDataInAttributeList(int attributeNumber, int size, float[] data) {
        int vboID = GL15.glGenBuffers();
        VBOS.add(vboID); // to keep track of all created VBOs

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer fb = this.storeDataInFloatBuffer(data);

        // add data (in the form of a FloatBuffer) into the VBO
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size,
                GL11.GL_FLOAT, false, 0, 0);
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);

        return fb.put(data).flip();
    }

    // unbind the VAO (when done using it)
    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        VBOS.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer ib = this.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ib, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer ib = BufferUtils.createIntBuffer(data.length);
        return ib.put(data).flip();
    }

    // delete all VBOs, VAOs and Textures
    public static void clean() {
        for (Integer vaoID : VAOS) {
            GL30.glDeleteVertexArrays(vaoID);
        }
        for (Integer vboID : VBOS) {
            GL15.glDeleteBuffers(vboID);
        }
        for (Integer textureID : TEXTURE_IDS) {
            GL11.glDeleteTextures(textureID);
        }
    }

}
