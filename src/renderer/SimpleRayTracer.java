package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and returns the color at the intersection point.
     *
     * @param ray the ray to trace
     * @return the color at the intersection point
     */
    @Override
    public Color traceRay(Ray ray) {
        // Find all intersection points of the ray with the scene
        List<Point> intersectionPoints = scene.geometries.findIntersections(ray);
        // Check if there are any intersection points
        if (intersectionPoints == null || intersectionPoints.isEmpty()) {
            return scene.backgroundColor; // Return background color if no intersection
        }
        // Find the closest intersection point
        Point closestPoint = ray.findClosestPoint(intersectionPoints);

        // Calculate the color at the intersection point
        return calcColor(closestPoint);
    }

    /**
     * Calculates the color at a given point in the scene.
     *
     * @param p the point to calculate the color for
     * @return the color at the specified point
     */
    private Color calcColor(Point p){
        // Calculate the color at point p
        return scene.ambientLight.getIntensity();
    }
}
