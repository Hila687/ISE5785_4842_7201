package primitives;


import static primitives.Util.isZero;

/**
 * Represents a 3D vector in space, defined by its direction and magnitude.
 * Inherits coordinate storage from {@link Point} (internally a {@link Double3}),
 * but semantically a vector represents direction and not a location.
 */
public class Vector extends Point {

    /**
     * Constant unit vector in the X direction (1, 0, 0).
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);

    /**
     * Constant unit vector in the Y direction (0, 1, 0).
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);

    /**
     * Constant unit vector in the Z direction (0, 0, 1).
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * Constructs a vector from individual x, y, z components.
     * Throws an exception if the zero vector (0, 0, 0) is passed,
     * since a vector must have direction and magnitude.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @throws IllegalArgumentException if all components are zero
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Constructs a vector from a {@link Double3} object.
     * Throws an exception if the vector is the zero vector.
     *
     * @param xyz the vector components as a {@link Double3}
     * @throws IllegalArgumentException if the vector is the zero vector
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be the zero vector");
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector vector)) return false;
        return  xyz.equals(vector.xyz);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Computes the squared length (magnitude) of the vector.
     * Useful for comparisons and efficiency (avoids square root).
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return xyz.d1() * xyz.d1()
                + xyz.d2() * xyz.d2()
                + xyz.d3() * xyz.d3();
    }

    /**
     * Computes the Euclidean length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns a new vector scaled by the given scalar.
     * The direction is preserved and the length is multiplied by the scalar.
     *
     * @param scalar the scaling factor
     * @return a new scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    /**
     * Adds another vector to this vector and returns the resulting vector.
     *
     * @param other the vector to add
     * @return a new vector representing the sum
     */
    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    /**
     * Normalizes this vector to a unit vector (length = 1).
     *
     * @return a new normalized vector
     */
    public Vector normalize() {
        double length = length();
        return new Vector(xyz.scale(1.0 / length));
    }

    /**
     * Computes the dot product of this vector with another.
     * The result is a scalar representing the cosine of the angle between them,
     * scaled by their magnitudes.
     *
     * @param other the other vector
     * @return the dot product (scalar)
     */
    public double dotProduct(Vector other) {
        return xyz.d1() * other.xyz.d1()
                + xyz.d2() * other.xyz.d2()
                + xyz.d3() * other.xyz.d3();
    }

    /**
     * Computes the cross product of this vector with another.
     * The result is a new vector that is perpendicular to both inputs.
     *
     * @param other the other vector
     * @return a new vector perpendicular to this and the other vector
     */
    public Vector crossProduct(Vector other) {
        return new Vector(
                xyz.d2() * other.xyz.d3() - xyz.d3() * other.xyz.d2(),
                xyz.d3() * other.xyz.d1() - xyz.d1() * other.xyz.d3(),
                xyz.d1() * other.xyz.d2() - xyz.d2() * other.xyz.d1()
        );
    }

    public Vector createOrthogonal() {
        if (!isZero(this.xyz.d1()) || !isZero(this.xyz.d2())) {
            return this.crossProduct(AXIS_Z);
        } else {
            return this.crossProduct(AXIS_X);
        }
    }
}


