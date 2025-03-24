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
     * Returns the height of the cylinder.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }
}

