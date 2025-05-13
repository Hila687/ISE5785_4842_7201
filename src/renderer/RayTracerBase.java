package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracers.
 * A ray tracer is responsible for calculating the color seen along a ray
 * based on the scene configuration (geometry, lighting, etc.).
 */
public abstract class RayTracerBase {

    /** The scene to be rendered by the ray tracer */
    protected final Scene scene;

    /**
     * Traces a given ray through the scene and returns the resulting color.
     * This method must be implemented by subclasses.
     *
     * @param ray the ray to trace
     * @return the color computed for the ray
     */
    public abstract Color traceRay(Ray ray);

    /**
     * Constructs a ray tracer using the specified scene.
     *
     * @param scene the scene to render
     */
    public RayTracerBase(Scene scene) {
        // Store the scene to be used during ray tracing
        this.scene = scene;
    }
}
