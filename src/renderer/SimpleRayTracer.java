package renderer;

import geometries.Intersectable. Intersection;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * SimpleRayTracer is a basic implementation of a ray tracer that renders a scene.
 * It extends the RayTracerBase class and provides functionality to trace rays through the scene.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }


    @Override
    public Color traceRay(Ray ray) {
        // Find all intersection points of the ray with the scene
        List<Intersection> intersectionPoints = scene.geometries.calculateIntersections(ray);

        // Check if there are any intersection points
        if (intersectionPoints == null || intersectionPoints.isEmpty()) {
            return scene.backgroundColor; // Return background color if no intersection
        }

        // Find the closest intersection point
        Intersection closestIntersection = ray.findClosestIntersection(intersectionPoints);

        // Calculate the color at the intersection point
        return calcColor(closestIntersection);
    }

    /**
     * Calculates the color at the intersection point.
     *
     * @param intersection the intersection point
     * @return the color at the intersection point
     */
    private Color calcColor(Intersection intersection){
        // Calculate the color at the intersection point
        return scene.ambientLight.getIntensity()
                .add(intersection.geometry.getEmission());
    }
}
