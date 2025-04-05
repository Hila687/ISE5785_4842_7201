package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
     * @param p the point on the surface of the tube
     * @return the normal vector at the given point (currently {@code null})
     */
    @Override
    public Vector getNormal(Point p) {
        //projection of p-o on the ray
        double t = p.subtract( axis.getP0()).dotProduct(axis.getDirection());// (p-po)*v(t=u*v)
        return p.subtract(axis.getP0().add(axis.getDirection().scale(t))).normalize();// p-(po+u*v)
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
