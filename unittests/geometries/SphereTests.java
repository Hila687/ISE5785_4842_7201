package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTests {

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
}