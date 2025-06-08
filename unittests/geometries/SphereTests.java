package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;


import java.util.Comparator;
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
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.00001;
    /**
     * Constant value: √0.75, used for calculations involving distances or positions on the sphere.
     */
    private final double sqrt075 = Math.sqrt(0.75);
    /**
     * Vector v1 with components (1, 0, 1)
     */
    private final Vector v1 = new Vector(1, 0, 1);
    /**
     * Vector v2 with components (0, -1, 0)
     */
    private final Vector v2 = new Vector(0, -1, 0);
    /**
     * Vector v3 with components (0, 1, 0)
     */
    private final Vector v3 = new Vector(0, 1, 0);
    /**
     * Vector v4 with components (1, 1, 1)
     */
    private final Vector v4 = new Vector(1, 1, 1);
    /**
     * Point located at (0, 1, 1)
     */
    private final Point p2 = new Point(0, 1, 1);
    /**
     * Point located at (0, 2, 1)
     */
    private final Point p3 = new Point(0, 2, 1);
    /**
     * Point located at (0, -1, 1)
     */
    private final Point p4 = new Point(0, -1, 1);
    /**
     * Point located at (0, 0.5, 1)
     */
    private final Point p5 = new Point(0, 0.5, 1);
    /**
     * Point located at (0, √0.75, 1.5)
     */
    private final Point p7 = new Point(0, sqrt075, 1.5);
    /**
     * Point located at (0, 0, 1)
     */
    private final Point p8 = new Point(0, 0, 1);

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
    void testFindIntersections() {
        Sphere sphere = new Sphere(1, p8);

        // ============ Equivalence Partitions Tests ==============
        // TC01 The ray start inside the sphere
        assertEquals(List.of(p7), sphere.findIntersections(new Ray(new Point(0, 0, 1.5), v3)), "Failed to find the intersection point when the ray start inside the sphere");

        // TC02 The ray never intersect the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 3), v4)), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC03 The ray start outside the sphere and intersect the sphere twice
        assertEquals(List.of(p7, new Point(0, -sqrt075, 1.5)), sphere.findIntersections(new Ray(new Point(0, 2, 1.5), v2)), "Failed to find the intersection points when the ray start outside the sphere and intersect the sphere twice");

        // TC04 The ray start outside the sphere and the ray does not intersect the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(0, -2, 1.5), v2)), "Failed to find the intersection points when the ray start outside the sphere and not intersect the sphere");

        // =============== Boundary Values Tests =================

        // **** Group 1: Ray is orthogonal to the sphere
        // TC05 The ray is orthogonal to the sphere and start before the sphere
        assertNull(sphere.findIntersections(new Ray(p3, new Vector(0, 0, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC06 The ray is orthogonal to the sphere and start in the sphere
        assertEquals(List.of(new Point(0, 0.5, 1 - sqrt075)), sphere.findIntersections(new Ray(p5, new Vector(0, 0, -1))), "Failed to find the intersection point when the ray start inside the sphere");

        // **** Group 2: Ray is tangential to the sphere
        // TC07 The ray is tangential to the sphere and start before the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 1, 0), v1)), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC08 The ray is tangential to the sphere and start on the sphere
        assertNull(sphere.findIntersections(new Ray(p2, v1)), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC09 The ray is tangential to the sphere and start after the sphere
        assertNull(sphere.findIntersections(new Ray(new Point(1, 1, 2), v1)), "Failed to find the intersection point when the ray never intersect the sphere");


        // **** Group 3: Ray is not orthogonal or tangential
        // TC10 The ray start on the sphere and intersect the sphere
        assertEquals(List.of(new Point(-2.0 / 3, 1.0 / 3, 1.0 / 3)), sphere.findIntersections(new Ray(p2, new Vector(-1, -1, -1))), "Failed to find the intersection point when the ray start on the sphere and intersect the sphere");

        // TC11 The ray start on the sphere and does not intersect the sphere
        assertNull(sphere.findIntersections(new Ray(p2, v4)), "Failed to find the intersection point when the ray start on the sphere and doesn't intersect the sphere");

        // **** Group 4: Ray goes through the center
        // TC12 The ray start on the sphere and reach the middle of the sphere
        assertEquals(List.of(p4), sphere.findIntersections(new Ray(p2, v2)), "Failed to find the intersection point when the ray start on the sphere and reach the middle of the sphere");

        // TC13 The ray start before the sphere and reach the middle of the sphere
        assertEquals(List.of(p2, p4), sphere.findIntersections(new Ray(p3, v2)).stream().sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1, 0, 0)))).toList(), "Failed to find the intersection point when the ray start before the sphere and reach the middle of the sphere");

        //TC14 The ray start in the middle of the sphere
        assertEquals(List.of(p2), sphere.findIntersections(new Ray(p8, v3)), "Failed to find the intersection point when the ray start in the middle of the sphere");

        //TC15 the run on the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntersections(new Ray(p2, v3)), "Failed to find the intersection point when the ray start on the sphere and doesn't reach the middle of the sphere");

        //TC16 the run after the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntersections(new Ray(p3, v3)), "Failed to find the intersection point when the ray start after the sphere and doesn't reach the middle of the sphere");

        //TC17 the run in the sphere and does not reach the middle of the sphere because the direction is opposite
        assertEquals(List.of(p2), sphere.findIntersections(new Ray(p5, v3)), "Failed to find the intersection point when the ray start in the sphere and doesn't reach the middle of the sphere");
    }

    /**
     * Test method for {@link Sphere#calculateIntersections(Ray, double)}.
     * Validates behavior with maxDistance constraint.
     */
    @Test
    void testCalculateIntersectionsWithMaxDistance() {
        Sphere sphere = new Sphere(1, new Point(0, 0, 0));

        // TC01: Two intersection points, both within maxDistance
        Ray ray1 = new Ray(new Point(-2, 0, 0), new Vector(1, 0, 0));
        List<Intersectable.Intersection> result1 = sphere.calculateIntersections(ray1, 4);
        assertNotNull(result1, "TC01: Expected two intersection points");
        assertEquals(2, result1.size(), "TC01: Wrong number of intersections");

        // TC02: Two intersections, only the first is within maxDistance
        List<Intersectable.Intersection> result2 = sphere.calculateIntersections(ray1, 2.5);
        assertNotNull(result2, "TC02: Expected one intersection point");
        assertEquals(1, result2.size(), "TC02: Wrong number of intersections");

        // TC03: Both intersection points are beyond maxDistance
        List<Intersectable.Intersection> result3 = sphere.calculateIntersections(ray1, 0.5);
        assertNull(result3, "TC03: Expected no intersections due to small maxDistance");

        // TC04: Single intersection from inside the sphere, within maxDistance
        Ray ray4 = new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0));
        List<Intersectable.Intersection> result4 = sphere.calculateIntersections(ray4, 2);
        assertNotNull(result4, "TC04: Expected one intersection from inside the sphere");
        assertEquals(1, result4.size(), "TC04: Wrong number of intersections");

        // TC05: Ray is tangent, point exactly at maxDistance
        Ray ray5 = new Ray(new Point(0, -1, 0), new Vector(1, 0, 0));
        List<Intersectable.Intersection> result5 = sphere.calculateIntersections(ray5, 1);
        assertNull(result5, "TC05: Tangent ray should not intersect");

        // TC06: Ray misses the sphere completely
        Ray ray6 = new Ray(new Point(0, -2, 0), new Vector(1, 0, 0));
        List<Intersectable.Intersection> result6 = sphere.calculateIntersections(ray6, 10);
        assertNull(result6, "TC06: Ray should miss the sphere entirely");
    }
}