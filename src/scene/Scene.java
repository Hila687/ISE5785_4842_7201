package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a 3D scene containing geometries, background color, and ambient light.
 * This class holds the core configuration for rendering.
 */
public class Scene {

    /** The collection of light sources in the scene */
    public List<LightSource> lights = new LinkedList<>();

    /** The name of the scene (used for identification purposes) */
    public final String sceneName;

    /** The collection of geometries contained in the scene */
    public Geometries geometries = new Geometries();

    /** The background color of the scene (default: black) */
    public Color backgroundColor = Color.BLACK;

    /** The ambient light in the scene (default: none) */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * Constructs a new scene with the specified name.
     * @param sceneName the name of the scene
     */
    public Scene(String sceneName) {
        this.sceneName = sceneName;
    }

    /**
     * Sets the background color of the scene.
     * @param backgroundColor the background color to use
     * @return the current Scene object (for method chaining)
     */
    public Scene setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     * @param ambientLight the ambient light to use
     * @return the current Scene object (for method chaining)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries contained in the scene.
     * Replaces the current geometry collection with the provided one.
     * @param geometries the set of geometries to add
     * @return the current Scene object (for method chaining)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * sets the light sources in the scene.
     * @param lights the list of light sources to set
     * @return the current Scene object (for method chaining)
     */
    private Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
