package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Plane represents an infinite plane in 3D space, defined by a point and a normal vector.
 * A plane can be constructed using a point and a normal, or three non-collinear points in space.
 * The class extends {@link Geometry}, and implements the method to get the normal vector
 * as well as intersection logic with a {@link Ray}.
 */
public class Plane extends Geometry {

    /**
     * A reference point on the plane (not necessarily unique).
     */
    private final Point q0;

    /**
     * The normal vector of the plane. It is normalized during construction.
     */
    private final Vector normal;

    /**
     * Constructs a plane from three non-collinear points in space.
     * The normal is computed as the normalized cross-product of the vectors defined by the points.
     *
     * @param p1 first point on the plane
     * @param p2 second point on the plane
     * @param p3 third point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Vector normal = v1.crossProduct(v2).normalize(); // Normal perpendicular to the plane passing through the three points
        this.normal = normal;
        this.q0 = p1;
    }

    /**
     * Constructs a plane from a point and a normal vector.
     * The normal vector is normalized to ensure consistency.
     *
     * @param q0     a point on the plane
     * @param normal the normal vector to the plane (will be normalized)
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }


    @Override
    public Vector getNormal(Point p1) {
        return normal;
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();

        Vector q0ToP0;

        try {
            q0ToP0 = q0.subtract(p0); // Vector from ray origin to plane point (Q0 - P0)
        } catch (IllegalArgumentException e) {
            // Special case: the ray starts exactly on the plane reference point
            return null;
        }

        // Calculate numerator and denominator of the plane intersection formula
        double numerator = alignZero(normal.dotProduct(q0ToP0));
        double denominator = alignZero(normal.dotProduct(dir));

        // If denominator is zero → the ray is parallel to the plane
        if (isZero(denominator)) {
            return null;
        }

        // Calculate t (scalar for ray direction)
        double t = alignZero(numerator / denominator);

        // If t <= 0 → the intersection is behind the ray's origin or exactly at it
        if (t <= 0) {
            return null;
        }

        // Calculate the actual intersection point on the ray
        Point intersection = ray.getPoint(t);
        return List.of(intersection);
    }
}
