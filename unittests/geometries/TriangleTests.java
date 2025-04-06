package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;


import static org.junit.jupiter.api.Assertions.*;

class TriangleTests {
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: A point on the triangle
        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );
        assertEquals(new Vector(0, 0, 1), triangle.getNormal(new Point(0, 0, 0)), "Bad normal to triangle");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(
                new Point(0, 1, 0),
                new Point(1, 0, 0),
                new Point(-1, 0, 0)
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects the triangle (inside)
        Ray ray1 = new Ray(new Point(0, 0.5, -1), new Vector(0, 0, 1));
        assertNotNull(triangle.findIntersections(ray1), "Ray intersects inside triangle");

        // TC02: Ray misses the triangle – outside against edge
        Ray ray2 = new Ray(new Point(1, 0.5, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray2), "Ray misses triangle - outside against edge");

        // TC03: Ray misses the triangle – outside against vertex
        Ray ray3 = new Ray(new Point(0, 1.5, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray3), "Ray misses triangle - outside against vertex");

        // =============== Boundary Values Tests ==================

        // TC11: Ray intersects exactly on edge
        Ray ray4 = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray4), "Ray on triangle edge – no intersection");

        // TC12: Ray intersects exactly in vertex
        Ray ray5 = new Ray(new Point(1, 0, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray5), "Ray on triangle vertex – no intersection");

        // TC13: Ray intersects on edge continuation
        Ray ray6 = new Ray(new Point(2, -1, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray6), "Ray on edge continuation – no intersection");
    }

}