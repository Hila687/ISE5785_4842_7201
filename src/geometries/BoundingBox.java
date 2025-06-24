package geometries;

import primitives.*;

import java.util.List;

public class BoundingBox {
    public final Point min;
    public final Point max;

    /**
     * Constructs a bounding box from two points by computing min and max per axis.
     * @param p1 first point
     * @param p2 second point
     */
    public BoundingBox(Point p1, Point p2) {
        this.min = computeMin(p1, p2);
        this.max = computeMax(p1, p2);
    }

    /**
     * Combines two bounding boxes into one that contains both.
     * @param box1 first box
     * @param box2 second box
     * @return combined bounding box
     */
    public static BoundingBox combine(BoundingBox box1, BoundingBox box2) {
        Point min = computeMin(box1.min, box2.min);
        Point max = computeMax(box1.max, box2.max);
        return new BoundingBox(min, max);
    }

    /**
     * Helper method to compute per-axis min point between two points.
     */
    private static Point computeMin(Point p1, Point p2) {
        return new Point(
                Math.min(p1.getX(), p2.getX()),
                Math.min(p1.getY(), p2.getY()),
                Math.min(p1.getZ(), p2.getZ())
        );
    }

    /**
     * Helper method to compute per-axis max point between two points.
     */
    private static Point computeMax(Point p1, Point p2) {
        return new Point(
                Math.max(p1.getX(), p2.getX()),
                Math.max(p1.getY(), p2.getY()),
                Math.max(p1.getZ(), p2.getZ())
        );
    }




    /**
     * Checks if this bounding box fully contains another bounding box.
     *
     * @param other the other bounding box to check
     * @return true if this box contains the other, false otherwise
     */
    public boolean contains(BoundingBox other) {
        if (other == null) return false;

        return other.getMinX() >= this.getMinX() && other.getMaxX() <= this.getMaxX()
                && other.getMinY() >= this.getMinY() && other.getMaxY() <= this.getMaxY()
                && other.getMinZ() >= this.getMinZ() && other.getMaxZ() <= this.getMaxZ();
    }

    /**
     * Divides the bounding box into two sub-boxes along the longest axis.
     *
     * @return a list containing two subdivided bounding boxes.
     */
    public List<BoundingBox> divide() {
        double lengthX = getMaxX() - getMinX();
        double lengthY = getMaxY() - getMinY();
        double lengthZ = getMaxZ() - getMinZ();

        int axis; // 0=X, 1=Y, 2=Z
        if (lengthX >= lengthY && lengthX >= lengthZ) {
            axis = 0;
        } else if (lengthY >= lengthZ) {
            axis = 1;
        } else {
            axis = 2;
        }

        BoundingBox left, right;

        switch (axis) {
            case 0 -> {
                double mid = getBoundingBoxCenter().getX();
                left = new BoundingBox(min, new Point(mid, getMaxY(), getMaxZ()));
                right = new BoundingBox(new Point(mid, getMinY(), getMinZ()), max);
            }
            case 1 -> {
                double mid = getBoundingBoxCenter().getY();
                left = new BoundingBox(min, new Point(getMaxX(), mid, getMaxZ()));
                right = new BoundingBox(new Point(getMinX(), mid, getMinZ()), max);
            }
            case 2 -> {
                double mid = getBoundingBoxCenter().getY();
                left = new BoundingBox(min, new Point(getMaxX(), getMaxY(), mid));
                right = new BoundingBox(new Point(getMinX(), getMinY(), mid), max);
            }
            default -> throw new IllegalStateException("Unexpected axis value");
        }

        return List.of(left, right);
    }


    /**
     * Checks if a ray intersects this bounding box within a maximum distance.
     * Uses the slab method for efficient intersection testing.
     *
     * @param ray         the ray to test for intersection
     * @param maxDistance the maximum distance from the ray origin to consider
     * @return true if the ray intersects the bounding box within the distance, false otherwise
     */
    public boolean intersects(Ray ray, double maxDistance) {
        Vector dir = ray.getDirection();
        Point origin = ray.getHead();

        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        for (int axis = 0; axis < 3; axis++) {
            double originCoord = getCoord(origin, axis);
            double dirCoord = getCoord(dir, axis);
            double minCoord = getCoord(min, axis);
            double maxCoord = getCoord(max, axis);

            if (dirCoord == 0) {
                // Ray is parallel to the slab
                if (originCoord < minCoord || originCoord > maxCoord)
                    return false;
            } else {
                double t1 = (minCoord - originCoord) / dirCoord;
                double t2 = (maxCoord - originCoord) / dirCoord;

                if (t1 > t2) {
                    double temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                tMin = Math.max(tMin, t1);
                tMax = Math.min(tMax, t2);

                if (tMin > tMax)
                    return false;
            }
        }

        // Optional: make sure the intersection is not beyond the max distance
        return tMin <= maxDistance && tMax >= 0;
    }

    /**
     * Checks if a ray intersects this bounding box without limiting the distance.
     *
     * @param ray the ray to test for intersection
     * @return true if the ray intersects the bounding box
     */
    public boolean intersects(Ray ray) {
        return intersects(ray, Double.POSITIVE_INFINITY);
    }



    /**
     * Helper to extract the X/Y/Z coordinate of a Point or Vector by axis index
     * axis: 0 → x, 1 → y, 2 → z
     */
    private static double getCoord(Object obj, int axis) {
        if (obj instanceof Point p) {
            return switch (axis) {
                case 0 -> -Point.ZERO.subtract(p).dotProduct(new Vector(1, 0, 0));
                case 1 -> -Point.ZERO.subtract(p).dotProduct(new Vector(0, 1, 0));
                case 2 -> -Point.ZERO.subtract(p).dotProduct(new Vector(0, 0, 1));
                default -> throw new IllegalArgumentException("Invalid axis");
            };
        }
        if (obj instanceof Vector v) {
            return switch (axis) {
                case 0 -> v.dotProduct(new Vector(1, 0, 0));
                case 1 -> v.dotProduct(new Vector(0, 1, 0));
                case 2 -> v.dotProduct(new Vector(0, 0, 1));
                default -> throw new IllegalArgumentException("Invalid axis");
            };
        }
        throw new IllegalArgumentException("Unsupported object type");
    }

    /**
     * @return the minimum X coordinate of the bounding box
     */
    public double getMinX() {
        return min.getX();
    }

    /**
     * @return the minimum Y coordinate of the bounding box
     */
    public double getMinY() {
        return min.getY();
    }

    /**
     * @return the minimum Z coordinate of the bounding box
     */
    public double getMinZ() {
        return min.getZ();
    }

    /**
     * @return the maximum X coordinate of the bounding box
     */
    public double getMaxX() {
        return max.getX();
    }

    /**
     * @return the maximum Y coordinate of the bounding box
     */
    public double getMaxY() {
        return max.getY();
    }

    /**
     * @return the maximum Z coordinate of the bounding box
     */
    public double getMaxZ() {
        return max.getZ();
    }

    /**
     * function to get the center of the bounding box
     *
     * @return the Point in the middle of the bounding box
     */
    protected Point getBoundingBoxCenter() {

        return new Point(
                (getMaxX() + getMinX()) / 2,
                (getMaxY() + getMinY()) / 2,
                (getMaxZ() + getMinZ()) / 2
        );
    }

    /**
     * Calculates the Euclidean distance between this bounding box and another.
     * Uses edge-based distance by default.
     *
     * @param other the other bounding box
     * @return the Euclidean distance
     */
    public double boundingBoxDistance(BoundingBox other) {
        return boundingBoxDistance(other, true);
    }

    /**
     * Calculates the squared Euclidean distance between this bounding box and another.
     * Uses edge-based distance by default.
     *
     * @param other the other bounding box
     * @return the squared distance
     */
    public double boundingBoxDistanceSquared(BoundingBox other) {
        return boundingBoxDistanceSquared(other, true);
    }

    /**
     * Calculates the Euclidean distance between this bounding box and another.
     *
     * @param other the other bounding box
     * @param edges true = distance between edges, false = between centers
     * @return the distance
     */
    public double boundingBoxDistance(BoundingBox other, boolean edges) {
        return Math.sqrt(boundingBoxDistanceSquared(other, edges));
    }

    /**
     * Calculates the squared distance between this bounding box and another.
     *
     * @param other the other bounding box
     * @param edges true = distance between edges, false = between centers
     * @return the squared distance
     */
    public double boundingBoxDistanceSquared(BoundingBox other, boolean edges) {
        return edges
                ? boundingBoxDistanceBetweenEdgesSquared(other)
                : boundingBoxDistanceBetweenCentersSquared(other);
    }

    /**
     * Calculates the squared distance between the centers of two bounding boxes.
     *
     * @param other the other bounding box
     * @return the squared center-to-center distance
     */
    private double boundingBoxDistanceBetweenCentersSquared(BoundingBox other) {
        return this.getBoundingBoxCenter().distanceSquared(other.getBoundingBoxCenter());
    }

    /**
     * Calculates the squared distance between edges of two bounding boxes.
     *
     * @param other the other bounding box
     * @return the squared edge-to-edge distance
     */
    private double boundingBoxDistanceBetweenEdgesSquared(BoundingBox other) {
        double dx = Math.max(0, Math.max(this.getMinX() - other.getMaxX(), other.getMinX() - this.getMaxX()));
        double dy = Math.max(0, Math.max(this.getMinY() - other.getMaxY(), other.getMinY() - this.getMaxY()));
        double dz = Math.max(0, Math.max(this.getMinZ() - other.getMaxZ(), other.getMinZ() - this.getMaxZ()));
        return dx * dx + dy * dy + dz * dz;
    }


}
