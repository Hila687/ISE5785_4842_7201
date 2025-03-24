package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Represents an infinite tube in 3D space.
 * A tube is defined by a central axis (as a ray) and a constant radius.
 */
public class Tube extends RadialGeometry {

    /**
     * The central axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with the specified radius and axis.
     *
     * @param radius the radius of the tube
     * @param axis the central axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at the specified point on its surface.
     * <p><b>Note:</b> This method is currently not implemented and returns {@code null}.</p>
     *
     * @param p1 the point on the surface of the tube
     * @return the normal vector at the given point (currently {@code null})
     */
    @Override
    public Vector getNormal(Point p1) {
        return null; // Should return the normalized vector from the tube surface to its axis
    }
}
