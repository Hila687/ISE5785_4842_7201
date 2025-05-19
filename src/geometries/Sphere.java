package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 * Inherits the radius from {@link RadialGeometry}.
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


    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }


//    @Override
//    public List<Point> findIntersections(Ray ray) {
//        Vector v = ray.getDirection();
//        Point p0 = ray.getHead();
//
//        // Special case: the ray starts exactly at the sphere center
//        if (p0.equals(center))
//            return List.of(center.add(v.scale(radius))); // returns one point on the surface
//
//        Vector u = center.subtract(p0); // vector from ray origin to sphere center
//        double tm = alignZero(v.dotProduct(u)); // projection of u on the ray direction
//        double d2 = alignZero(u.lengthSquared() - tm * tm); // squared distance from center to ray
//
//        // If the distance from the ray to the center is larger than the radius → no intersections
//        if (alignZero(d2 - radius * radius) > 0)
//            return null;
//
//        double th = alignZero(Math.sqrt(radius * radius - d2)); // half chord length
//        double t1 = alignZero(tm - th); // distance to first intersection
//        double t2 = alignZero(tm + th); // distance to second intersection
//
//        // Both points are in front of the ray origin
//        if (t1 > 0 && t2 > 0)
//            return List.of(ray.getPoint(t1), ray.getPoint(t2));
//
//        // Only one point is in front of the ray origin
//        if (t1 > 0)
//            return List.of(ray.getPoint(t1));
//
//        if (t2 > 0)
//            return List.of(ray.getPoint(t2));
//
//        // Both points are behind the ray origin
//        return null;
//    }

    @Override
    public List<Intersection> calculateIntersectionsHelper(Ray ray)
    {
        Vector v = ray.getDirection();
        Point p0 = ray.getHead();

        // Special case: the ray starts exactly at the sphere center
        if (p0.equals(center))
            return List.of(new Intersection(this, center.add(v.scale(radius)))); // returns one point on the surface

        Vector u = center.subtract(p0); // vector from ray origin to sphere center
        double tm = alignZero(v.dotProduct(u)); // projection of u on the ray direction
        double d2 = alignZero(u.lengthSquared() - tm * tm); // squared distance from center to ray

        // If the distance from the ray to the center is larger than the radius → no intersections
        if (alignZero(d2 - radius * radius) > 0)
            return null;

        double th = alignZero(Math.sqrt(radius * radius - d2)); // half chord length
        double t1 = alignZero(tm - th); // distance to first intersection
        double t2 = alignZero(tm + th); // distance to second intersection

        // Both points are in front of the ray origin
        if (t1 > 0 && t2 > 0)
            return List.of(new Intersection(this, ray.getPoint(t1)), new Intersection(this, ray.getPoint(t2)));

        // Only one point is in front of the ray origin
        if (t1 > 0)
            return List.of(new Intersection(this, ray.getPoint(t1)));

        if (t2 > 0)
            return List.of(new Intersection(this, ray.getPoint(t2)));

        // Both points are behind the ray origin
        return null;
    }
}
