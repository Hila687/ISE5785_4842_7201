package primitives;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class VectorTests {
    private static final double ACCURACY = 1e-10;
    Vector v1 = new Vector(1, 2, 3);
    Vector v2 = new Vector(2, 3, 4);

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     */
    @Test
    void testZeroVectorCreation() {
        // =============== Boundary Values Tests ==================
        // TC11: Constructing a zero vector should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(0, 0, 0),
                "Constructor allows creation of zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: lengthSquared of vector (1, 2, 3) = 14
        assertEquals(14, v1.lengthSquared(), "lengthSquared() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: length of vector (1, 2, 3) = 3.7416573867739413
        assertEquals(3.7416573867739413, v1.length(), "length() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: scale of vector (1, 2, 3) by 2 = (2, 4, 6)
        Vector v3 = v1.scale(2);
        assertEquals(new Vector(2, 4, 6), v3, "Scale() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScaleZero() {
        // =============== Boundary Values Tests ==================

        // TC11: scaling vector by 0 should throw exception
        assertThrows(IllegalArgumentException.class,
                () -> v1.scale(0),
                "Scaling by 0 should throw an exception (zero vector is not allowed)");
    }


    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: add vector (1, 2, 3) to vector (2, 3, 4) = (3, 5, 7)
        Vector v3 = v1.add(v2);
        assertEquals(new Vector(3, 5, 7), v3, "Add() wrong value");
    }

    /**
     * Test method for {@link Vector#normalize()} .
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: normalize vector (1, 2, 3) = (0.2672612419124244, 0.5345224838248488, 0.8017837257372732)
        Vector v3 = v1.normalize();
        assertEquals(new Vector(0.2672612419124244, 0.5345224838248488, 0.8017837257372732), v3, "Normalize() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: dotProduct of vector (1, 2, 3) and vector (2, 3, 4) = 20

        assertEquals(20, v1.dotProduct(v2), "DotProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: crossProduct of vector (1, 2, 3) and vector (2, 3, 4) = (-1, 2, -1)
        Vector vr = v1.crossProduct(v2);

        // TC01: result orthogonal to operands
        assertEquals(0, vr.dotProduct(v1),ACCURACY, "Not orthogonal to first operand");
        assertEquals(0, vr.dotProduct(v2),ACCURACY, "Not orthogonal to second operand");
        assertEquals(new Vector(-1, 2, -1), vr, "CrossProduct() wrong value");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProductParallelVectors() {
        // =============== Boundary Values Tests ==================

        // TC11: crossProduct of parallel vectors should throw exception
        Vector v3 = new Vector(-2, -4, -6); // מקביל ל־v1
        assertThrows(IllegalArgumentException.class,
                () -> v1.crossProduct(v3),
                "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     * (required in VectorTests by course instructions)
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: (3, 5, 7) - (1, 2, 3) = Vector(2, 3, 4)
        Point p1 = new Point(3, 5, 7);
        Point p2 = new Point(1, 2, 3);
        assertEquals(v2, p1.subtract(p2), "Point.subtract() wrong result");
    }

    @Test
    void testSubtractIdenticalPoints() {
        // =============== Boundary Values Tests ==================
        Point p1 = new Point(3, 5, 7);

        // TC11: Subtracting identical points should throw exception (zero vector)
        assertThrows(IllegalArgumentException.class,
                () -> p1.subtract(p1),
                "Subtracting identical points should throw exception (zero vector)");
    }



}