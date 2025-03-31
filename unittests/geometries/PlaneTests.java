package geometries;

import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {

    private static final double ACCURACY = 1e-10;

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct plane creation from three non-collinear points
        assertDoesNotThrow(() ->
                        new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)),
                "Failed to create a valid plane from three non-collinear points");

        // =============== Boundary Values Tests ==================

        // TC11: Two identical points
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 1, 0)),
                "Plane constructor did not throw exception for two identical points");

        // TC12: All three points on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 0), new Point(1, 1, 1), new Point(2, 2, 2)),
                "Plane constructor did not throw exception for collinear points");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: get normal to a known plane
        Plane pl = new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        Vector normal = pl.getNormal(new Point(0, 0, 1));

        // Ensure the normal is normalized
        assertEquals(1, normal.length(), ACCURACY, "Normal is not a unit vector");

        // Ensure the normal is orthogonal to the plane vectors
        Vector v1 = new Point(1, 0, 0).subtract(new Point(0, 0, 1));
        Vector v2 = new Point(0, 1, 0).subtract(new Point(0, 0, 1));

        assertEquals(0, normal.dotProduct(v1), ACCURACY, "Normal is not orthogonal to first vector in the plane");
        assertEquals(0, normal.dotProduct(v2), ACCURACY, "Normal is not orthogonal to second vector in the plane");
    }
}
