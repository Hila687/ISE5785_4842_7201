package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Geometries class represents a collection of {@link Intersectable} geometries,
 * using the Composite Design Pattern.
 * It allows grouping multiple geometric shapes and treating them as a single unit.
 */
public class Geometries implements Intersectable {

    /**
     * Internal list of geometries that implement {@link Intersectable}.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor - creates an empty list of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructor that initializes the collection with one or more geometries.
     *
     * @param geometries one or more intersectable geometries to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the internal collection.
     *
     * @param geometries intersectable geometries to be added
     */
    public void add(Intersectable... geometries) {
        // Add all given geometries to the list
        Collections.addAll(this.geometries, geometries);
    }


    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;

        // Iterate over each geometry in the collection
        for (Intersectable geometry : geometries) {
            // Get intersections with the current geometry
            List<Point> temp = geometry.findIntersections(ray);

            // If there are intersections, add them to the final list
            if (temp != null) {
                if (intersections == null) {
                    intersections = new LinkedList<>();
                }
                intersections.addAll(temp);
            }
        }

        // Return null if no intersections were found, or the list of points otherwise
        return intersections;
    }
}
