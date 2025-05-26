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
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

    /**
     * Finds the full intersection details (geometry, point, etc.) of a ray with this geometry.
     * Uses the subclass's implementation of the helper method and performs null/empty filtering.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of Intersection objects, or null if no intersections were found
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        List<Intersection> intersections = calculateIntersectionsHelper(ray);
        // Return null instead of empty list to match course convention
        return (intersections == null || intersections.isEmpty()) ? null : intersections;
    }

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
        public Vector n;

        /** Dot product between the normal and the ray direction (v) */
        public double nv;

        /** The light source currently being evaluated */
        public LightSource light;

        /** Vector from the light source to the intersection point */
        public Vector l;

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
