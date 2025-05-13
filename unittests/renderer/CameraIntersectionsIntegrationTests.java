package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;

/**
 * Integration tests for Camera ray construction and intersections with geometries.
 * Tests include Sphere, Plane, and Triangle intersection counts from rays cast by the Camera.
 */
public class CameraIntersectionsIntegrationTests {

    /** A camera located at origin, facing toward -Z, with a 3x3 view plane at distance 1 */
    Camera camera = new Camera.Builder()
            .setLocation(new Point(0, 0, 0))
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpSize(3, 3)
            .setVpDistance(1)
            .setResolution(3, 3)
            .build();

    /**
     * Helper method to count the total number of intersection points
     * between a geometry and rays constructed through all pixels of the view plane.
     *
     * @param camera   the camera constructing the rays
     * @param geometry the geometry to test intersections with
     * @param nX       number of columns (width) in the view plane
     * @param nY       number of rows (height) in the view plane
     * @param expected the expected number of intersection points
     */
    private void countIntersections(Camera camera, Intersectable geometry, int nX, int nY, int expected) {
        int count = 0;

        // Loop over each pixel in the view plane
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // Construct a ray through the pixel
                var intersections = geometry.findIntersections(camera.constructRay(nX, nY, j, i));

                // If there are intersection points, add their count
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }

        // Assert that the total number of intersection points matches the expected value
        assertEquals(expected, count, "Wrong number of intersections with geometry");
    }

    /**
     * Integration test for intersections between camera rays and a sphere.
     * The test counts how many rays from the camera intersect with the sphere.
     * Sphere is centered at (0, 0, -3) with radius 1.
     */
    @Test
    void testSphereIntersections() {
        Sphere sphere = new Sphere(1, new Point(0, 0, -3));
        countIntersections(camera, sphere, 3, 3, 2);
    }

    /**
     * Integration test for intersections between camera rays and a plane.
     * The test counts how many rays from the camera intersect with the plane.
     * Plane is at z = -5, facing forward (normal vector (0, 0, 1)).
     */
    @Test
    void testPlaneIntersections() {
        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        countIntersections(camera, plane, 3, 3, 9);
    }

    /**
     * Integration test for intersections between camera rays and a triangle.
     * The test counts how many rays from the camera intersect with the triangle.
     * Triangle is positioned directly in front of the center pixel.
     */
    @Test
    void testTriangleIntersections() {
        Triangle triangle = new Triangle(
                new Point(0, 1, -2),
                new Point(-1, -1, -2),
                new Point(1, -1, -2)
        );
        countIntersections(camera, triangle, 3, 3, 1);
    }
}
