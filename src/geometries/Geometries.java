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
public class Geometries extends Container {

    /**
     * A list to hold all the geometric shapes that can be intersected.
     */
    private final List<Container> containerList = new LinkedList<>();

    /**
     * get the containerList
     *
     * @return the containerList
     */
    public List<Container> getContainerList() {
        return containerList;
    }

    /**
     * Default constructor - creates an empty list of geometries.
     */
    public Geometries() {
    }

    /**
     * Constructor that initializes the collection with one or more geometric shapes.
     *
     * @param geometries one or more geometric shapes to be added to the collection.
     */
    public Geometries(Container... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more containers (geometries or groups) to the internal collection.
     *
     * @param geometries containers to be added
     */
    public void add(Container... geometries) {
        Collections.addAll(containerList, geometries);
    }

    /**
     * Adds a list of containers to the internal collection.
     *
     * @param geometries list of containers to be added
     */
    public void add(List<Container> geometries) {
        containerList.addAll(geometries);
    }

    /**
     * Flattens the current list of geometries by unwrapping nested Geometries containers.
     * After calling this method, all direct children will be simple (non-nested) containers.
     */
    public void flatten() {
        // Create a temporary Geometries object to iterate safely
        Geometries temp = new Geometries(containerList.toArray(new Container[0]));

        // Clear the current list to refill it with flat containers
        containerList.clear();

        // Recursively flatten the temp structure into this one
        flatten(temp);
    }

    /**
     * Helper function to recursively flatten a nested Geometries structure.
     *
     * @param geometries the nested Geometries to flatten
     */
    private void flatten(Geometries geometries) {
        for (Container container : geometries.containerList) {
            // If the container is itself a Geometries instance (i.e., a subtree), go deeper
            if (container instanceof Geometries inner) {
                flatten(inner); // Recursive call
            } else {
                // Otherwise, it's a leaf (simple shape) → add it directly
                containerList.add(container);
            }
        }
    }

    /**
     * Removes containers from the internal collection.
     *
     * @param geometries containers to remove
     * @return this object (for chaining)
     */
    public Geometries remove(Container... geometries) {
        containerList.removeAll(List.of(geometries));
        return this;
    }

    /**
     * Enables or disables BVH acceleration recursively for all children.
     * If enabled, sets bounding boxes where possible.
     *
     * @param on true to enable BVH, false to disable
     */
    public void turnOnOffBvh(boolean on) {
        turnOnOffBvh(this, on);
    }

    /**
     * Recursive helper for turnOnOffBvh – applies setting to all nested containers.
     *
     * @param geometries the geometries collection to apply the BVH setting on
     * @param on         true to enable BVH, false to disable
     */
    private void turnOnOffBvh(Geometries geometries, boolean on) {
        for (Container geometry : geometries.containerList) {
            geometry.setBvh(on);
            if (on && geometry.getBoundingBox() == null) {
                geometry.setBoundingBox(); // only when turning ON
            }
            if (geometry instanceof Geometries subGeometries) {
                turnOnOffBvh(subGeometries, on); // recurse deeper
            }
        }
    }

    /**
     * Tries to set a bounding box that contains all bounded geometries.
     * Ignores geometries that cannot be bounded (e.g., infinite surfaces).
     * @return true if all geometries were bounded perfectly, false otherwise
     */
    private boolean setImperfectBoundingBox() {
        boolean isPerfect = true;

        // Start with extreme values
        double xMin = Double.POSITIVE_INFINITY;
        double yMin = Double.POSITIVE_INFINITY;
        double zMin = Double.POSITIVE_INFINITY;
        double xMax = Double.NEGATIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        double zMax = Double.NEGATIVE_INFINITY;

        for (Container geometry : containerList) {
            // If it's a Geometries, try to set its own imperfect bounding box
            if (geometry instanceof Geometries inner) {
                if (!inner.setImperfectBoundingBox()) {
                    isPerfect = false;
                }
            }

            // Try to set box if not yet defined
            if (geometry.getBoundingBox() == null) {
                geometry.setBoundingBox();
            }

            var box = geometry.getBoundingBox();
            if (box == null) {
                isPerfect = false;
                continue;
            }

            // Update bounding limits
            xMin = Math.min(xMin, box.getMinX());
            yMin = Math.min(yMin, box.getMinY());
            zMin = Math.min(zMin, box.getMinZ());
            xMax = Math.max(xMax, box.getMaxX());
            yMax = Math.max(yMax, box.getMaxY());
            zMax = Math.max(zMax, box.getMaxZ());
        }

        if (xMin == Double.POSITIVE_INFINITY) {
            boundingBox = null;
            return false;
        }

        // Create and set bounding box
        boundingBox = new BoundingBox(
                new Point(xMin, yMin, zMin),
                new Point(xMax, yMax, zMax)
        );

        return isPerfect;
    }

    /**
     * Checks if there are geometries that could not be bounded.
     * If any are found, the bounding box for this node is cleared.
     */
    private void checkForUnBoundability() {
        for (Container geometry : containerList) {
            if (geometry.getBoundingBox() == null) {
                boundingBox = null;
                return;
            }
        }
    }



    /**
     * Builds a BVH tree for the current geometry collection.
     * It flattens the tree, builds a bounding box around all bounded geometries,
     * then recursively groups them into a binary hierarchy.
     */
    public void buildBvhTree() {
        // First, flatten nested geometries into a flat list
        flatten();

        // Set an initial (imperfect) bounding box for this level
        setImperfectBoundingBox();

        // If no bounding box could be created (e.g., only infinite geometries) → do nothing
        if (getBoundingBox() == null) {
            return;
        }

        // Recursively organize geometries into a binary tree of bounding volumes
        buildBvhTree(getBoundingBox());

        // Check for geometries that couldn't be bounded, and reset the box if needed
        checkForUnBoundability();
    }

    /**
     * Recursively builds a BVH tree for the geometries inside the given bounding box.
     * It separates the contained geometries into sub-boxes and wraps them in Geometries nodes.
     *
     * @param box The bounding box that defines the current BVH node
     */
    public void buildBvhTree(BoundingBox box) {
        boolean foundContainedGeometries = false;

        // Check which geometries are fully contained in the current bounding box
        for (Container geometry : containerList) {
            if (geometry instanceof Geometries inner) {
                inner.setImperfectBoundingBox(); // Prepare nested bounding box
            }

            if (geometry.getBoundingBox() == null) {
                geometry.setBoundingBox(); // Try to assign a box if missing
            }

            BoundingBox gBox = geometry.getBoundingBox();
            if (gBox != null && box.contains(gBox)) {
                foundContainedGeometries = true;
            }
        }

        // If no geometry fits in this box, no subdivision needed
        if (!foundContainedGeometries) return;

        // Divide the current bounding box into two sub-boxes
        List<BoundingBox> subBoxes = box.divide(); // Usually splits along the longest axis

        // Recursively build BVH for both sub-boxes
        buildBvhTree(subBoxes.get(0));
        buildBvhTree(subBoxes.get(1));

        // Create a new Geometries group for geometries fully inside this box
        Geometries grouped = new Geometries();
        for (Container geometry : new LinkedList<>(containerList)) {
            BoundingBox gBox = geometry.getBoundingBox();
            if (gBox != null && box.contains(gBox)) {
                grouped.add(geometry);
                containerList.remove(geometry);
            }
        }

        // If any geometries were grouped, wrap them and add back to the main list
        if (!grouped.containerList.isEmpty()) {
            containerList.add(grouped);
        }
    }


    /**
     * Calculates the intersections of a ray with all geometries in the collection.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersections, or null if no intersections were found
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        if (isBvh() && getBoundingBox() != null && !getBoundingBox().intersects(ray, maxDistance)) {
            return null;
        }

        if (containerList.size() == 1 && !(containerList.getFirst() instanceof Geometries)) {
            return containerList.getFirst().calculateIntersections(ray, maxDistance);
        }

        List<Intersection> intersections = null;
        for (Container child : containerList) {
            var childIntersections = child.calculateIntersections(ray, maxDistance);
            if (childIntersections != null && !childIntersections.isEmpty()) {
                if (intersections == null)
                    intersections = new LinkedList<>(childIntersections);
                else
                    intersections.addAll(childIntersections);
            }
        }
        return intersections;
    }

    @Override
    public void setBoundingBox() {
        if (!setImperfectBoundingBox())
            boundingBox = null;
    }

    /**
     * automated build bounding volume hierarchy tree
     * with default edges = false
     * so it will calculate the distance between the edges
     */
    public void buildBinaryBvhTree() {
        buildBinaryBvhTree(false);
    }

    /**
     * automated build bounding volume hierarchy tree
     *
     * @param edges whether to calculate between the edges (true) or centers (false)
     */
    public void buildBinaryBvhTree(boolean edges) {
        boolean on = this.isBvh();
        this.flatten();
        Container[] containersInArray = containerList.toArray(new Container[0]);
        double distanceSquared, best;
        boolean allAreBounded = false;
        Container bestGeometry1, bestGeometry2, geometry1 = null, geometry2 = null;

        while (!allAreBounded) {
            bestGeometry1 = bestGeometry2 = null;
            best = Double.POSITIVE_INFINITY;
            allAreBounded = true;

            for (int i = 0; i < containersInArray.length; i++) {
                geometry1 = containersInArray[i];
                if (geometry1.getBoundingBox() == null)
                    geometry1.setBoundingBox();
                if (geometry1.getBoundingBox() == null)
                    continue;

                for (int j = i + 1; j < containersInArray.length; j++) {
                    geometry2 = containersInArray[j];
                    if (geometry2.getBoundingBox() == null)
                        geometry2.setBoundingBox();
                    if (geometry2.getBoundingBox() == null)
                        continue;

                    // measure the distanceSquared between every couple of bounding boxes
                    distanceSquared = geometry1.boundingBox.boundingBoxDistanceSquared(geometry2.boundingBox, edges);
                    if (distanceSquared < best) {
                        best = distanceSquared;
                        bestGeometry1 = geometry1;
                        bestGeometry2 = geometry2;
                    }
                }
            }

            if (bestGeometry1 != null) {
                allAreBounded = false;
                Geometries combined = new Geometries(bestGeometry1, bestGeometry2);
                add(combined);
                remove(bestGeometry1, bestGeometry2);

                int i;
                for (i = 0; i < containersInArray.length; i++) {
                    if (containersInArray[i].equals(bestGeometry1) || containersInArray[i].equals(bestGeometry2)) {
                        containersInArray[i] = combined;
                        break;
                    }
                }
                for (; i < containersInArray.length; i++) {
                    if (containersInArray[i].equals(bestGeometry1) || containersInArray[i].equals(bestGeometry2)) {
                        break;
                    }
                }

                Container[] newArray = new Container[containersInArray.length - 1];
                for (int k = 0, j = 0; k < containersInArray.length; k++) {
                    if (k != i) {
                        newArray[j++] = containersInArray[k];
                    }
                }

                containersInArray = newArray;
            }
        }
        if (!on)
            turnOnOffBvh(false);
    }

    /**
     * Prints the structure of the BVH tree starting from the current node.
     * This method recursively prints the depth of each node, the number of children,
     * and the bounding box associated with each node.
     *
     * @param depth the current depth in the tree, used for indentation in the output
     */
    @SuppressWarnings("unused")
    public void printTree(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "Node | Depth: " + depth +
                " | Children: " + containerList.size() +
                " | BoundingBox: " + (boundingBox != null ? boundingBox : "null"));
        for (Container child : containerList) {
            if (child instanceof Geometries) {
                ((Geometries) child).printTree(depth + 1);
            } else {
                System.out.println(indent + "  Leaf: " + child.getClass().getSimpleName() +
                        (child.getBoundingBox() != null ? " | BoundingBox: " + child.getBoundingBox() : ""));
            }
        }
    }

}
