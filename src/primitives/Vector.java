package primitives;

/**
 * Represents a vector in 3D space.
 * Inherits from {@link Point} but restricts the value to be non-zero.
 * Provides common vector operations such as addition, scaling, dot product,
 * cross product, normalization, and length calculation.
 */
public class Vector extends Point {

    /**
     * Constructs a vector from individual coordinates.
     * Throws an exception if the zero vector is provided.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }

    /**
     * Constructs a vector from a {@link Double3} object.
     * Throws an exception if the zero vector is provided.
     *
     * @param xyz the 3D values of the vector
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param obj the object to compare to
     * @return {@code true} if both are vectors with equal components
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector vector)
                && xyz.equals(vector.xyz);
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string describing the vector
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns a hash code value for the vector.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Returns the squared length (magnitude) of the vector.
     * More efficient than {@link #length()} since it avoids computing a square root.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return xyz.d1() * xyz.d1() + xyz.d2() * xyz.d2() + xyz.d3() * xyz.d3();
    }

    /**
     * Returns the length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Scales the vector by a scalar value.
     *
     * @param scalar the scalar to multiply with
     * @return a new scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other the vector to add
     * @return the resulting vector
     */
    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    /**
     * Normalizes the vector (makes it of length 1).
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        double length = length();
        return new Vector(xyz.scale(1.0 / length));
    }

    /**
     * Calculates the dot product of this vector and another.
     *
     * @param other the other vector
     * @return the dot product
     */
    public double dotProduct(Vector other) {
        return xyz.d1() * other.xyz.d1()
                + xyz.d2() * other.xyz.d2()
                + xyz.d3() * other.xyz.d3();
    }

    /**
     * Calculates the cross product of this vector and another.
     *
     * @param other the other vector
     * @return the resulting vector perpendicular to both
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                xyz.d2() * other.xyz.d3() - xyz.d3() * other.xyz.d2(),
                xyz.d3() * other.xyz.d1() - xyz.d1() * other.xyz.d3(),
                xyz.d1() * other.xyz.d2() - xyz.d2() * other.xyz.d1()
        );
    }
}
