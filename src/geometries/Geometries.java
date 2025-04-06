package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
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
     * @param geometries intersectable objects to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds geometries to the collection.
     * @param geometries intersectable objects to add
     */
    private void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries) {
            this.geometries.add(geometry);
        }
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
