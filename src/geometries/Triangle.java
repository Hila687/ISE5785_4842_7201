package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;

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


    /**
     * returns intersection points of the triangle with a ray
     * @param ray the ray to intersect with the geometry
     * @return intersection points of the triangle with the ray
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersection = plane.findIntersections(ray);
        if (intersection == null) {
            return null;
        }

        Point p0 = ray.getP0();
        Vector dir = ray.getDirection();

        // Vectors from ray start to triangle vertices
        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector v3 = vertices.get(2).subtract(p0);

        // Normals to the edges
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = alignZero(dir.dotProduct(n1));
        double s2 = alignZero(dir.dotProduct(n2));
        double s3 = alignZero(dir.dotProduct(n3));

        // Check if all signs are the same (either all positive or all negative)
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            return intersection;
        }

        return null;
    }

}
