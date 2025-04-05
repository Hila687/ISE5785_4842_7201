package geometries;

import primitives.*;

import java.util.List;

/**
 * Intersectactable interface represents a geometric shape that can be intersected by a ray.
 */
public interface Intersectable {
    /**
     * Finds the intersections of a ray with the geometry.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points, or an empty list if no intersections were found
     */
    List<Point> findIntersections(Ray ray);
}
