package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTests {
    /**
     * A point used in some tests
     */
    private final Point p001 = new Point(0, 0, 1);
    /**
     * A point used in some tests
     */
    private final Point p100 = new Point(1, 0, 0);
    /**
     * A vector used in some tests
     */
    private final Vector v001 = new Vector(0, 0, 1);

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: A point on the sphere
        Sphere s = new Sphere(1, new Point(0, 0, 0));
        assertEquals(new Vector(1, 0, 0), s.getNormal(new Point(1, 0, 0)), "Bad normal to sphere");
    }




    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, p100);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final var result2 = sphere.findIntersections(new Ray(p100, v310));
        assertNotNull(result2, "Can't be empty list");
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(gp1, result2.get(0), "Ray crosses sphere");

        // TC04: Ray starts after the sphere (0 points)
        final var result3 = sphere.findIntersections(new Ray(new Point(2, 0, 0), v310));
        assertNull(result3, "Ray's line out of sphere");

        // =============== Boundary Values Tests ==================

        // TC11: Ray starts at sphere and goes inside (1 point)
        Ray ray11 = new Ray(gp2, v310.scale(-1));
        var result11 = sphere.findIntersections(ray11);
        assertNotNull(result11, "Should intersect once");
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(List.of(gp1), result11, "Wrong intersection point");

        // TC12: Ray starts at sphere and goes outside (0 points)
        Ray ray12 = new Ray(gp1, v310);
        assertNull(sphere.findIntersections(ray12), "No intersection when going outside");

        // TC21: Ray starts before the sphere and goes through center (2 points)
        Ray ray21 = new Ray(new Point(-1, 0, 0), new Vector(2, 0, 0));
        var result21 = sphere.findIntersections(ray21);
        assertNotNull(result21, "Should intersect at two points");
        assertEquals(2, result21.size(), "Wrong number of points");

        // TC22: Ray starts at sphere and goes through center (1 point)
        Ray ray22 = new Ray(new Point(0, 0, 1), new Vector(1, 0, -1));
        var result22 = sphere.findIntersections(ray22);
        assertNotNull(result22, "Should intersect once");
        assertEquals(1, result22.size(), "Wrong number of points");

        // TC23: Ray starts inside and goes through center (1 point)
        Ray ray23 = new Ray(new Point(0.5, 0.5, 0.5), new Vector(1, 1, 1));
        var result23 = sphere.findIntersections(ray23);
        assertNotNull(result23, "Should intersect once");
        assertEquals(1, result23.size(), "Wrong number of points");

        // TC24: Ray starts at the center (1 point)
        Ray ray24 = new Ray(p100, new Vector(1, 0, 0));
        var result24 = sphere.findIntersections(ray24);
        assertNotNull(result24, "Should intersect once");
        assertEquals(1, result24.size(), "Wrong number of points");

        // TC25: Ray starts at sphere and goes outside (0 points)
        Ray ray25 = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray25), "No intersection");

        // TC26: Ray starts after sphere (0 points)
        Ray ray26 = new Ray(new Point(3, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray26), "No intersection");

        // TC31: Ray tangent before point (0 points)
        Ray ray31 = new Ray(new Point(0, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray31), "Tangent - no intersection");

        // TC32: Ray tangent at point (0 points)
        Ray ray32 = new Ray(new Point(1, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray32), "Tangent - no intersection");

        // TC33: Ray tangent after point (0 points)
        Ray ray33 = new Ray(new Point(2, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray33), "Tangent - no intersection");

        // TC41: Ray outside orthogonal to center line (0 points)
        Ray ray41 = new Ray(new Point(0, 2, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray41), "Ray is orthogonal and outside");

        // TC42: Ray inside orthogonal to center line (1 point)
        Ray ray42 = new Ray(new Point(1, 0.5, 0), new Vector(0, 1, 0));
        var result42 = sphere.findIntersections(ray42);
        assertNotNull(result42, "Should intersect once");
        assertEquals(1, result42.size(), "Wrong number of points");
    }

}