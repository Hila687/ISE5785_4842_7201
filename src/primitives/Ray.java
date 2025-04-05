package primitives;

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
     * @param head the starting point (origin) of the ray
     * @param direction the direction vector of the ray
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    public Point getP0() {
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
}
