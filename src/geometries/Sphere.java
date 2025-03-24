package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a sphere with the specified radius and center point.
     *
     * @param radius the radius of the sphere
     * @param center the center point of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector to the sphere at the specified point on its surface.
     * <p><b>Note:</b> This method is currently not implemented and returns {@code null}.</p>
     *
     * @param p1 the point on the surface of the sphere
     * @return the normal vector at the given point (currently {@code null})
     */
    @Override
    public Vector getNormal(Point p1) {
        return null; // Should return (p1 - center).normalize()
    }
}
