package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

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
     * TC01: Point on round surface (side)
     */
    @Test
    void testGetNormal_EP_RoundSurface() {
        Vector normal = cylinder.getNormal(new Point(0, 1, 1));
        assertEquals(new Vector(0, 1, 0), normal, "Bad normal on side surface");
    }

    /**
     * TC02: Point on bottom base (z=0)
     */
    @Test
    void testGetNormal_EP_BottomBase() {
        Vector normal = cylinder.getNormal(new Point(0.5, 0, 0));
        assertEquals(new Vector(0, 0, -1), normal, "Bad normal on bottom base");
    }

    /**
     * TC03: Point on top base (z=2)
     */
    @Test
    void testGetNormal_EP_TopBase() {
        Vector normal = cylinder.getNormal(new Point(-0.5, 0, 2));
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal on top base");
    }

    // =============== Boundary Values Tests ==================

    /**
     * TC11: Point at center of bottom base
     */
    @Test
    void testGetNormal_BVA_CenterBottomBase() {
        Vector normal = cylinder.getNormal(new Point(0, 0, 0));
        assertEquals(new Vector(0, 0, -1), normal, "Bad normal at center of bottom base");
    }

    /**
     * TC12: Point at center of top base
     */
    @Test
    void testGetNormal_BVA_CenterTopBase() {
        Vector normal = cylinder.getNormal(new Point(0, 0, 2));
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal at center of top base");
    }

    /**
     * TC13: Point on edge between bottom base and side
     */
    @Test
    void testGetNormal_BVA_EdgeBottom() {
        Vector normal = cylinder.getNormal(new Point(1, 0, 0));
        // According to the assignment instructions: this case forces the implementation
        // to "choose" whether to return the normal of the base or the side surface.
        // We assume (based on the current implementation) that it returns the base normal – because z == 0

        assertEquals(new Vector(0, 0, -1), normal, "Bad normal on edge (bottom)");
    }

    /**
     * TC14: Point on edge between top base and side
     */
    @Test
    void testGetNormal_BVA_EdgeTop() {
        Vector normal = cylinder.getNormal(new Point(1, 0, 2));
        // According to the assignment instructions: this case forces the implementation
        // to "choose" whether to return the normal of the base or the side surface.
        // We assume (based on the current implementation) that it returns the base normal – because z == height
        assertEquals(new Vector(0, 0, 1), normal, "Bad normal on edge (top)");
    }
}
