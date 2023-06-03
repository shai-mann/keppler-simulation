package main;

import engine.graphics.loading.Loader;
import engine.graphics.rendering.MasterRenderer;
import engine.io.Input;
import entity.Camera;
import entity.Light;
import entity.Planet;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;
import utils.maths.MathUtils;

import java.util.ArrayList;


public class Game {

    Loader loader = new Loader();

    Camera camera = new Camera();
    Light light = new Light(MathUtils.nVector(10), MathUtils.nVector(1));
    MasterRenderer renderer;

    Window window;

    ArrayList<Planet> planets = new ArrayList<>();

    boolean animate = false;

    public Game(int width, int height, String title) {
        window = new Window(width, height, title, this);

        window.init();
        this.init();

        start(); // start game loop
    }

    // initialize fields for rendering (called by Window once GLFW is ready)
    public void init() {
        renderer = new MasterRenderer(loader);

        Planet.initModel(loader); // load planet model

        // TODO: Object creation

        // TODO:
        /*
        Collisions: Absorb mass into larger planet, shoot off fragments

        Update models/textures for planets

        Suns/Stars

        Planet editing/creation
         - Color
         - mass
         - set to orbit another planet
         - initial velocity
         - idk position obviously

        Particle effects for planet collisions?

        Spaceship to pilot/slingshot

         */
        planets.add(new Planet(MathUtils.nVector(10),
                 100f, new Vector3f(0.05f, -0.07f, 0), 0.01f, renderer));
        planets.add(new Planet(MathUtils.nVector(0), 10000f, 0.1f, renderer));
        planets.add(new Planet(MathUtils.nVector(5), 2000f,
                new Vector3f(0, 0, 0f), 0.05f, renderer));
        for (int i = 0; i < 10; i++) {
            planets.add(new Planet(
                    MathUtils.randVector(50f, -50f), 150f,
                    MathUtils.randVector(0.01f, 0), 0.05f, renderer
            ));
        }

        // Co-orbiting:
//        planets.add(new Planet(new Vector3f(10, 0,0), 4000f,
//                new Vector3f(0, 0.1f, 0), 0.1f, renderer));
//        planets.add(new Planet(new Vector3f(-10, 0, 0), 4000f,
//                new Vector3f(0, -0.1f, 0), 0.1f, renderer));

        // Moon Orbiting Planet:
//        planets.add(new Planet(new Vector3f(10, 0, 0),
//                 100f, new Vector3f(0, 0, 0.00258346666f), 0.01f, renderer));
//        planets.add(new Planet(MathUtils.nVector(0),1000000f, 0.1f, renderer));
//
        planets.get(0).hasGravity = false;
    }

    public void start() {
        // loop continuously and render and update
        while (!window.windowShouldClose())
        {
            // Get the time delta then
            // Update and render
            update(window.getDelta());
            render();

            // update window
            window.update();
        }

        // Dispose the game
        window.dispose();
        renderer.clean();

        System.exit(0); // end program
    }

    // updates movement and other aspects of game objects
    public void update(float timeDelta) {
        camera.move();
        // TODO: place entity updates here
        if (animate) {
            for (Planet p : planets) {
                p.applyGravity(planets);
                p.update();
            }
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_T)) {
//            Planet nearestPlanet = Planet.getNearestPlanet(camera.getPosition(), planets);
            animate = !animate;
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_J)) {
            camera.setRotation(new Vector3f(-10, 223, 0));
        }
    }

    // render game
    public void render() {
        renderer.render(light, camera);
    }
}
