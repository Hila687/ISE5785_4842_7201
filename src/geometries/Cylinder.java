package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a cylinder in 3D space.
 * A cylinder is defined by a central axis (as a ray), a radius (inherited from Tube), and a finite height.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a cylinder with the specified radius, axis ray, and height.
     *
     * @param radius the radius of the cylinder
     * @param axis the central axis of the cylinder, represented as a ray
     * @param height the finite height of the cylinder
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }

    /**
     * Returns the normal to the cylinder.
     *
     * @return the normal to the cylinder
     */
    @Override
    public Vector getNormal(Point p) {
        double t;

        try {
            // Compute projection of vector (p - p0) on the cylinder axis direction
            // This gives the height coordinate (t) of point p along the axis
            t = axis.getDirection().dotProduct(p.subtract(axis.getP0()));
        } catch (IllegalArgumentException e) {
            // If p is exactly at axis.origin(), the subtraction creates a zero vector
            // This happens when p is the center of the bottom base
            return axis.getDirection().scale(-1); // Return bottom base normal (opposite to axis)
        }

        final double eps = 1e-10; // Small epsilon to allow floating-point tolerance

        // If t is very close to 0 → point is on the bottom base
        if (Math.abs(t) <= eps) {
            return axis.getDirection().scale(-1); // Normal points down
        }

        // If t is very close to height → point is on the top base
        if (Math.abs(t - height) <= eps) {
            return axis.getDirection(); // Normal points up
        }

        // Otherwise, the point is on the side surface – use Tube logic
        return super.getNormal(p);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

}

