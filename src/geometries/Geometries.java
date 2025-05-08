package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/*
    Geometries class represents a collection of intersectable geometries, using composite pattern.
 */
public class Geometries implements Intersectable {
    /**
     * A list of intersectable geometries.
     */
    private final List<Intersectable> geometries = new LinkedList<>();


    /**
     * Default constructor - creates an empty collection.
     */
    public Geometries() {
    }

    /**
     * Constructor that accepts multiple geometries.
     *
     * @param geometries intersectable objects to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds geometries to the collection.
     *
     * @param geometries intersectable objects to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(this.geometries, geometries);
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;

        for (Intersectable geometry : geometries) {
            List<Point> temp = geometry.findIntersections(ray);
            if (temp != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(temp);
            }
        }

        return intersections;
    }

}
