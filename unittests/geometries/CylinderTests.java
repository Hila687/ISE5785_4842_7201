package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Cylinder#getNormal(Point)} method.
 * Tests include both Equivalence Partitions and Boundary Value Analysis (EP and BVA).
 */
class CylinderTests {


    private static final double ACCURACY = 1e-10;

    // Cylinder with axis from (0,0,0) in direction (0,0,1), radius 1, height 2
    Cylinder cylinder = new Cylinder(
            1,
            new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
            2
    );

    // ============ Equivalence Partitions Tests ==============

    /**
     * TC01: Test getNormal for a point on the curved side surface of the cylinder.
     */
    @Test
    void testGetNormal_EP_RoundSurface() {
        Vector normal = cylinder.getNormal(new Point(0, 1, 1));
        // Expected: normal is perpendicular to the axis, pointing outward
        assertEquals(new Vector(0, 1, 0), normal, "Bad normal on side surface");
    }

    /**
     * TC02: Test getNormal for a point on the bottom base of the cylinder (z=0).
     */
    @Test
    void testGetNormal_EP_BottomBase() {
        Vector normal = cylinder.getNormal(new Point(0.5, 0, 0));
        // Expected: normal points downward, same as axis direction * -1
        assertEquals(new Vector(0, 0, -1), normal, "Bad normal on bottom base");
    }

    /**
     * TC03: Test getNormal for a point on the top base of the cylinder (z=2).
     */
    @Test
    void testGetNormal_EP_TopBase() {
        Vector normal = cylinder.getNormal(new Point(-0.5, 0, 2));
        // Expected: normal points upward, same as axis direction
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal on top base");
    }

    // =============== Boundary Values Tests ==================

    /**
     * TC11: Test getNormal at the exact center of the bottom base.
     */
    @Test
    void testGetNormal_BVA_CenterBottomBase() {
        Vector normal = cylinder.getNormal(new Point(0, 0, 0));
        // Expected: normal points downward (opposite to axis direction)
        assertEquals(new Vector(0, 0, -1), normal, "Bad normal at center of bottom base");
    }

    /**
     * TC12: Test getNormal at the exact center of the top base.
     */
    @Test
    void testGetNormal_BVA_CenterTopBase() {
        Vector normal = cylinder.getNormal(new Point(0, 0, 2));
        // Expected: normal points upward (same as axis direction)
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal at center of top base");
    }

    /**
     * TC13: Test getNormal on the edge between bottom base and side surface.
     */
    @Test
    void testGetNormal_BVA_EdgeBottom() {
        Vector normal = cylinder.getNormal(new Point(1, 0, 0));
        // This point lies both on the bottom base and the side.
        // Expected behavior (based on implementation): return base normal (downward)
        assertEquals(new Vector(0, 0, -1), normal, "Bad normal on edge (bottom)");
    }

    /**
     * TC14: Test getNormal on the edge between top base and side surface.
     */
    @Test
    void testGetNormal_BVA_EdgeTop() {
        Vector normal = cylinder.getNormal(new Point(1, 0, 2));
        // This point lies both on the top base and the side.
        // Expected behavior (based on implementation): return base normal (upward)
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal on edge (top)");
    }

    /**
     * Additional tests for {@link geometries.Cylinder#calculateIntersections(Ray, double)}
     * to verify proper filtering using maxDistance (Bonus 3 – Option 2).
     */
    @Test
    void testCalculateIntersectionsWithMaxDistance() {
        // Cylinder: axis along X-axis from (1,0,0) to (2,0,0), radius = 1
        Cylinder cylinder = new Cylinder(1, new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)), 1);

        // Ray from Z-, through the center of curved surface
        Ray ray = new Ray(new Point(1.5, 0, -2), new Vector(0, 0, 1));
        var fullResults = cylinder.calculateIntersections(ray, 10);
        assertNotNull(fullResults, "Expected intersections with full maxDistance");
        assertEquals(2, fullResults.size(), "Expected 2 intersections (curved surface)");

        double dist1 = fullResults.get(0).point.distance(ray.getHead());
        double dist2 = fullResults.get(1).point.distance(ray.getHead());

        // TC01: maxDistance > both → return both
        var result1 = cylinder.calculateIntersections(ray, 5);
        assertNotNull(result1, "Should detect both intersections");
        assertEquals(2, result1.size(), "Expected 2 intersections");

        // TC02: maxDistance just below first → none
        var result2 = cylinder.calculateIntersections(ray, dist1 - 0.01);
        assertTrue(result2 == null || result2.isEmpty(), "Expected no intersections");

        // TC03: maxDistance = first → one intersection
        var result3 = cylinder.calculateIntersections(ray, dist1);
        assertEquals(1, result3.size(), "Expected one intersection (first)");

        // TC04: maxDistance between both → only one
        var result4 = cylinder.calculateIntersections(ray, (dist1 + dist2) / 2);
        assertEquals(1, result4.size(), "Expected one intersection (only first)");

        // TC05: maxDistance = second → return both
        var result5 = cylinder.calculateIntersections(ray, dist2);
        assertEquals(2, result5.size(), "Expected both intersections on edge");

        // TC06: maxDistance = 0 → no intersection
        var result6 = cylinder.calculateIntersections(ray, 0);
        assertTrue(result6 == null || result6.isEmpty(), "Expected no intersections with distance = 0");
    }

}
