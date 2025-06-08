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
public class Geometries extends Intersectable {

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




    /**
     * Calculates the intersections of a ray with all geometries in the collection.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersections, or null if no intersections were found
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = new LinkedList<>();

        // Iterate over each geometry in the collection
        for (Intersectable geometry : geometries) {
            // Use the extended intersection method with distance limit
            List<Intersection> temp = geometry.calculateIntersections(ray, maxDistance);

            // If intersections were found, add them to the result list
            if (temp != null) {
                intersections.addAll(temp);
            }
        }

        // Return null if the result list is empty
        return intersections.isEmpty() ? null : intersections;
    }

}
