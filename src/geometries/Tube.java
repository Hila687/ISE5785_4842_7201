package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents an infinite tube in 3D space.
 * A tube is defined by a central axis (represented as a {@link Ray}) and a constant radius.
 * Inherits the radius from {@link RadialGeometry}.
 */
public class Tube extends RadialGeometry {

    /**
     * The central axis ray of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with the specified radius and axis.
     *
     * @param radius the radius of the tube
     * @param axis   the central axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }


    @Override
    public Vector getNormal(Point p) {

        if (axis.getHead().equals(p))
            throw new IllegalArgumentException("Point cannot be the head of the axis");

        // Compute projection of vector (p - p0) onto axis direction
        double t = p.subtract(axis.getHead()).dotProduct(axis.getDirection());

        if (isZero(t)) {
            // If t is zero, the point is directly on the axis head
            return p.subtract(axis.getHead()).normalize();
        }

        // Compute point on axis: p0 + t * v
        Point o = axis.getHead().add(axis.getDirection().scale(t));

        // Return the normalized vector from axis to point
        return p.subtract(o).normalize();
    }



    /**
     * Computes intersection distances (t values) between the ray and the infinite tube surface.
     *
     * @param ray the ray to intersect
     * @return list of valid t values (positive only), or empty list if no intersection
     */
    private List<Double> findTubeIntersectionTs(Ray ray) {
        Point P0 = ray.getHead();           // Ray origin
        Vector v = ray.getDirection();      // Ray direction

        Point Pa = axis.getHead();          // Tube axis origin
        Vector Va = axis.getDirection();    // Tube axis direction

        double A, B, C;

        // Compute the vector: v - (v ⋅ Va) * Va
        Vector VecA = v;
        double Vva = v.dotProduct(Va);

        try {
            if (!isZero(Vva))
                VecA = v.subtract(Va.scale(Vva));

            A = VecA.lengthSquared();
        } catch (IllegalArgumentException ex) {
            return List.of(); // Ray is parallel to tube axis → no intersection
        }

        try {
            Vector deltaP = P0.subtract(Pa);
            Vector deltaPMinusVa = deltaP;
            double deltaPva = deltaP.dotProduct(Va);

            if (!isZero(deltaPva))
                deltaPMinusVa = deltaP.subtract(Va.scale(deltaPva));

            B = 2 * VecA.dotProduct(deltaPMinusVa);
            C = deltaPMinusVa.lengthSquared() - radius * radius;
        } catch (IllegalArgumentException ex) {
            B = 0;
            C = -1 * radius * radius;
        }

        // Solve the quadratic equation At² + Bt + C = 0
        double discriminant = alignZero(B * B - 4 * A * C);
        if (discriminant <= 0) return List.of(); // No real solutions

        double sqrtDisc = Math.sqrt(discriminant);
        double t1 = alignZero((-B + sqrtDisc) / (2 * A));
        double t2 = alignZero((-B - sqrtDisc) / (2 * A));

        List<Double> result = new LinkedList<>();
        if (t1 > 0) result.add(t1);
        if (t2 > 0 && !isZero(t2 - t1)) result.add(t2); // Avoid duplicates
        return result;
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Double> ts = findTubeIntersectionTs(ray);
        List<Intersection> intersections = new LinkedList<>();

        for (double t : ts) {
            if (alignZero(t - maxDistance) <= 0) {
                intersections.add(new Intersection(this, ray.getPoint(t)));
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }

}
