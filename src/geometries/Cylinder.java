package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
            t = axis.direction().dotProduct(p.subtract(axis.origin()));
        } catch (IllegalArgumentException e) {
            // הנקודה היא בדיוק על המקור של הקרן (מרכז הבסיס התחתון)
            return axis.direction().scale(-1);
        }

        final double eps = 1e-10;

        if (Math.abs(t) <= eps) {
            return axis.direction().scale(-1);
        }

        if (Math.abs(t - height) <= eps) {
            return axis.direction();
        }

        return super.getNormal(p);
    }

}

