package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link primitives.Ray} class.
 *
 * @author Hila Rosental & Hila Miller
 */
class RayTest {
    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void findClosestPoint() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Closest point is the first one in the list

        // Create a ray with a starting point and direction
        Point startPoint = new Point(1, 2, 3);
        Vector direction = new Vector(4, 5, 6);
        Ray ray = new Ray(startPoint, direction);

        // Create a list of points to test against
        List<Point> points = List.of(
                new Point(7, 8, 9),
                new Point(1, 2, 3), // This point is the same as the start point
                new Point(10, 11, 12)
        );

        // Find the closest point to the ray
        Point closestPoint = ray.findClosestPoint(points);

        System.out.println("Closest point: " + closestPoint);
        // Check that the closest point is the expected one
        assertEquals( startPoint, closestPoint, "Closest point should be the first one in the list");

        //============ Boundary Values Tests ======================
        // TC11: Empty list

        // Create an empty list of points
        List<Point> emptyList = List.of();
        assertNull( ray.findClosestPoint(emptyList), "Empty list should return null");

        // TC12: First point is the closest one
        // Create a list of points with the first point being the closest
        List<Point> firstClosestList = List.of(
                new Point(1, 2, 3), // This point is the same as the start point
                new Point(7, 8, 9),
                new Point(10, 11, 12)
        );

        // Find the closest point to the ray
        Point firstClosestPoint = ray.findClosestPoint(firstClosestList);
        // Check that the closest point is the expected one
        assertEquals(startPoint, firstClosestPoint, "First point should be the closest one");

        // TC13: Last point is the closest one
        // Create a list of points with the last point being the closest
        List<Point> lastClosestList = List.of(
                new Point(7, 8, 9),
                new Point(10, 11, 12),
                new Point(1, 2, 3)// This point is the closest one
        );

        // Find the closest point to the ray
        Point lastClosestPoint = ray.findClosestPoint(lastClosestList);
        // Check that the closest point is the expected one
        assertEquals(new Point(1,2,3), lastClosestPoint, "Last point should be the closest one");

    }
}