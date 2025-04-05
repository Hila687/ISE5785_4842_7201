package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents a triangle in 3D space.
 * A triangle is a specific type of polygon with exactly three vertices.
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle from three vertices.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // barycentric coordinates;
//        Vector edge1 = vertices.get(1).subtract(vertices.get(0));
//        Vector edge2 = vertices.get(2).subtract(vertices.get(0));
//        Vector h = ray.direction().crossProduct(edge2);
//        double a = edge1.dotProduct(h);
//        if (isZero(a)) {
//            return null; // ray is parallel to the triangle
//        }
//        double f = 1 / a;
//        Vector s = ray.origin().subtract(vertices.get(0));
//        double u = f * s.dotProduct(h);
//        if (u < 0 || u > 1) {
//            return null;
//        }
//        Vector q = s.crossProduct(edge1);
//        double v = f * ray.direction().dotProduct(q);
//        if (v < 0 || u + v > 1) {
//            return null;
//        }
//        double t = f * edge2.dotProduct(q);
//        if (t > 0) {
//            return List.of(ray.getPoint(t));
//        }
          return null;





    }
}
