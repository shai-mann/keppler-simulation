package engine.graphics.loading;

import engine.graphics.models.RawModel;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {

    public static RawModel loadObjModel(String fileName, Loader loader) {
        BufferedReader reader = null;
        reader = new BufferedReader(
                new InputStreamReader(ObjectLoader.class.getResourceAsStream(fileName)));

        String line = "";

        // prepare to read values from file
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray;
        float[] texturesArray = null;
        float[] normalsArray = null;
        int[] indicesArray;

        try {
            while ((line = reader.readLine()) != null) {
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) { // implies vertex position
                    vertices.add(readVector3D(currentLine));

                } else if (line.startsWith("vt ")) { // implies texture position
                    textures.add(readVector2D(currentLine));

                } else if (line.startsWith("vn ")) { // implies normal position
                    normals.add(readVector3D(currentLine));

                } else if (line.startsWith("f ")) { // implies index values (face)
                    texturesArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];

                    break; // pass off to separate half of function to load in indices
                }
            }

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                // detection for non-textured models:
                if (textures.size() == 0) {
                    textures.add(new Vector2f(0, 0)); // empty texture coordinate
                }

                processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Object Reading: Error reading object at path (" + fileName + ")");
            e.printStackTrace();
            System.exit(-1); //crash
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        // convert vertices from List into float[]
        int vertexPointer = 0;
        for (Vector3f v : vertices) {
            verticesArray[vertexPointer++] = v.x;
            verticesArray[vertexPointer++] = v.y;
            verticesArray[vertexPointer++] = v.z;
        }

        // convert indices from List into int[]
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, indicesArray, texturesArray, normalsArray);
    }

    // presumed to be in the following form:
    // ["suffix", "x", "y", "z"]
    static Vector3f readVector3D(String[] line) {
        return new Vector3f(
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2]),
                Float.parseFloat(line[3])
        );
    }

    // presumed to be in the following form:
    // ["suffix", "x", "y"]
    static Vector2f readVector2D(String[] line) {
        return new Vector2f(
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2])
        );
    }

    static void processVertex(
            String[] vertexData,
            List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
            float[] textureArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        // 1 - currentTex.y to account for Blender/OpenGL disconnect in coordinate styles

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNormal.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
    }

}
