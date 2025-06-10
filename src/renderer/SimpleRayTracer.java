package renderer;

import geometries.Intersectable;
import geometries.Intersectable.Intersection;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * SimpleRayTracer is a basic ray tracer that computes the color of a ray
 * using the Phong lighting model (diffuse + specular), including ambient and emission.
 * This implementation handles only local lighting effects (Stage 6).
 */
public class SimpleRayTracer extends RayTracerBase {



    /** Maximum recursion depth for color calculations */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /** Minimum value for color calculations to avoid division by zero */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /** Initial value for color calculations, used to avoid zero values */
    private static final Double3 INITIAL_K = Double3.ONE;

    private int softShadowSamples = 15;


    /**
     * Constructs a SimpleRayTracer with the given scene.
     *
     * @param scene the scene to render
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Set the number of samples for soft shadows
     *
     * @param n the number of samples
     * @return the current instance of SimpleRayTracer
     */
    public SimpleRayTracer setSoftShadowSamples(int n) {
        this.softShadowSamples = n;
        return this;
    }

    /**
     * Finds the closest intersection point for a given ray.
     * This method calculates all intersections and determines the closest one.
     *
     * @param ray the ray for which the closest intersection is to be found
     * @return the closest intersection, or null if no intersections exist
     */
    private Intersection findClosestIntersection(Ray ray) {
        // Calculate all intersection points for the ray
        List<Intersection> intersectionPoints = scene.geometries.calculateIntersections(ray);

        // If no intersections, return null
        if (intersectionPoints == null ) {
            return null;
        }

        // Find and return the closest intersection
        return intersectionPoints.stream()
                .min((i1, i2) -> Double.compare(
                        i1.point.distance(ray.getHead()),
                        i2.point.distance(ray.getHead())))
                .orElse(null);
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

        Intersection closestIntersection = findClosestIntersection(ray);
        if (closestIntersection == null) {
            return scene.backgroundColor;
        }

        return calcColor(closestIntersection, ray);
    }





    /**
     * Calculates the final color at a given intersection point.
     * This method is the entry point for color computation and includes:
     * - Local and global lighting effects (recursively)
     * - Ambient light contribution
     * If the intersection cannot be processed, returns black.
     *
     * @param intersection the intersection point on a geometry
     * @param ray the ray that caused the intersection
     * @return the calculated color at the intersection point
     */
    private Color calcColor(Intersection intersection, Ray ray) {
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity()
                        .scale(intersection.geometry.getMaterial().KA))
                : Color.BLACK;
    }


    /**
     * Recursively calculates the color at an intersection point.
     * This method handles the recursive computation of lighting effects,
     * including global effects like reflection and refraction (in later stages),
     * with an attenuation factor and recursion depth limitation.
     *
     * @param intersection the intersection point
     * @param level the current recursion depth
     * @param k the cumulative reflection/refraction attenuation coefficient
     * @return the resulting color at the intersection point
     */
    private Color calcColor(Intersection intersection, int level, Double3 k) {
        if (level == 0 || k.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK;
        }

        Color localEffects = calcColorLocalEffects(intersection);
        Color globalEffects = calcGlobalEffects(intersection, level, k);

        return localEffects.add(globalEffects);
    }




    /**
     * Checks if the intersection point is unshaded (not in shadow).
     * Casts a shadow ray towards the light source and checks for intersections.
     *
     * @param intersection the intersection to check
     * @return true if the point is unshaded, false if it is in shadow
     */
    private boolean unshaded(Intersection intersection) {


        // Flip light direction: from point toward light source
        Vector lightDirection = intersection.lightDirection.scale(-1);

        // Construct a shadow ray from the intersection point toward the light
        // The normal is used as a third argument to slightly shift the origin (to avoid self-shadowing)
        Ray lightRay = new Ray(intersection.point, lightDirection, intersection.normal);

        // Measure the distance from the intersection point to the light source
        double lightDistance = intersection.light.getDistance(intersection.point);

        // Find all geometries intersected by the shadow ray
        List<Intersection> intersections = scene.geometries.calculateIntersections(lightRay, lightDistance);

        // If there are no objects in the way, the point is illuminated
        if (intersections == null) return true;

        // Iterate through all intersections found along the shadow ray
        for (Intersection inter : intersections) {
            double interDistance = inter.point.distance(intersection.point);

            // Check if the intersecting geometry is closer than the light and is not sufficiently transparent
            if (alignZero(interDistance - lightDistance) < 0 &&
                    inter.material.kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false; // There is a blocking object between the point and the light
            }
        }

        // No blocking geometry found → the point is illuminated
        return true;
    }


    /**
     * Calculates the transparency of the intersection point.
     * This method checks if the point is in shadow by casting a shadow ray
     * towards the light source and checking for intersections with other geometries.
     *
     * @param intersection the intersection point to check
     * @return the transparency factor (0 = fully blocked, 1 = fully transparent)
     */
    private Double3 transparency(Intersection intersection) {
        // Calculate the direction from the intersection point to the light source
        Vector lightDirection = intersection.lightDirection.scale(-1);

        // Create a shadow ray with an offset to avoid self-shadowing
        Ray shadowRay = new Ray(intersection.point, lightDirection, intersection.normal);

        // Get all intersections of the shadow ray with the scene
        List<Intersection> shadowIntersections = scene.geometries.calculateIntersections(shadowRay);

        // If no intersections, the point is fully transparent
        if (shadowIntersections == null || shadowIntersections.isEmpty()) {
            return Double3.ONE; // Fully transparent
        }

        Double3 kT = Double3.ONE; // Start with full transparency
        double lightDistance = intersection.light.getDistance(intersection.point);
        // Loop through all shadow ray intersections
        for (Intersection shadowIntersection : shadowIntersections) {
            double intersectionDistance = shadowIntersection.point.distance(intersection.point);

            // Check if the intersection is closer than the light source
            if (alignZero(intersectionDistance - lightDistance) < 0) {
                // Multiply the transparency by the kT of the intersected geometry
                kT = kT.product(shadowIntersection.geometry.getMaterial().kT);

                // If transparency is below the threshold, stop further checks
                if (kT.lowerThan(MIN_CALC_COLOR_K)) {
                    return Double3.ZERO; // Fully blocked
                }
            }
        }

        return kT; // Return the cumulative transparency
    }



    private Double3 transparency(Intersection intersection, int numberOfSamples) {
        if (intersection.light.getRadius() == 0 ||
                intersection.light.getPosition() == null ||
                numberOfSamples <= 1) {
            return transparency(intersection);
        }
        Vector l = intersection.lightDirection;
        Vector vUp = l.createOrthogonal();
        Vector vRight = vUp.crossProduct(l);

        Board board = new Board(
                intersection.light.getPosition(),
                vUp, vRight,
                intersection.light.getRadius() * 2
        ).setCircle(true);

        List<Point> lightSamples = board.getPoints(36 );
        Double3 ktrSum = Double3.ZERO;
        int contributingSamples = 0;

        for (Point areaLightPoint : lightSamples) {
            Vector areaLightDir = areaLightPoint.subtract(intersection.point).normalize();
            if (intersection.nl <= 0)
                continue;
            double lightDist = intersection.point.distance(areaLightPoint);

            Ray shadowRay = new Ray(intersection.point, areaLightDir, intersection.normal);
            List<Intersection> shadowHits = scene.geometries.calculateIntersections(shadowRay, lightDist);
            Double3 ktr = Double3.ONE;
            if (shadowHits != null) {
                for (Intersection hit : shadowHits) {
                    ktr = ktr.product(hit.material.kT);
                    if (ktr.lowerThan(MIN_CALC_COLOR_K)) {
                        ktr = Double3.ZERO;
                        break;
                    }
                }
            }
            ktrSum = ktrSum.add(ktr);
            contributingSamples++;
        }
        if (contributingSamples == 0)
            return Double3.ZERO;
        return ktrSum.reduce(lightSamples.size());
    }

    private boolean unshaded(Intersection intersection, LightSource light) {
        Vector l = light.getL(intersection.point);
        Vector n = intersection.geometry.getNormal(intersection.point);

        Ray shadowRay = new Ray(intersection.point, l, n);
        double lightDistance = light.getDistance(intersection.point);

        var intersections = scene.geometries
                .calculateIntersections(shadowRay, lightDistance);

        return intersections == null || intersections.isEmpty();
    }



    /**
     * Constructs a reflected ray from the given intersection point.
     *
     * @param intersection the intersection point
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v.subtract(intersection.normal.scale(2 * intersection.nv)), intersection.normal);
    }

    /**
     * Constructs a refracted (transparent) ray from the given intersection point.
     *
     * @param intersection the intersection point
     * @return the refracted ray
     */
    private Ray constructRefractedRay(Intersection intersection) {
        return new Ray(intersection.point, intersection.v, intersection.normal);
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
        intersection.normal = intersection.geometry.getNormal(intersection.point);

        // Compute dot product between ray and normal
        intersection.nv = intersection.normal.dotProduct(v);

        // Skip if the ray is perpendicular to the surface
        return !isZero(intersection.nv);
    }


    /**
     * Sets the current light source for the intersection and computes
     * the direction to the light and the dot product with the surface normal.
     * Flips the normal if needed to ensure correct lighting direction.
     *
     * @param intersection the intersection point
     * @param lightSource the light source to consider
     * @return true if lighting is applicable, false otherwise
     */
    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        if (intersection == null || lightSource == null) {
            return false;
        }

        // Set the current light
        intersection.light = lightSource;

        // Compute light direction vector
        intersection.lightDirection = lightSource.getL(intersection.point);

        // Compute dot product between normal and light direction
        intersection.nl = intersection.normal.dotProduct(intersection.lightDirection);

        // Flip the normal if it's facing away from the light
        if (intersection.nl < 0) {
            intersection.normal = intersection.normal.scale(-1);
            intersection.nl = -intersection.nl;
        }

        // Recompute nv after potential normal flip
        intersection.nv = intersection.normal.dotProduct(intersection.v);

        // Skip if both nl and nv are zero (no lighting influence)
        return !(isZero(intersection.nv) && isZero(intersection.nl));
    }


    /**
     * Computes local lighting (diffuse + specular) from all light sources.
     *
     * @param intersection the intersection to shade
     * @return the local lighting color contribution
     */
    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();

        // Loop through all light sources
        for (LightSource lightSource : scene.lights) {
            // Update intersection data for the current light
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }

            Double3 kT;
            // Get the transparency level of the point
            if (intersection.light.getRadius()== 0)
                 kT = transparency(intersection);
            else
                 kT = transparency(intersection, softShadowSamples);

            // Skip this light source if the point is fully blocked
            if (kT.equals(Double3.ZERO)) {
                continue;
            }

            // Check if light and view are on the same side of the surface
            double nv = intersection.normal.dotProduct(intersection.v);
            double nl = intersection.nl;
            if (nl * nv <= 0) continue;

            // Get light intensity at the point
            Color lightIntensity = lightSource.getIntensity(intersection.point);

            // Compute diffuse and specular components
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);
            Double3 contribution = diffusive.add(specular);

            // Add scaled light contribution to final color
            color = color.add(lightIntensity.scale(contribution.product(kT)));
        }

        return color;
    }

    /**
     * Calculates the color of a single global effect (reflection or transparency).
     *
     * @param ray the secondary ray (reflection or transparency)
     * @param level the current recursion depth
     * @param k the cumulative attenuation coefficient
     * @param kx the specific attenuation coefficient for the effect
     * @return the calculated color for the global effect
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) {
            return Color.BLACK; // Stop recursion if the attenuation is too low
        }

        // Find the closest intersection of the secondary ray
        Intersection intersection = findClosestIntersection(ray);
        if (intersection == null) {
            return scene.backgroundColor.scale(kx); // Return background color if no intersection
        }

        // Recursively calculate the color at the intersection point
        return preprocessIntersection(intersection, ray.getDirection())
                ? calcColor(intersection, level - 1, kkx).scale(kx)
                : Color.BLACK;
    }

    /**
     * Calculates the combined color of global effects (reflection and transparency).
     *
     * @param intersection the intersection point
     * @param level the current recursion depth
     * @param k the cumulative attenuation coefficient
     * @return the combined color of reflection and transparency effects
     */
    private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
        return calcGlobalEffect(constructRefractedRay(intersection), level, k, intersection.material.kT)
                .add(calcGlobalEffect(constructReflectedRay(intersection), level, k, intersection.material.kR));
    }

    /**
     * Computes the specular component using the Phong model.
     *
     * @param intersection the current intersection
     * @return the specular reflection coefficient
     */
    private Double3 calcSpecular(Intersection intersection) {
        // Compute reflection vector R = L - 2(N • L)N
        Vector r = intersection.lightDirection.subtract(intersection.normal.scale(2 * intersection.nl)).normalize();

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