package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PointTests {
    Point P1 = new Point(1, 2, 3);
    Point P2 = new Point(2, 4, 6);

    @Test
    void testSubtract() {
//        Vector V1 = new Vector(1, 2, 3);
//        assertEquals(v1);
//        try {
//            P1.subtract(P1);
//            fail("Subtract() for same point does not throw an exception for Vector(0,0,0)");
//        } catch (IllegalArgumentException ignore) {
//            assertTrue(true);
//        } catch (Exception ignore) {
//            fail("Subtracting a point from itself throws the wrong exception");
//        }

    }

    @Test
    void testAdd() {
        Vector V1 = new Vector(1, 2, 3);
        Point P3 = P1.add(V1);
        assertEquals(new Point(2, 4, 6), P3, "Point addition did not produce the expected result");
    }

    @Test
    void testDistanceSquared() {
    }

    @Test
    void testDistance() {
    }
}