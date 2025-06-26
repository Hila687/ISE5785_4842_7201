package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.pow;
import static primitives.Util.alignZero;


/**
 * class represents axis-aligned bounding box, it is used to check if ray is in the area of a geometry
 * by checking if the ray direction come with intersection in the bounding box of the geometry.
 * It means to us that the calculation of all the intersections of the same ray should be taken into account
 *
 * @author Hila and Hila
 */

public class BoundingBox {


    /**
     * static final variables for the axis names
     */
    public final static char X = 'x';
    /**
     * static final variables for the axis names
     */
    public final static char Y = 'y';
    /**
     * static final variables for the axis names
     */
    public final static char Z = 'z';

    private Point min;
    private Point max;


    /**Add commentMore actions
     * Constructs a bounding box from two points by computing min and max per axis.
     * @param p1 first point
     * @param p2 second point
     */
    public BoundingBox(Point p1, Point p2) {
        this.min = computeMin(p1, p2);
        this.max = computeMax(p1, p2);
    }

    /**Add commentMore actions
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
    //region getters

    /**Add commentMore actions
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
     * Function which checks if a ray intersects the bounding region
     *
     * @param ray the ray to check for intersection
     * @return boolean result, true if intersects, false otherwise
     */
    public boolean intersectBV(Ray ray) {
        return intersectBV(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Function which checks if a ray intersects the bounding region
     *
     * @param ray         the ray to check for intersection
     * @param maxDistance the max distance that the ray allowed to go
     * @return boolean result, true if intersects, false otherwise
     */
    public boolean intersectBV(Ray ray, double maxDistance) {
        Point p0 = ray.getHead();
        Vector dirHead = ray.getDirection(); //will cast the Vector to Point


        // the coordinates of the ray direction
        double
                dirHeadX = alignZero(dirHead.getX()),
                dirHeadY = alignZero(dirHead.getY()),
                dirHeadZ = alignZero(dirHead.getZ()),

                // the coordinates of the ray starting point
                rayStartPointX = alignZero(p0.getX()),
                rayStartPointY = alignZero(p0.getY()),
                rayStartPointZ = alignZero(p0.getZ()),

                // define default variables for calculations
                //the t's represent how many times does we need to go with the ray (of length 1) to be in the box
                txMin, txMax,
                tyMin, tyMax,
                tzMin, tzMax;

        // for all 3 axes:
        //
        // calculate the intersection distance t0 and t1
        // (t_Min represent the min and t_Max represent the max)
        //
        //  1. when the values for t are negative, the box is behind the ray (no need to find intersections)
        //  2. if the ray is parallel to an axis it will not intersect with the bounding volume plane for this axis.
        //  3. we first find where the ray intersects the planes defined by each face of the bounding cube,
        //     after that, we find the ray's first and second intersections with the planes.

        if (dirHeadX > 0) {
            txMax = alignZero((getMaxX() - rayStartPointX) / dirHeadX);
            txMin = alignZero((getMinX() - rayStartPointX) / dirHeadX);
        } else if (dirHeadX < 0) {
            txMax = (getMinX() - rayStartPointX) / dirHeadX;
            txMin = alignZero((getMaxX() - rayStartPointX) / dirHeadX);
        } else { // preventing parallel to the x-axis
            if (rayStartPointX >= getMaxX() || rayStartPointX <= getMinX())
                return false;
            txMax = Double.POSITIVE_INFINITY;
            txMin = Double.NEGATIVE_INFINITY;
        }
        if (txMax <= 0) {
            return false; // if value for t_Max is negative, the box is behind the ray.
        }

        if (dirHeadY > 0) {
            tyMax = alignZero((getMaxY() - rayStartPointY) / dirHeadY);
            tyMin = alignZero((getMinY() - rayStartPointY) / dirHeadY);
        } else if (dirHeadY < 0) {
            tyMax = alignZero((getMinY() - rayStartPointY) / dirHeadY);
            tyMin = alignZero((getMaxY() - rayStartPointY) / dirHeadY);
        } else { // preventing parallel to the y-axis
            if (rayStartPointY >= getMaxY() || rayStartPointY <= getMinY())
                return false;
            tyMax = Double.POSITIVE_INFINITY;
            tyMin = Double.NEGATIVE_INFINITY;
        }
        if (tyMax <= 0) {
            return false; // if value for tyMax is negative, the box is behind the ray.
        }
        // cases where the ray misses the cube
        // the ray misses the box when t0x is greater than t1y and when t0y is greater than  t1x
        if ((txMin > tyMax) || (tyMin > txMax)) {
            return false;
        }

        // we find which one of these two points lie on the cube by comparing their values:
        // we simply need to choose the point which value for t is the greatest.
        if (tyMin > txMin)
            txMin = tyMin;
        // we find the second point where the ray intersects the box
        // we simply need to choose the point which value for t is the smallest
        if (tyMax < txMax)
            txMax = tyMax;

        if (dirHeadZ > 0) {
            tzMax = (getMaxZ() - rayStartPointZ) / dirHeadZ;
            tzMin = (getMinZ() - rayStartPointZ) / dirHeadZ;
        } else if (dirHeadZ < 0) {
            tzMax = (getMinZ() - rayStartPointZ) / dirHeadZ;
            tzMin = (getMaxZ() - rayStartPointZ) / dirHeadZ;
        } else { // preventing parallel to the z axis
            if (rayStartPointZ >= getMaxZ() || rayStartPointZ <= getMinZ())
                return false;
            tzMax = Double.POSITIVE_INFINITY;
            tzMin = Double.NEGATIVE_INFINITY;
        }
        if (tzMax <= 0) {
            return false; // if value for tzMax is negative, the box is behind the ray.
        }

        //txs here represent the tighter bound of x and y
        // cases where the ray misses the cube
        // the ray misses the box when t0 is greater than t1z and when t0z is greater than  t1
        if (txMin > tzMax || tzMin > txMax)
            return false;

        return maxDistance > max(txMin, tzMin);
    }

    //region distance metric

    /**
     * Calculates the Euclidean distance between this bounding box and another bounding box.
     * The distance is calculated based on the edges of the bounding boxes by default.
     *
     * @param other The other bounding box to calculate the distance to.
     * @return The Euclidean distance between this bounding box and the other bounding box.
     */
    public double boundingBoxDistance(BoundingBox other) {
        return boundingBoxDistance(other, true);
    }

    /**
     * Calculates the squared Euclidean distance between this bounding box and another bounding box.
     * The squared distance is calculated to avoid the computational cost of a square root operation.
     * The distance is calculated based on the edges of the bounding boxes by default.
     *
     * @param other The other bounding box to calculate the squared distance to.
     * @return The squared Euclidean distance between this bounding box and the other bounding box.
     */
    public double boundingBoxDistanceSquared(BoundingBox other) {
        return boundingBoxDistanceSquared(other, true);
    }

    /**
     * Calculates the Euclidean distance between this bounding box and another bounding box.
     * The distance can be calculated either between the edges of the bounding boxes or their centers.
     *
     * @param other The other bounding box to calculate the distance to.
     * @param edges If true, the distance is calculated between the edges of the bounding boxes.
     *              If false, the distance is calculated between the centers of the bounding boxes.
     * @return The Euclidean distance between this bounding box and the other bounding box.
     */
    public double boundingBoxDistance(BoundingBox other, boolean edges) {
        return Math.sqrt(boundingBoxDistanceSquared(other, edges));
    }

    /**
     * Calculates the squared Euclidean distance between this bounding box and another bounding box.
     * The squared distance is calculated to avoid the computational cost of a square root operation.
     * The distance can be calculated either between the edges of the bounding boxes or their centers.
     *
     * @param other The other bounding box to calculate the squared distance to.
     * @param edges If true, the squared distance is calculated between the edges of the bounding boxes.
     *              If false, the squared distance is calculated between the centers of the bounding boxes.
     * @return The squared Euclidean distance between this bounding box and the other bounding box.
     */
    public double boundingBoxDistanceSquared(BoundingBox other, boolean edges) {
        if (edges)
            return boundingBoxDistanceBetweenEdgesSquared(other);
        return boundingBoxDistanceBetweenCentersSquared(other);
    }

    /**
     * calculate the maximum distance between the edges
     *
     * @param other the other boundingBox
     * @return the distance
     */
    private double boundingBoxDistanceBetweenEdgesSquared( BoundingBox other) {
        return pow(max(
                        this.getMaxX() - other.getMinX(),
                        other.getMaxX() - this.getMinX()),
                2) +
                pow(max(
                                this.getMaxY() - other.getMinY(),
                                other.getMaxY() - this.getMinY()),
                        2) +
                pow(max(
                                this.getMaxZ() - other.getMinZ(),
                                other.getMaxZ() - this.getMinZ()),
                        2);
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
     * function to get the distance between the centers of two bounding boxes
     *
     * @param other - the other bounding box
     * @return the distance between the center of the boxes
     */
    private double boundingBoxDistanceBetweenCentersSquared(BoundingBox other) {
        return this.getBoundingBoxCenter().distanceSquared(other.getBoundingBoxCenter());
    }

    //endregion

    /**
     * finds the longest axis of the bounding box by comparing the ranges of x, y, and z coordinates.
     *
     * @return the static final variable x, y or z depends on who has the longest range
     */
    private char findLongest() {
        double x = getMaxX() - getMinX(), y = getMaxY() - getMinY(), z = getMaxZ() - getMinZ();
        if (x > y && x > z)
            return BoundingBox.X;
        if (y > z)
            return BoundingBox.Y;
        return BoundingBox.Z;
    }

    /**
     * Divides the bounding box along its longest axis and returns a list of two new bounding boxes.
     *
     * @return A list of two BoundingBox objects resulting from the division along the longest axis.
     */
    public List<BoundingBox> divide() {
        char longest = findLongest();

        Point min1 = min;
        Point max1, min2, max2 = max;

        switch (longest) {
            case X -> {
                double midX = (min.getX() + max.getX()) / 2;
                max1 = new Point(midX, max.getY(), max.getZ());
                min2 = new Point(midX, min.getY(), min.getZ());
            }
            case Y -> {
                double midY = (min.getY() + max.getY()) / 2;
                max1 = new Point(max.getX(), midY, max.getZ());
                min2 = new Point(min.getX(), midY, min.getZ());
            }
            case Z -> {
                double midZ = (min.getZ() + max.getZ()) / 2;
                max1 = new Point(max.getX(), max.getY(), midZ);
                min2 = new Point(min.getX(), min.getY(), midZ);
            }
            default -> throw new RuntimeException("Unexpected axis: " + longest);
        }

        return List.of(
                new BoundingBox(min1, max1),
                new BoundingBox(min2, max2)
        );
    }


    /**
     * Checks if this bounding box contains another bounding box.
     *
     * @param other possibly smaller bounding box within this one
     * @return whether other is contained inside this one
     */
    public boolean contain(BoundingBox other) {
        return other != null &&
                getMinX() <= other.getMinX() &&
                getMaxX() >= other.getMaxX() &&
                getMinY() <= other.getMinY() &&
                getMaxY() >= other.getMaxY() &&
                getMinZ() <= other.getMinZ() &&
                getMaxZ() >= other.getMaxZ();

    }
}
