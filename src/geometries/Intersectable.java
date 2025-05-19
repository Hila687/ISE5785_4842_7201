package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * Intersectactable interface represents a geometric shape that can be intersected by a ray.
 */
public abstract class Intersectable {
    /**
     * Finds the intersections of a ray with the geometry.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or an empty list if no intersections were found
     */
    public final List<Point> findIntersections(Ray ray) {
        var list = calculateIntersections(ray);
        return list == null ? null : list.stream().map(intersection -> intersection.point).toList();
    }

    /**
     * Calculates the intersections between a ray and this geometry.
     * This method must be implemented by all concrete geometries.
     *
     * @param ray the ray to intersect with
     * @return list of intersection objects, or null if there are none
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);



    /**
     * Calculates the intersections of a ray with the geometry.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersections, or an empty list if no intersections were found
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        List<Intersection> intersections = calculateIntersectionsHelper(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        return intersections;
    }
    /**
     * intersection class represents an intersection between a ray and a geometry.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        /**
         * Constructor to create an intersection object.
         *
         * @param geometry the geometry that was intersected
         * @param point    the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
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
