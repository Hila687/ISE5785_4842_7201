package primitives;

import geometries.Intersectable. Intersection;

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

    /**
     * Returns the starting point (head) of the ray.
     * @return the starting point of the ray
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction vector of the ray.
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Returns a point on the ray at a specified distance from the head.
     * If the distance is zero, it returns the head point itself.
     *
     * @param distance the distance from the head to the desired point
     * @return the point on the ray at the specified distance
     */
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ray other)) return false;
        return head.equals(other.head) && direction.equals(other.direction);
    }


    /**
     * Finds the closest point from a list of points.
     * If the list is null, returns null.
     *
     * @param points the list of points to search
     * @return the closest point to the head of the ray, or null if the list is empty
     */
    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty()) return null;
        Intersection closest = findClosestIntersection(
                points.stream().map(p -> new Intersection(null, p)).toList()
        );
        return closest == null ? null : closest.point;
    }

    /**
     * Finds the closest intersection point from a list of intersections.
     * If the list is null or empty, returns null.
     *
     * @param intersections the list of intersections to search
     * @return the closest intersection point to the head of the ray, or null if the list is empty
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        // Check if the list is null or empty; return null if so
        if (intersections == null) {
            return null;
        }

        // Initialize the minimum distance to the maximum possible value
        double minDistance = Double.MAX_VALUE;

        // Assume the first intersection is the closest initially
        Intersection closestIntersection = intersections.getFirst();

        // Iterate over all intersections in the list
        for (Intersection intersection : intersections) {
            // Calculate the squared distance from the ray's head to the current intersection point
            double distance = intersection.point.distanceSquared(head);

            // Update the closest intersection if a closer one is found
            if (distance < minDistance) {
                minDistance = distance;
                closestIntersection = intersection;
            }
        }

        // Return the closest intersection found
        return closestIntersection;
    }
}
