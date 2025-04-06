package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;
import java.util.LinkedList;

import static primitives.Util.*;

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
        Vector v = ray.getDirection();         // Ray direction
        Point p0 = ray.getP0();          // Ray origin
        Vector va = axis.getDirection();    // Tube axis direction
        Point pa = axis.getP0();      // Tube axis origin

        Vector deltaP;
        try {
            deltaP = p0.subtract(pa);
        } catch (IllegalArgumentException e) {
            deltaP = new Vector(0, 0, 0);
        }

        // Project v and deltaP onto the axis
        double vVa = v.dotProduct(va);
        Vector vPerp = vVa == 0 ? v : v.subtract(va.scale(vVa));

        double dPVa = deltaP.dotProduct(va);
        Vector dPerp = dPVa == 0 ? deltaP : deltaP.subtract(va.scale(dPVa));

        double A = alignZero(vPerp.lengthSquared());
        double B = alignZero(2 * vPerp.dotProduct(dPerp));
        double C = alignZero(dPerp.lengthSquared() - radius * radius);

        double discriminant = alignZero(B * B - 4 * A * C);

        if (discriminant < 0 || isZero(A)) return null;

        double sqrtDisc = Math.sqrt(discriminant);
        double t1 = alignZero((-B + sqrtDisc) / (2 * A));
        double t2 = alignZero((-B - sqrtDisc) / (2 * A));

        List<Point> intersections = new LinkedList<>();
        if (t1 > 0) intersections.add(ray.getPoint(t1));
        if (t2 > 0 && !isZero(t2 - t1)) intersections.add(ray.getPoint(t2));

        return intersections.isEmpty() ? null : intersections;
    }

}
