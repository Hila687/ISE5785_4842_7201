package primitives;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space.
 * A ray is defined by a starting point (head) and a normalized direction vector.
 */
public class Ray {

    /**
     * The starting point (origin) of the ray.
     */
    private final Point head;

    /**
     * The direction vector of the ray (normalized).
     */
    private final Vector direction;

    /**
     * Constructs a ray from a starting point and a direction vector.
     * The direction vector is automatically normalized.
     *
     * @param head      the starting point (origin) of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    public Point getHead() {
        return head;
    }

    public Vector getDirection() {
        return direction;
    }

    public Point getPoint(double distance) {
        if (isZero(distance)) {
            return head;
        }
        return head.add(direction.scale(distance)); // head + t * direction
    }

    @Override
    public String toString() {
        return "Ray{" +
                "head=" + head +
                ", direction=" + direction +
                '}';
    }
    public Point findClosestPoint(List<Point> points) {
        if(points == null|| points.isEmpty()) {
            return null;
        }
        double minDistance = Double.MAX_VALUE;
        Point closestPoint = points.get(0);
        for (Point point : points) {
            double distance = point.distanceSquared(head);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }
        return closestPoint;
    }

}
