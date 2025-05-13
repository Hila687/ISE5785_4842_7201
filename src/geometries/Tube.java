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

    /**
     * Returns the normal vector to the tube at the specified point on its surface.
     * The normal is calculated as the vector from the axis to the point, perpendicular to the axis.
     *
     * @param p the point on the surface of the tube
     * @return the normalized normal vector to the tube at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        // Compute projection of vector (p - p0) onto axis direction
        double t = p.subtract(axis.getHead()).dotProduct(axis.getDirection());

        // Compute point on axis: p0 + t * v
        Point o = axis.getHead().add(axis.getDirection().scale(t));

        // Return the normalized vector from axis to point
        return p.subtract(o).normalize();
    }

    /**
     * Finds the intersection points between a ray and the tube.
     * Uses the analytical solution of a quadratic equation derived from the tube's geometry.
     *
     * @param ray the ray to intersect with the tube
     * @return a list of intersection points, or {@code null} if there are none
     */
    @Override
    public List<Point> findIntersections(Ray ray) {

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
                VecA = v.subtract(Va.scale(Vva)); // Vector component perpendicular to axis

            // A = ||v - (v ⋅ Va)Va||^2
            A = VecA.lengthSquared();
        } catch (IllegalArgumentException ex) {
            // v parallel to Va → A = 0 → no intersections
            return null;
        }

        try {
            // Compute deltaP = P0 - Pa
            Vector DeltaP = P0.subtract(Pa);

            // Compute: DeltaP - (DeltaP ⋅ Va) * Va
            Vector DeltaPMinusDeltaPVaVa = DeltaP;
            double DeltaPVa = DeltaP.dotProduct(Va);

            if (!isZero(DeltaPVa))
                DeltaPMinusDeltaPVaVa = DeltaP.subtract(Va.scale(DeltaPVa));

            // B = 2 * [(v - (v ⋅ Va)Va) ⋅ (DeltaP - (DeltaP ⋅ Va)Va)]
            B = 2 * (VecA.dotProduct(DeltaPMinusDeltaPVaVa));

            // C = ||DeltaP - (DeltaP ⋅ Va)Va||^2 - r^2
            C = DeltaPMinusDeltaPVaVa.lengthSquared() - radius * radius;

        } catch (IllegalArgumentException ex) {
            // Case: DeltaP is zero or colinear → fallback values
            B = 0;
            C = -1 * radius * radius;
        }

        // Solve the quadratic equation: At^2 + Bt + C = 0
        double Disc = alignZero(B * B - 4 * A * C); // Discriminant

        if (Disc <= 0)
            return null; // No real solutions

        double t1, t2;

        // Compute the two roots
        t1 = alignZero((-B + Math.sqrt(Disc)) / (2 * A));
        t2 = alignZero((-B - Math.sqrt(Disc)) / (2 * A));

        List<Point> intersections = new LinkedList<>();

        // Add valid (positive) intersection points
        if (t1 > 0) intersections.add(ray.getPoint(t1));
        if (t2 > 0 && !isZero(t2 - t1)) intersections.add(ray.getPoint(t2)); // Avoid duplicates

        return intersections.isEmpty() ? null : intersections;
    }
}
