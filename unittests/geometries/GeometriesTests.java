package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Geometries#findIntersections(Ray)}.
 * This class tests intersection behavior of composite geometries using various configurations.
 */
class GeometriesTests {

    // Common reference points and directions
    private static final Point P0 = new Point(0, 0, -1);
    private static final Point SPHERE_CENTER = new Point(0, 0, 2);
    private static final Point PLANE_POINT = new Point(0, 0, 1);

    private static final Vector DIRECTION_UP = new Vector(0, 0, 1);
    private static final Vector DIRECTION_SIDE = new Vector(0, 1, 0);

    /**
     * Test method for {@link Geometries#findIntersections(Ray)}.
     * Tests both Equivalence Partitions and Boundary Value conditions.
     */
    @Test
    void testFindIntersections() {
        // Create basic geometries
        Sphere sphere = new Sphere(1, SPHERE_CENTER);
        Plane plane = new Plane(PLANE_POINT, DIRECTION_UP);
        Triangle triangle = new Triangle(
                new Point(1, 1, 1),
                new Point(-1, 1, 1),
                new Point(0, -1, 1)
        );

        // Create a composite geometry that includes all three shapes
        Geometries geometries = new Geometries(sphere, plane, triangle);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Empty collection (no geometries at all)
        Geometries empty = new Geometries();
        // Expecting null since there are no geometries to intersect
        assertNull(empty.findIntersections(new Ray(P0, DIRECTION_UP)),
                "Empty collection should return null");

        // TC02: No geometries are intersected by the ray
        Ray rayNoHit = new Ray(P0, DIRECTION_SIDE);
        // The ray is parallel and offset, so it shouldn't intersect any shape
        assertNull(geometries.findIntersections(rayNoHit),
                "Ray should miss all geometries");

        // TC03: Only one geometry (the plane) is intersected
        Geometries singleHit = new Geometries(plane);
        Ray rayPlane = new Ray(P0, DIRECTION_UP);
        List<Point> resultSingle = singleHit.findIntersections(rayPlane);
        assertNotNull(resultSingle, "Expected one intersection");
        assertEquals(1, resultSingle.size(), "Wrong number of points");

        // TC04: Two geometries are intersected (sphere + plane)
        Geometries twoHit = new Geometries(sphere, plane);
        List<Point> resultTwo = twoHit.findIntersections(rayPlane);
        assertNotNull(resultTwo, "Expected two intersections");
        assertEquals(3, resultTwo.size(), "Expected 3 intersection points"); // sphere: 2, plane: 1

        // TC05: All geometries are intersected (sphere + plane + triangle)
        List<Point> resultAll = geometries.findIntersections(rayPlane);
        assertNotNull(resultAll, "Expected intersections with all geometries");
        assertEquals(4, resultAll.size(), "Expected 4 intersection points"); // sphere: 2, plane: 1, triangle: 1
    }
}
