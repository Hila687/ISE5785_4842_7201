package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.*;

import java.util.List;

/**
 * Unit tests for {@link Polygon} class.
 * Includes tests for constructor, {@link Polygon#getNormal(Point)}, and {@link Polygon#findIntersections(Ray)}.
 */
class PolygonTests {

   /** Delta value for comparing floating-point values with tolerance */
   private static final double DELTA = 0.000001;

   // Common reference point and directions
   private static final Point P0 = new Point(0, 0, -1);
   private static final Vector DIRECTION_UP = new Vector(0, 0, 1);
   private static final Vector DIRECTION_SIDE = new Vector(0, 1, 0);

   /**
    * Test method for {@link Polygon#Polygon(Point...)}.
    * Validates correct polygon construction and exception handling for invalid input.
    */
   @Test
   void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Valid convex quadrilateral with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                      new Point(1, 0, 0),
                      new Point(0, 1, 0),
                      new Point(-1, 1, 1)),
              "Failed constructing a correct polygon");

      // TC02: Invalid order of vertices
      assertThrows(IllegalArgumentException.class,
              () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0),
                      new Point(1, 0, 0), new Point(-1, 1, 1)),
              "Constructed a polygon with wrong order of vertices");

      // TC03: Points not in the same plane
      assertThrows(IllegalArgumentException.class,
              () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0),
                      new Point(0, 1, 0), new Point(0, 2, 2)),
              "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave shape
      assertThrows(IllegalArgumentException.class,
              () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0),
                      new Point(0, 1, 0), new Point(0.5, 0.25, 0.5)),
              "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex lies on the edge of the polygon
      assertThrows(IllegalArgumentException.class,
              () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0),
                      new Point(0, 1, 0), new Point(0, 0.5, 0.5)),
              "Constructed a polygon with vertex on a side");

      // TC11: First and last points are the same
      assertThrows(IllegalArgumentException.class,
              () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0),
                      new Point(0, 1, 0), new Point(0, 0, 1)),
              "Constructed a polygon with repeated first/last vertex");

      // TC12: Two identical points in the input
      assertThrows(IllegalArgumentException.class,
              () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0),
                      new Point(0, 1, 0), new Point(0, 1, 0)),
              "Constructed a polygon with duplicate vertices");
   }

   /**
    * Test method for {@link Polygon#getNormal(Point)}.
    * Verifies correctness of normal vector and orthogonality to all edges.
    */
   @Test
   void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Valid polygon with four vertices (quad)
      Point[] pts = {
              new Point(0, 0, 1),
              new Point(1, 0, 0),
              new Point(0, 1, 0),
              new Point(-1, 1, 1)
      };
      Polygon pol = new Polygon(pts);

      // Ensure getNormal does not throw
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");

      // Get the normal vector
      Vector result = pol.getNormal(new Point(0, 0, 1));

      // Assert it is a unit vector
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");

      // Assert orthogonality to all edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                 "Polygon's normal is not orthogonal to one of the edges");
   }

   /**
    * Test method for {@link Polygon#findIntersections(Ray)}.
    * Validates intersections for a square polygon in the Z=1 plane.
    */
   @Test
   void testFindIntersections() {
      // Define a square polygon in plane z = 1
      Polygon polygon = new Polygon(
              new Point(1, 1, 1),
              new Point(-1, 1, 1),
              new Point(-1, -1, 1),
              new Point(1, -1, 1)
      );

      // TC01: Ray intersects inside the polygon
      Ray rayInside = new Ray(P0, DIRECTION_UP);
      List<Point> result1 = polygon.findIntersections(rayInside);
      assertNotNull(result1, "Ray should intersect the polygon");
      assertEquals(1, result1.size(), "Wrong number of intersection points");

      // TC02: Ray misses the polygon (outside boundary)
      Ray rayMiss = new Ray(new Point(2, 2, -1), DIRECTION_UP);
      assertNull(polygon.findIntersections(rayMiss), "Ray should miss the polygon");

      // TC03: Ray intersects exactly on the polygon edge
      Ray rayEdge = new Ray(new Point(1, 0, -1), DIRECTION_UP);
      assertNull(polygon.findIntersections(rayEdge), "Ray on polygon edge is not considered inside");

      // TC04: Ray intersects exactly at a polygon vertex
      Ray rayVertex = new Ray(new Point(1, 1, -1), DIRECTION_UP);
      assertNull(polygon.findIntersections(rayVertex), "Ray on polygon vertex is not considered inside");
   }
}
