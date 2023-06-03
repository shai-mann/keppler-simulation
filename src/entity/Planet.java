package entity;

import engine.graphics.loading.Loader;
import engine.graphics.loading.ObjectLoader;
import engine.graphics.models.TexturedModel;
import engine.graphics.rendering.MasterRenderer;
import engine.graphics.textures.ModelTexture;
import org.lwjglx.util.vector.Vector3f;
import utils.maths.MathUtils;

import java.util.ArrayList;

public class Planet extends GameObject {

    static TexturedModel planetModel;
//    static float G = 0.0000000000667430f;
    static float G = 0.000066743f;
    static int SPHERE_RADIUS = 24;

    private Vector3f velocity;

    public float mass;
    public boolean hasGravity = true;
    public boolean collided = false;

    public Planet(Vector3f position, float mass, float scale, MasterRenderer renderer) {
        super(planetModel, position, 90, 0, 0, scale, renderer);

        this.mass = mass;
        this.velocity = MathUtils.nVector(0);
    }

    public Planet(Vector3f position, float mass, Vector3f velocity, float scale, MasterRenderer renderer) {
        this(position, mass, scale, renderer);

        this.mass = mass;
        this.velocity = velocity;
    }

    public void update() {
        // TODO: add collision detection?
        if (collided) {
//            velocity.negate();
            collided = false;
        }

        this.translate(); // update based on new gravitational pulls
    }

    /* GRAVITATIONAL METHODS */

    public void applyGravity(ArrayList<Planet> planets) {
        if (!this.hasGravity) {
            return;
        }

        for (Planet p : planets) {
            if (!p.equals(this)) {
                this.translateVelocity(p.getGravitation(this));
            }
        }
        System.out.println(velocity);
    }

    // returns gravitational pull (as vector) on given planet
    public Vector3f getGravitation(Planet p) {
        Vector3f distance = MathUtils.subtract(this.getPosition(), p.getPosition());
        System.out.println("distance: " + distance);

        if (distance.length() < SPHERE_RADIUS * this.getScale()) {
            // destroy self
            System.out.println("collided");
            this.collided = true;
            p.collided = true;
            return MathUtils.nVector(0);
        }

        float r2 = distance.lengthSquared();
        float a = G * mass / r2;

        distance.normalise().scale(a);
        return distance;
    }

    // adds given velocity vector onto current velocity vector
    private void translateVelocity(Vector3f addedVelocity) {
        this.velocity.translate(addedVelocity.x, addedVelocity.y, addedVelocity.z);
    }

    // translates this planets position by its current velocity
    private void translate() {
        super.translate(velocity);
    }

    /* STATIC METHODS */

    public static Planet getNearestPlanet(Vector3f position, ArrayList<Planet> planets) {
        Planet nearestPlanet = null;
        float nearestDistance = 0;

        for (Planet p : planets) {
            float distance = MathUtils.distance(p.getPosition(), position);
            if (nearestPlanet == null || distance < nearestDistance) {
                nearestDistance = distance;
                nearestPlanet = p;
            }
        }
        return nearestPlanet;
    }

    public static void initModel(Loader loader) {
        planetModel = new TexturedModel(
                ObjectLoader.loadObjModel("/models/planet.obj", loader),
                new ModelTexture(loader.loadTexture("/textures/stallTexture.png"))
        );
    }

}
