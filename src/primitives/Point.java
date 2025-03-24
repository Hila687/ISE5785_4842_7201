package primitives;

import java.util.Objects;

/**
 * Represents a point in 3D space using a {@link Double3} to hold the coordinates.
 * Provides operations such as vector subtraction, vector addition, and distance calculations.
 */
public class Point {

    /**
     * The 3D coordinates of the point.
     */
    protected final Double3 xyz;

    /**
     * Constant representing the origin point (0, 0, 0).
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructs a point using a {@link Double3} object.
     *
     * @param xyz the 3D coordinates of the point
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Constructs a point with the specified x, y, and z coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Point(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Subtracts another point from this point, resulting in a vector from the other point to this point.
     *
     * @param other the point to subtract
     * @return the resulting vector
     */
    public Vector subtract(Point other) {
        return new Vector(xyz.subtract(other.xyz));
    }

    /**
     * Adds a vector to this point, returning a new point translated by the vector.
     *
     * @param vector the vector to add
     * @return the resulting point
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     * This method avoids the performance cost of a square root when only relative distance is needed.
     *
     * @param p1 the other point
     * @return the squared distance
     */
    public double distanceSquared(Point p1) {
        double x2x1 = p1.xyz.d1() - xyz.d1();
        double y2y1 = p1.xyz.d2() - xyz.d2();
        double z2z1 = p1.xyz.d3() - xyz.d3();

        return x2x1 * x2x1 + y2y1 * y2y1 + z2z1 * z2z1;
    }

    /**
     * Calculates the Euclidean distance between this point and another point.
     *
     * @param p1 the other point
     * @return the distance
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    /**
     * Returns a string representation of the point.
     *
     * @return a string in the format of the underlying {@link Double3}
     */
    @Override
    public String toString() {
        return "" + xyz;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this point is equal to the other object
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other)
                && Objects.equals(xyz, other.xyz);
    }

    /**
     * Returns a hash code value for the point.
     *
     * @return the hash code based on the {@link Double3} field
     */
    @Override
    public int hashCode() {
        return xyz.hashCode();
    }
}
