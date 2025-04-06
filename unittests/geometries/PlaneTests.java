package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTests {

    private static final double ACCURACY = 1e-10;
    Point p1 = new Point(0, 0, 1);
    Point p2 = new Point(0, 1, 0);
    Point p3 = new Point(1, 0, 0);
    Point p4 = new Point(1, 1, 1);
    Plane plane = new Plane(p1, p3, p2);


    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructor() {
        // =============== Equivalence Partitions Tests ===============

        // TC01: Valid plane creation from 3 non-collinear points
        assertDoesNotThrow(() -> plane,
                "Failed to create a valid plane from three non-collinear points");

        // TC02: Check that the normal is normalized and orthogonal to plane vectors
        Vector normal = plane.getNormal(p1);

        Vector v1 = p3.subtract(p1);
        Vector v2 = p2.subtract(p1);

        assertEquals(1, normal.length(), ACCURACY, "Normal is not normalized");
        assertEquals(0, normal.dotProduct(v1), ACCURACY, "Normal is not orthogonal to v1");
        assertEquals(0, normal.dotProduct(v2), ACCURACY, "Normal is not orthogonal to v2");

        // ================== Boundary Values Tests ===================

        // TC11: First and second points are identical
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p1, p2),
                "Plane constructor did not throw exception for first and second points being identical");

        // TC12: All three points are on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 0), p4, new Point(2, 2, 2)),
                "Plane constructor did not throw exception for collinear points");

        // TC13: First and third points are identical
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p3, p1),
                "Plane constructor did not throw exception for first and third points being identical");

        // TC14: Second and third points are identical
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p1, p3, p3),
                "Plane constructor did not throw exception for second and third points being identical");

        // TC15: All three points are identical
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(p4, p4, p4),
                "Plane constructor did not throw exception for three identical points");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: get normal to a known plane
        Vector normal = plane.getNormal(new Point(0, 0, 1));

        // Ensure the normal is normalized
        assertEquals(1, normal.length(), ACCURACY, "Normal is not a unit vector");

        // Ensure the normal is orthogonal to the plane vectors
        Vector v1 = p3.subtract(p1);
        Vector v2 = p2.subtract(p1);

        assertEquals(0, normal.dotProduct(v1), ACCURACY, "Normal is not orthogonal to first vector in the plane");
        assertEquals(0, normal.dotProduct(v2), ACCURACY, "Normal is not orthogonal to second vector in the plane");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Plane plane = new Plane(
                new Point(0, 0, 1),
                new Point(1, 0, 1),
                new Point(0, 1, 1)
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the plane
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertNotNull(plane.findIntersections(ray1), "Ray should intersect the plane");

        // TC02: Ray does not intersect the plane
        Ray ray2 = new Ray(new Point(0, 0, 0), new Vector(0, 0, -1));
        assertNull(plane.findIntersections(ray2), "Ray should not intersect the plane");

        // =============== Boundary Values Tests ==================

        // TC11: Ray is parallel to the plane and included in the plane
        Ray ray3 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 0));
        assertNull(plane.findIntersections(ray3), "Ray lies in the plane – no intersection");

        // TC12: Ray is parallel to the plane but not included
        Ray ray4 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray4), "Ray is parallel and not in the plane");

        // TC13: Ray is orthogonal to the plane and starts before
        Ray ray5 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertNotNull(plane.findIntersections(ray5), "Ray is orthogonal and should intersect");

        // TC14: Ray is orthogonal and starts in the plane
        Ray ray6 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray6), "Ray is orthogonal and starts in the plane");

        // TC15: Ray is orthogonal and starts after the plane
        Ray ray7 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray7), "Ray is orthogonal and starts after the plane");

        // TC16: Ray is neither orthogonal nor parallel and starts at the plane (but not contained in it)
        Ray ray8 = new Ray(new Point(0.5, 0.5, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray8), "Ray starts at the plane and goes out, should return null");

        // TC17: Ray starts exactly at the reference point Q0 and is neither orthogonal nor parallel
        Ray ray9 = new Ray(new Point(0, 0, 1), new Vector(1, 2, -3));
        assertNull(plane.findIntersections(ray9), "Ray starts at Q0 and goes out, should return null");
    }
}
