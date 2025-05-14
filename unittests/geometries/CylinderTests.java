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
}
