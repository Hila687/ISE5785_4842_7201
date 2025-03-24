package geometries;

/**
 * Represents a radial geometry — a geometric shape defined by a radius,
 * such as spheres, tubes, and cylinders.
 * This is an abstract base class for all radial geometries.
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * The radius of the geometry.
     */
    protected final double radius;

    /**
     * Constructs a radial geometry with the specified radius.
     *
     * @param radius the radius of the geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
