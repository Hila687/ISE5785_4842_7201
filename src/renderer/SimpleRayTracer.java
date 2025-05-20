package renderer;

import geometries.Intersectable. Intersection;

import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.isZero;

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
        return calcColor(closestIntersection, ray);
    }

    /**
     * Calculates the color at the intersection point.
     *
     * @param intersection the intersection point
     * @return the color at the intersection point
     */
    public Color calcColor(Intersection intersection, Ray ray) {
        // Initialize intersection data
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK; // Return black if preprocessIntersection fails
        }

        // Get the ambient light intensity
        Color ambientIntensity = scene.ambientLight.getIntensity();

        // Get the material's ambient reflection coefficient (ka)
        Double3 ka = intersection.geometry.getMaterial().KA;

        // Calculate the ambient light contribution
        Color ambientLight = ambientIntensity.scale(ka);

        // Add the local lighting effects
        Color localEffects = calcColorLocalEffects(intersection);

        // Combine ambient light and local effects
        return ambientLight.add(localEffects);
    }

    public boolean preprocessIntersection(Intersection intersection, Vector v) {
        if (intersection == null) {
            return false;
        }

        // Initialize the ray direction vector
        intersection.v = v;

        // Initialize the normal vector at the intersection point
        intersection.n = intersection.geometry.getNormal(intersection.point);

        // Calculate the dot product of v and n
        intersection.nv = intersection.n.dotProduct(v);

        // Return false if nv is zero
        return !isZero(intersection.nv);
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        if (intersection == null || lightSource == null) {
            return false;
        }

        // Initialize the light source
        intersection.light = lightSource;

        // Calculate the vector from the light source to the intersection point
        intersection.l = lightSource.getL(intersection.point);

        // Calculate the dot product of l and n
        intersection.nl = intersection.n.dotProduct(intersection.l);

        // Return false if both nv and nl are zero
        return !(isZero(intersection.nv) && isZero(intersection.nl));
    }

    private Color calcColorLocalEffects(Intersection intersection) {
        // Start with the emission color of the geometry
        Color color = intersection.geometry.getEmission();

        // Iterate over all light sources in the scene
        for (LightSource lightSource : scene.lights) {
            // Update light source data in the intersection object
            if (!setLightSource(intersection, lightSource)) {
                continue; // Skip to the next light source if setLightSource fails
            }

            // Calculate the light intensity at the intersection point
            Color lightIntensity = lightSource.getIntensity(intersection.point);

            // Add the contribution of the light source to the color
            color = color.add(lightIntensity.scale(
                    calcDiffusive(intersection).add(calcSpecular(intersection))
            ));
        }

        return color;
    }

    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.n.scale(2 * intersection.nl)).normalize();
        double vr = Math.max(0, -intersection.v.dotProduct(r));
        double specularFactor = Math.pow(vr, intersection.material.nSh);

        return intersection.material.KS.scale(specularFactor);
    }

    private Double3 calcDiffusive(Intersection intersection) {
        double absNl = Math.abs(intersection.nl);
        return intersection.material.KD.scale(absNl);
    }
}
