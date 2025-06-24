package geometries;

import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract base class for all geometric containers (individual or composite).
 * Supports optional BVH acceleration using bounding volumes.
 * All geometric shapes and collections should extend this class.
 *
 * @author Hila Rosental and Hila Miller
 */
public abstract class Container extends Intersectable {

    /**
     * The bounding box of the geometry or composite geometry.
     */
    protected BoundingBox boundingBox = null;

    /**
     * Flag indicating whether BVH optimization should be used.
     */
    private boolean bvh = true;

    /**
     * Returns the bounding box of this container.
     *
     * @return the bounding box, or null if not set
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * Sets the bounding box of this container.
     * Must be implemented by each subclass individually.
     */
    public abstract void setBoundingBox();

    /**
     * Returns whether BVH optimization is enabled.
     *
     * @return true if BVH is enabled, false otherwise
     */
    public boolean isBvh() {
        return bvh;
    }

    /**
     * Sets whether BVH optimization should be enabled.
     *
     * @param bvh true to enable BVH, false to disable
     * @return this container (for chaining)
     */
    public Container setBvh(boolean bvh) {
        this.bvh = bvh;
        return this;
    }

    /**
     * Computes the intersections between a ray and this container,
     * using BVH optimization if enabled.
     *
     * @param ray         the ray to intersect
     * @param maxDistance the maximum allowed distance for intersections
     * @return a list of intersection points, or null if none
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        // Skip if BVH is enabled and ray misses the bounding box
        if (bvh && boundingBox != null && !boundingBox.intersects(ray, maxDistance)) {
            return null;
        }

        // Handle a single geometry (leaf node)
        if (this instanceof Geometry geo) {
            return geo.calculateIntersectionsHelper(ray, maxDistance);
        }

        // Handle a group of geometries (composite node)
        if (this instanceof Geometries group) {
            List<Intersection> intersections = null;

            for (Container child : group.getContainerList()) {
                List<Intersection> childHits = child.calculateIntersections(ray, maxDistance);
                if (childHits != null && !childHits.isEmpty()) {
                    if (intersections == null)
                        intersections = new LinkedList<>(childHits);
                    else
                        intersections.addAll(childHits);
                }
            }

            return intersections;
        }

        return null;
    }
}
