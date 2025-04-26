package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;

/**
 * Integration tests for Camera Ray construction and Intersections with geometries.
 * Testing Sphere, Plane, and Triangle intersections through Camera rays.
 */
public class CameraIntersectionsIntegrationTests {

    Camera camera = new Camera.Builder()
            .setLocation(new Point(0, 0, 0))
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpSize(3, 3)
            .setVpDistance(1)
            .setResolution(3, 3)
            .build();

    /**
     * Helper method to count the total number of intersection points
     * between a geometry and rays constructed through all pixels of the View Plane.
     *
     * @param camera    the camera creating the rays
     * @param geometry  the geometry to intersect
     * @param nX        number of columns (width) of the View Plane
     * @param nY        number of rows (height) of the View Plane
     * @return total number of intersection points
     */
    private void countIntersections(Camera camera, Intersectable geometry, int nX, int nY, int expected) {
        int count = 0;
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                var intersections = geometry.findIntersections(camera.constructRay(nX, nY, j, i));
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        assertEquals(expected, count, "Wrong number of intersections with geometry");
    }

    /**
     * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testSphereIntersections() {

        Sphere sphere = new Sphere(1, new Point(0, 0, -3));

        countIntersections(camera, sphere, 3, 3, 2);
    }


    /**
     * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testPlaneIntersections() {

        Plane plane = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));

        countIntersections(camera, plane, 3, 3, 9);

    }

    /**
     * Test method for {@link renderer.Camera#constructRay(int, int, int, int)}.
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