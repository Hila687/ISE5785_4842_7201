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
}