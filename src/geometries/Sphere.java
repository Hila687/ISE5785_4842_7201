package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    protected final Point center;

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
     * @param p the point on the surface of the sphere
     * @return the normal vector at the given point (currently {@code null})
     */
    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        //Vector v = center.subtract(ray.origin());
        Vector v = ray.getDirection();
        Point p0 = ray.getP0();

        //if the ray starts at the center of the sphere

        if (p0.equals(center))
            return List.of(center.add(v.scale(radius)));

        Vector u = center.subtract(p0);
        double tm = alignZero(v.dotProduct(u));
        double d2 = alignZero(u.lengthSquared() - tm * tm);

        if (alignZero(d2 - radius * radius)>0)
            return null;

        double th = alignZero(Math.sqrt(radius * radius - d2));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if (t1 > 0 && t2 > 0)
            return List.of(ray.getPoint(t1), ray.getPoint(t2));

        if (t1 > 0)
            return List.of(ray.getPoint(t1));

        if (t2 > 0)
            return List.of(ray.getPoint(t2));

        return null;
    }
}
