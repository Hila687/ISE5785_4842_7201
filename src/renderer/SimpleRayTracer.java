package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.isZero;

/**
 * SimpleRayTracer is a basic ray tracer that computes the color of a ray
 * using the Phong lighting model (diffuse + specular), including ambient and emission.
 * This implementation handles only local lighting effects (Stage 6).
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructs a SimpleRayTracer with the given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Traces a ray through the scene and returns the resulting color.
     * If no intersection is found, returns the background color.
     *
     * @param ray the ray to trace
     * @return the resulting color
     */
    @Override
    public Color traceRay(Ray ray) {
        // Find all intersection points of the ray with the scene
        List<Intersection> intersectionPoints = scene.geometries.calculateIntersections(ray);

        // If no intersections, return background color
        if (intersectionPoints == null) {
            return scene.backgroundColor;
        }

        // Find the closest intersection point
        Intersection closestIntersection = ray.findClosestIntersection(intersectionPoints);

        // Calculate the color at the intersection point
        return calcColor(closestIntersection, ray);
    }

    /**
     * Calculates the final color at the intersection point.
     * Includes emission, ambient light, and local lighting effects.
     *
     * @param intersection the intersection point
     * @param ray the ray that produced the intersection
     * @return the resulting color
     */
    public Color calcColor(Intersection intersection, Ray ray) {
        // Initialize intersection normal and view direction
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        // Ambient light contribution
        Color ambientIntensity = scene.ambientLight.getIntensity();
        Double3 ka = intersection.geometry.getMaterial().KA;
        Color ambientLight = ambientIntensity.scale(ka);

        // Local lighting (diffuse + specular)
        Color localEffects = calcColorLocalEffects(intersection);

        // Final color = ambient + local
        return ambientLight.add(localEffects);
    }

    /**
     * Prepares intersection data: view vector, normal vector, and their dot product.
     *
     * @param intersection the intersection to process
     * @param v the ray direction vector
     * @return true if valid (not perpendicular), false otherwise
     */
    public boolean preprocessIntersection(Intersection intersection, Vector v) {
        if (intersection == null) {
            return false;
        }

        // Store the ray direction
        intersection.v = v;

        // Compute the normal at the intersection point
        intersection.n = intersection.geometry.getNormal(intersection.point);

        // Compute dot product between ray and normal
        intersection.nv = intersection.n.dotProduct(v);

        // Skip if the ray is perpendicular to the surface
        return !isZero(intersection.nv);
    }

    /**
     * Sets the light source and calculates lighting direction and dot product.
     *
     * @param intersection the current intersection
     * @param lightSource the light source being considered
     * @return true if lighting is applicable, false otherwise
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        if (intersection == null || lightSource == null) {
            return false;
        }

        // Set current light
        intersection.light = lightSource;

        // Compute light direction vector
        intersection.l = lightSource.getL(intersection.point);

        // Compute dot product between normal and light vector
        intersection.nl = intersection.n.dotProduct(intersection.l);

        // Skip if both dot products are zero
        return !(isZero(intersection.nv) && isZero(intersection.nl));
    }

    /**
     * Computes local lighting (diffuse + specular) from all light sources.
     *
     * @param intersection the intersection to shade
     * @return the local lighting color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        // Start with the emission color of the geometry
        Color color = intersection.geometry.getEmission();

        // Loop through all light sources
        for (LightSource lightSource : scene.lights) {
            // Update intersection data for the current light
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            // Check if light and view are on the same side of the surface
            double nv = intersection.n.dotProduct(intersection.v);
            double nl = intersection.nl;
            if (nl * nv <= 0) continue;

            // Get light intensity at the point
            Color lightIntensity = lightSource.getIntensity(intersection.point);

            // Compute diffuse and specular components
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);
            Double3 contribution = diffusive.add(specular);

            // Add scaled light contribution to final color
            color = color.add(lightIntensity.scale(contribution));
        }

        return color;
    }

    /**
     * Computes the specular component using the Phong model.
     *
     * @param intersection the current intersection
     * @return the specular reflection coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        // Compute reflection vector R = L - 2(N • L)N
        Vector r = intersection.l.subtract(intersection.n.scale(2 * intersection.nl)).normalize();

        // Compute the dot product of view direction and reflection vector
        double vr = Math.max(0, -intersection.v.dotProduct(r));

        // Apply shininess exponent and scale
        double specularFactor = Math.pow(vr, intersection.material.nSh);
        return intersection.material.KS.scale(specularFactor);
    }

    /**
     * Computes the diffuse component using Lambert's cosine law.
     *
     * @param intersection the current intersection
     * @return the diffuse reflection coefficient
     */
    private Double3 calcDiffusive(Intersection intersection) {
        // Use the absolute value of the dot product to ensure non-negative light
        double absNl = Math.abs(intersection.nl);
        return intersection.material.KD.scale(absNl);
    }
}
