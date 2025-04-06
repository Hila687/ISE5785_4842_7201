package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.*;

import java.util.List;

/**
 * Testing Polygons
 * @author Dan
 */
class PolygonTests {
   /**
    * Delta value for accuracy when comparing the numbers of type 'double' in
    * assertEquals
    */
   private static final double DELTA = 0.000001;
   private static final Point P0 = new Point(0, 0, -1);
   private static final Vector DIRECTION_UP = new Vector(0, 0, 1);
   private static final Vector DIRECTION_SIDE = new Vector(0, 1, 0);


   /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
   @Test
   void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                                           new Point(1, 0, 0),
                                           new Point(0, 1, 0),
                                           new Point(-1, 1, 1)),
                         "Failed constructing a correct polygon");

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                   "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                   "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0.5, 0.25, 0.5)), //
                   "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertix on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                   "Constructed a polygon with vertice on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                   "Constructed a polygon with vertice on a side");

   }

   /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
   @Test
   void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                      "Polygon's normal is not orthogonal to one of the edges");
   }

   /**
    * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
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

      // TC01: Ray intersects the polygon inside
      Ray rayInside = new Ray(P0, DIRECTION_UP);
      List<Point> result1 = polygon.findIntersections(rayInside);
      assertNotNull(result1, "Ray should intersect the polygon");
      assertEquals(1, result1.size(), "Wrong number of intersection points");

      // TC02: Ray misses the polygon (right direction but outside)
      Ray rayMiss = new Ray(new Point(2, 2, -1), DIRECTION_UP);
      assertNull(polygon.findIntersections(rayMiss), "Ray should miss the polygon");

      // TC03: Ray hits the polygon's edge (should return null)
      Ray rayEdge = new Ray(new Point(1, 0, -1), DIRECTION_UP);
      assertNull(polygon.findIntersections(rayEdge), "Ray on polygon edge is not considered inside");

      // TC04: Ray hits the polygon's vertex (should return null)
      Ray rayVertex = new Ray(new Point(1, 1, -1), DIRECTION_UP);
      assertNull(polygon.findIntersections(rayVertex), "Ray on polygon vertex is not considered inside");
   }
}
