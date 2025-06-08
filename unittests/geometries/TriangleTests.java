package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Triangle} class.
 * Includes tests for {@link Triangle#getNormal(Point)} and {@link Triangle#findIntersections(Ray)} methods.
 */
class TriangleTests {

    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     * Verifies that the normal is correctly calculated.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: A point on the triangle – expect constant normal for entire surface
        Triangle triangle = new Triangle(
                new Point(0, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );

        // The triangle lies on the XY plane → normal should be (0, 0, 1)
        assertEquals(new Vector(0, 0, 1),
                triangle.getNormal(new Point(0, 0, 0)),
                "Bad normal to triangle");
    }

    /**
     * Test method for {@link Triangle#findIntersections(Ray)}.
     * Covers intersection scenarios including inside, outside, and boundary cases.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(
                new Point(0, 1, 0),
                new Point(1, 0, 0),
                new Point(-1, 0, 0)
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects inside the triangle
        Ray ray1 = new Ray(new Point(0, 0.5, -1), new Vector(0, 0, 1));
        assertNotNull(triangle.findIntersections(ray1),
                "Ray intersects inside triangle");

        // TC02: Ray misses the triangle – outside against an edge
        Ray ray2 = new Ray(new Point(1, 0.5, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray2),
                "Ray misses triangle - outside against edge");

        // TC03: Ray misses the triangle – outside against a vertex
        Ray ray3 = new Ray(new Point(0, 1.5, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray3),
                "Ray misses triangle - outside against vertex");

        // =============== Boundary Values Tests ==================

        // TC11: Ray intersects exactly on the edge of the triangle
        Ray ray4 = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray4),
                "Ray on triangle edge – no intersection");

        // TC12: Ray intersects exactly at one of the triangle's vertices
        Ray ray5 = new Ray(new Point(1, 0, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray5),
                "Ray on triangle vertex – no intersection");

        // TC13: Ray intersects on the continuation of an edge (outside triangle)
        Ray ray6 = new Ray(new Point(2, -1, -1), new Vector(0, 0, 1));
        assertNull(triangle.findIntersections(ray6),
                "Ray on edge continuation – no intersection");
    }

    /**
     * Test method for {@link Triangle#calculateIntersections(Ray, double)}.
     * Validates behavior with maxDistance constraint.
     */
    @Test
    void testCalculateIntersectionsWithMaxDistance() {
        Triangle triangle = new Triangle(
                new Point(0, 1, 0),
                new Point(1, 0, 0),
                new Point(-1, 0, 0)
        );

        // TC01: Intersection inside triangle within maxDistance
        Ray ray1 = new Ray(new Point(0, 0.5, -1), new Vector(0, 0, 1));
        List<Intersectable.Intersection> result1 = triangle.calculateIntersections(ray1, 3);
        assertNotNull(result1, "TC01: Expected intersection inside triangle");
        assertEquals(1, result1.size(), "TC01: Expected exactly one intersection");

        // TC02: Same ray, maxDistance too small → no intersection
        List<Intersectable.Intersection> result2 = triangle.calculateIntersections(ray1, 0.5);
        assertNull(result2, "TC02: Expected no intersection due to small maxDistance");

        // TC03: Intersection exactly at the maxDistance
        List<Intersectable.Intersection> result3 = triangle.calculateIntersections(ray1, 1.0);
        assertNotNull(result3, "TC03: Intersection exactly at maxDistance should be accepted");
        assertEquals(1, result3.size(), "TC03: Expected one intersection");

        // TC04: Ray aimed outside the triangle → no intersection
        Ray ray2 = new Ray(new Point(1, 1, -1), new Vector(0, 0, 1));
        List<Intersectable.Intersection> result4 = triangle.calculateIntersections(ray2, 10);
        assertNull(result4, "TC04: Ray misses triangle – should return null");

        // TC05: Ray aimed to hit exactly on the edge → no intersection
        Ray ray3 = new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1));
        List<Intersectable.Intersection> result5 = triangle.calculateIntersections(ray3, 5);
        assertNull(result5, "TC05: Ray on edge – should return null");

        // TC06: Ray aimed at the vertex of the triangle → no intersection
        Ray ray4 = new Ray(new Point(1, 0, -1), new Vector(0, 0, 1));
        List<Intersectable.Intersection> result6 = triangle.calculateIntersections(ray4, 5);
        assertNull(result6, "TC06: Ray on vertex – should return null");
    }

}
