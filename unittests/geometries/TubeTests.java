package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TubeTests {
    private static final double ACCURACY = 1e-10;

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: A point on the tube
        Tube tube = new Tube(1, new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)));
        assertEquals(new Vector(0, 1, 0), tube.getNormal(new Point(2, 1, 0)),"Bad normal to tube");

    }

    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormalZeroVector() {
        // =============== Boundary Values Tests ==================
        // TC11: A point on the tube
        Tube tube = new Tube(1, new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)));
        assertThrows(IllegalArgumentException.class,
                () -> tube.getNormal(new Point(1, 0, 0)),
                "getNormal() for zero vector does not throw an exception");
    }

    /**
     * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // Define a tube along the Z-axis with radius 1
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Tube tube = new Tube(1, axisRay);

        // TC01: Ray is perpendicular to axis and intersects in 2 points
        Ray ray1 = new Ray(new Point(1, 0, 0), new Vector(-1, 0, 0));
        assertEquals(2, tube.findIntersections(ray1).size(), "TC01: Wrong number of intersection points");

        // TC02: Ray is perpendicular and tangent (1 point)
        Ray ray2 = new Ray(new Point(1, 1, 0), new Vector(-1, 0, 0));
        assertEquals(1, tube.findIntersections(ray2).size(), "TC02: Should intersect in one tangent point");

        // TC03: Ray is perpendicular and misses (0 points)
        Ray ray3 = new Ray(new Point(1, 2, 0), new Vector(-1, 0, 0));
        assertNull(tube.findIntersections(ray3), "TC03: Ray should miss the tube");

        // TC04: Ray is parallel to axis and outside (0 points)
        Ray ray4 = new Ray(new Point(2, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray4), "TC04: Ray parallel and outside should miss");

        // TC05: Ray starts inside the tube and exits (1 point)
        Ray ray5 = new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0));
        assertEquals(1, tube.findIntersections(ray5).size(), "TC05: Ray from inside should intersect once");

        // TC06: Ray goes through the axis (2 points)
        Ray ray6 = new Ray(new Point(1, 1, -2), new Vector(0, 0, 1));
        assertEquals(2, tube.findIntersections(ray6).size(), "TC06: Ray through tube should intersect twice");

        // TC07: Ray starts at surface and goes inside (1 point)
        Ray ray7 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 0));
        assertEquals(1, tube.findIntersections(ray7).size(), "TC07: Ray from surface inward should intersect once");
    }
}