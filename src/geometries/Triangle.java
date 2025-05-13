package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a triangle in 3D space.
 * A triangle is a specific type of polygon with exactly three vertices.
 * Inherits from {@link Polygon}.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle from three vertices.
     * The vertices must be ordered in a way that preserves a convex shape.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    /**
     * Finds the intersection points between the triangle and a given ray.
     * First checks intersection with the triangle's plane.
     * Then verifies if the intersection point lies within the triangle boundaries,
     * using edge normal vectors and dot product signs.
     *
     * @param ray the ray to intersect with the geometry
     * @return a list of intersection points (at most one), or {@code null} if no intersection occurs
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        // First, check intersection with the triangle's supporting plane
        List<Point> intersection = plane.findIntersections(ray);
        if (intersection == null) {
            return null; // No intersection with the plane → no intersection with triangle
        }

        Point p0 = ray.getHead();      // Ray origin
        Vector dir = ray.getDirection(); // Ray direction

        // Create vectors from ray origin to each of the triangle's vertices
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        // Compute normals to the triangle edges (via cross product)
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Compute dot products of ray direction with each normal
        double s1 = alignZero(dir.dotProduct(n1));
        double s2 = alignZero(dir.dotProduct(n2));
        double s3 = alignZero(dir.dotProduct(n3));

        // Check if all dot products have the same sign (point lies inside triangle)
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return intersection; // Valid intersection within triangle boundaries
        }

        return null; // Intersection point is outside the triangle
    }
}
