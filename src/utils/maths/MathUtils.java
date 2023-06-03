package utils.maths;

import entity.Camera;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

import java.util.Random;

public class MathUtils {

    private static Random rand = new Random();

    /*
    Creates a transformation matrix from three properties:

    Translation:
        represented by (x, y, z), it is the distance from the origin.
        it can also be distance from another point (perhaps the previous location)

    Rotation:
        represented by (rx, ry, rz), known as Euler rotation format (kinda).
        it describes the angles (in degrees) of transformation on the x, y, and z axes

    Scale:
        represented by one float, this tells the matrix how much to scale the model by.
        if the scale value is 1.0f, then the size of the model is left as is.

     */
    public static Matrix4f createTransformationMatrix(
            Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = Matrix4f.setIdentity(new Matrix4f());

        // translate
        Matrix4f.translate(translation, matrix, matrix);

        // rotate
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

        // scale
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        // rotate
        Matrix4f.rotate(
                (float) Math.toRadians(camera.getPitch()),
                new Vector3f(1, 0, 0),
                viewMatrix, viewMatrix
        );
        Matrix4f.rotate(
                (float) Math.toRadians(camera.getYaw()),
                new Vector3f(0, 1, 0),
                viewMatrix, viewMatrix
        );
        Matrix4f.rotate(
                (float) Math.toRadians(camera.getRoll()),
                new Vector3f(0, 0, 1),
                viewMatrix, viewMatrix
        );

        // transform
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Vector3f nVector(float n) {
        return new Vector3f(n, n, n);
    }

    private static float getRand(float max, float min) {
        return min + rand.nextFloat() * (max - min);
    }

    public static Vector3f randVector(float max, float min) {
        return new Vector3f(getRand(max, min), getRand(max, min), getRand(max, min));
    }

    public static Vector3f subtract(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    // returns distance between two vectors
    public static float distance(Vector3f v1, Vector3f v2) {
        return subtract(v1, v2).length();
    }

    // gets rotation needed for the given object to face the given target
    public static Vector3f face(Vector3f object, Vector3f target) {
//        float rx = Math.atan2()
        return null; // TODO: figure out how to make this work???
    }

}
