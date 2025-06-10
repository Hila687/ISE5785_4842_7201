package geometries;

import lighting.LightSource;
import primitives.*;

import java.util.List;
import java.util.Objects;

/**
 * Intersectable represents an abstract geometric object that can be intersected by rays.
 * It defines the interface and shared logic for finding intersections.
 * Concrete classes (like Sphere, Triangle, etc.) must implement the intersection logic.
 */
public abstract class Intersectable {

    /**
     * Finds the intersection points (as Point objects) of a ray with this geometry.
     * This method is a wrapper that returns only the points, not full intersection details.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or null if none were found
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Helper method that must be implemented by subclasses.
     * Calculates the detailed intersections between a ray and this geometry.
     *
     * @param ray the ray to intersect with
     * @return list of Intersection objects, or null if there are none
     */
    protected  List<Intersection> calculateIntersectionsHelper(Ray ray)
    {
        // Calls the main intersection calculation method with no distance limit
        return calculateIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Calculates full intersection data (geometry, point, material, etc.).
     * Calls a helper method implemented by subclasses.
     *
     * @param ray the ray to check
     * @return a list of intersections, or null if none
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Calculates full intersection data with a maximum distance constraint.
     * This is useful for performance optimizations like shadows and transparency.
     *
     * @param ray the ray to check
     * @param maxDistance the maximum allowed distance for intersections
     * @return a list of intersections within the given distance, or null if none
     */
    public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
        return calculateIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Abstract helper method that calculates intersections up to a max distance.
     * Must be implemented by all geometries that support intersection logic.
     *
     * @param ray the ray to check
     * @param maxDistance the maximum allowed distance for intersections
     * @return a list of intersections or null if none
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Intersection represents a detailed result of a ray intersecting a geometry.
     * It stores not only the point and the geometry, but also lighting-related fields
     * required for computing shading (added in Stage 6).
     */
    public static class Intersection {
        /** The geometry object that was intersected */
        public final Geometry geometry;

        /** The intersection point on the geometry */
        public final Point point;

        /** The material at the intersection point (null if geometry is null) */
        public final Material material;

        /** The direction vector of the incoming ray */
        public Vector v;

        /** The normal vector at the intersection point */
        public Vector normal;

        /** Dot product between the normal and the ray direction (v) */
        public double nv;

        /** The light source currently being evaluated */
        public LightSource light;

        /** Vector from the light source to the intersection point */
        public Vector lightDirection;

        /** Dot product between the normal and the light vector */
        public double nl;

        /**
         * Constructs an Intersection object with the given geometry and point.
         * Also stores the material of the geometry if available.
         *
         * @param geometry the geometry that was intersected
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
            this.material = geometry == null ? null : geometry.getMaterial();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof Intersection that)) return false;
            return Objects.equals(geometry, that.geometry) && Objects.equals(point, that.point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
