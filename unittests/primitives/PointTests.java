package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class.
 *
 * @author Hila Rosental & Hila Miller
 */
class PointTests {
    /** Test point #1 */
    Point P1 = new Point(1, 2, 3);

    /** Test point #2 */
    Point P2 = new Point(2, 4, 6);

    /** Test vector for basic operations */
    Vector V1 = new Vector(1, 2, 3);

    /** Acceptable floating point error for assertions */
    private static final double ACCURACY = 1e-10;

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: subtract point (1, 2, 3) from point (2, 4, 6) = (1, 2, 3)
        assertEquals(V1, P2.subtract(P1), "Subtract() for two points does not produce the correct vector");
    }

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtractZeroVector() {
        // =============== Boundary Values Tests ==================
        // TC11: Subtracting a point from itself should throw an exception
        assertThrows(IllegalArgumentException.class,
                () -> P1.subtract(P1),
                "Subtracting a point from itself does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        Point P3 = P1.add(V1);

        // TC01: add vector (1, 2, 3) to point (1, 2, 3) = (2, 4, 6)
        assertEquals(P2, P3, "Point addition did not produce the expected result");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        double distanceSquared = P1.distanceSquared(P2);

        // TC01: distanceSquared between points (1, 2, 3) and (2, 4, 6) = 14
        assertEquals(14, distanceSquared,ACCURACY, "Distance squared between points is incorrect");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        double distance = P1.distance(P2);

        // TC01: distance between points (1, 2, 3) and (4, 6, 9) = sqrt(14)
        assertEquals(Math.sqrt(14), distance, ACCURACY,"Distance between points is incorrect");
    }
}