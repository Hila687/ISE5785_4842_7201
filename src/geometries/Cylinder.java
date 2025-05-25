package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Class Cylinder represents a finite cylinder in 3D space.
 * A cylinder is defined by a central axis (as a ray), a radius (inherited from Tube), and a finite height.
 * The class extends {@link Tube}, adding the height field and adapting the normal calculation.
 */
public class Cylinder extends Tube {
    /**
     * The finite height of the cylinder.
     */
    private final double height;

    /**
     * Constructor to create a cylinder using radius, axis ray, and height.
     *
     * @param radius the radius of the cylinder (distance from axis to surface)
     * @param axis   the central axis of the cylinder, represented as a {@link Ray}
     * @param height the height of the cylinder (distance between the two bases)
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }


    @Override
    public Vector getNormal(Point p) {
        double t;

        try {
            // Compute the projection of (p - base) onto the axis direction
            // This gives the scalar t: how far along the axis the point lies
            t = axis.getDirection().dotProduct(p.subtract(axis.getHead()));
        } catch (IllegalArgumentException e) {
            // Happens if p == axis.getHead(): center of the bottom base
            // In this case, return the normal pointing down (opposite to axis)
            return axis.getDirection().scale(-1);
        }

        final double eps = 1e-10; // Tolerance for floating-point comparisons

        // If t ≈ 0 → point lies on the bottom base
        if (Math.abs(t) <= eps) {
            return axis.getDirection().scale(-1);
        }

        // If t ≈ height → point lies on the top base
        if (Math.abs(t - height) <= eps) {
            return axis.getDirection();
        }

        // Otherwise → point is on the side surface, use Tube's logic
        return super.getNormal(p);
    }



}
