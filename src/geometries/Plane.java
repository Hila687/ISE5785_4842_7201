package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents an infinite plane in 3D space, defined by a point and a normal vector.
 */
public class Plane extends Geometry {

    /**
     * A point on the plane.
     */
    private final Point q0;

    /**
     * The normal vector to the plane.
     */
    private final Vector normal;

    /**
     * Constructs a plane from three non-collinear points in space.
     * <p><b>Note:</b> This constructor is currently incomplete – it sets the normal to null.</p>
     *
     * @param p1 first point on the plane
     * @param p2 second point on the plane
     * @param p3 third point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        Vector normal = v1.crossProduct(v2).normalize();
        this.normal = normal;
        this.q0 = p1;
    }

    /**
     * Constructs a plane from a point and a normal vector.
     *
     * @param q0 a point on the plane
     * @param normal the normal vector to the plane (will be normalized)
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector of the plane.
     *
     * @param p1 the point on the plane (ignored, since the normal is constant for a plane)
     * @return the normal vector of the plane
     */
    @Override
    public Vector getNormal(Point p1) {
        return normal;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
