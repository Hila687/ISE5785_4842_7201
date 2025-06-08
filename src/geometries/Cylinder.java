package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Class Cylinder represents a finite cylinder in 3D space.
 * A cylinder is defined by a central axis (as a ray), a radius (inherited from Tube), and a finite height.
 * The class extends {@link Tube}, adding the height field and adapting the normal calculation.
 */
public class Cylinder extends Tube {
    /**
     * The finite height of the cylinder.
     */
    private final double height;

    /**
     * Constructor to create a cylinder using radius, axis ray, and height.
     *
     * @param radius the radius of the cylinder (distance from axis to surface)
     * @param axis   the central axis of the cylinder, represented as a {@link Ray}
     * @param height the height of the cylinder (distance between the two bases)
     */
    public Cylinder(double radius, Ray axis, double height) {
        super(radius, axis);
        this.height = height;
    }


    @Override
    public Vector getNormal(Point p) {
        double t;

        try {
            // Compute the projection of (p - base) onto the axis direction
            // This gives the scalar t: how far along the axis the point lies
            t = axis.getDirection().dotProduct(p.subtract(axis.getHead()));
        } catch (IllegalArgumentException e) {
            // Happens if p == axis.getHead(): center of the bottom base
            // In this case, return the normal pointing down (opposite to axis)
            return axis.getDirection().scale(-1);
        }

        final double eps = 1e-10; // Tolerance for floating-point comparisons

        // If t ≈ 0 → point lies on the bottom base
        if (Math.abs(t) <= eps) {
            return axis.getDirection().scale(-1);
        }

        // If t ≈ height → point lies on the top base
        if (Math.abs(t - height) <= eps) {
            return axis.getDirection();
        }

        // Otherwise → point is on the side surface, use Tube's logic
        return super.getNormal(p);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> result = new LinkedList<>();

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();
        Point axisP0 = axis.getHead(); // bottom base center
        Vector axisDir = axis.getDirection();
        Point axisP1 = axisP0.add(axisDir.scale(height)); // top base center

        boolean rayStartsAtBottomCenter = p0.equals(axisP0);
        boolean rayAlongAxis = rayStartsAtBottomCenter &&
                (axisDir.equals(v) || axisDir.equals(v.scale(-1)));

        // Special case: ray starts at bottom center and goes exactly along the axis direction
        if (rayAlongAxis && axisDir.equals(v)) {
            if (alignZero(axisP1.distance(p0) - maxDistance) <= 0) {
                result.add(new Intersection(this, axisP1));
            }
            return result.isEmpty() ? null : result;
        }

        // === 1. Intersections with the curved side (tube) ===
        List<Intersection> sideHits = super.calculateIntersectionsHelper(ray, maxDistance);
        if (sideHits != null) {
            for (Intersection hit : sideHits) {
                double t = alignZero(hit.point.subtract(axisP0).dotProduct(axisDir));
                if (t >= 0 && t <= height &&
                        alignZero(p0.distance(hit.point) - maxDistance) <= 0) {
                    result.add(new Intersection(this, hit.point));
                }
            }
        }

        // === 2. Intersection with bottom base ===
        if (!rayStartsAtBottomCenter) {
            Plane bottom = new Plane(axisP0, axisDir);
            List<Point> bottomHits = bottom.findIntersections(ray);
            if (bottomHits != null) {
                Point p = bottomHits.getFirst();
                if (!isZero(p.subtract(p0).lengthSquared()) &&
                        alignZero(p.subtract(axisP0).lengthSquared() - radius * radius) <= 0 &&
                        alignZero(p0.distance(p) - maxDistance) <= 0) {
                    result.add(new Intersection(this, p));
                }
            }
        } else if (!rayAlongAxis) {
            // Special case: ray starts at center but not along axis – consider it as hitting bottom
            if (alignZero(p0.distance(axisP0) - maxDistance) <= 0) {
                result.add(new Intersection(this, axisP0));
            }
        }

        // === 3. Intersection with top base ===
        Plane top = new Plane(axisP1, axisDir);
        List<Point> topHits = top.findIntersections(ray);
        if (topHits != null) {
            Point p = topHits.getFirst();
            if (!isZero(p.subtract(p0).lengthSquared()) &&
                    alignZero(p.subtract(axisP1).lengthSquared() - radius * radius) <= 0 &&
                    alignZero(p0.distance(p) - maxDistance) <= 0) {
                result.add(new Intersection(this, p));
            }
        }

        // Sort results by distance from ray origin
        if (result.isEmpty()) return null;

        result.sort(Comparator.comparingDouble(i -> i.point.distance(p0)));
        return result;
    }


}
