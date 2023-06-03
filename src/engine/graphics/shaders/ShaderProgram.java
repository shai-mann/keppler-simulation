package engine.graphics.shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;
import utils.fileio.FileUtils;

import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private final int programID, vertexShaderID, fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        // initialize shaders
        vertexShaderID = ShaderProgram.loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = ShaderProgram.loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram(); // initialize program

        // attach shaders to program
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        bindAttributes();

        // link and validate program
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected abstract void bindAttributes();

    // binds an attribute to a certain location in the program
    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
    }

    protected void loadBoolean(int location, boolean value) {
        loadFloat(location, value ? 1 : 0);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private static int loadShader(String file, int type) {
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, FileUtils.loadAsString(file));
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Shader Compiling: Failed to compile the following shader:");
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.exit(-1); // crash program
        }

        return shaderID;
    }

    // start this shader program
    public void start() {
        GL20.glUseProgram(programID);
    }

    // stop this shader program
    public void stop() {
        GL20.glUseProgram(0);
    }

    // destroy this shader program and its sub-processes
    public void clean() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }
}
