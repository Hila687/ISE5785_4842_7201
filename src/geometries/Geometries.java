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
 *
 * @author Hila Rosental & Hila Miller
 */
public class Geometries extends Intersectable {

    /**
     * Internal list of geometries that implement {@link Intersectable}.
     */
    private final List<Intersectable> IntersectableList = new LinkedList<>();

    /**
     * Default constructor - creates an empty list of geometries.
     */
    public Geometries() {
    }

    /**
     * get the IntersectableList
     *
     * @return the IntersectableList
     */
    public List<Intersectable> getIntersectableList() {
        return IntersectableList;
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
        IntersectableList.addAll(List.of(geometries));
    }

    /**
     * Add geometries to the list
     *
     * @param geometries list of geometries
     */
    public void add(List<Intersectable> geometries) {
        IntersectableList.addAll(geometries);
    }




    /**
     * Calculates the intersections of a ray with all geometries in the collection.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersections, or null if no intersections were found
     */
    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> intersections = null;
        for (Intersectable child : IntersectableList) {
            var childIntersections = child.calculateIntersections(ray, maxDistance);
            if (childIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(childIntersections);
                else
                    intersections.addAll(childIntersections);
            }
        }
        return intersections;
    }

    /**
     * remove method allow to remove (even zero) geometries from the composite class
     *
     * @param geometries which we want to add to the composite class
     * @return the geometries after the remove
     */
    public Geometries remove(Intersectable... geometries) {
        IntersectableList.removeAll(List.of(geometries));
        return this;
    }
//endregion


    /**
     * method to flatten the geometries list
     */
    public void flatten() {
        Geometries new_geometries = new Geometries(IntersectableList.toArray(new Intersectable[0]));
        IntersectableList.clear();
        // call the second function which will make sure we only
        // have Intersectables with simple instances of geometry
        flatten(new_geometries);
    }

    /**
     * recursive func to flatten the geometries list (break the tree)
     * receives a Geometries instance, flattens it and adds the shapes to this current instance
     *
     * @param new_geometries geometries
     */
    private void flatten(Geometries new_geometries) {
        for (Intersectable Intersectable : new_geometries.IntersectableList) {
            if (Intersectable instanceof Geometries geometries) {

                flatten(geometries);
            } else {
                IntersectableList.add(Intersectable);
            }
        }
    }

    /**
     * turns on or off the bvh acceleration in all the sub geometries using a recursive call
     *
     * @param on whether the bvh should be on or off
     *           if on==true will also generate bounding boxes where it can
     */
    public void turnOnOffBvh(boolean on) {
        turnOnOffBvh(this, on);
    }

    /**
     * turns on or off the bvh acceleration in all the sub geometries using a recursive call
     *
     * @param on         whether the bvh should be on or off
     * @param geometries the current geometries that we are turning on or off the bvh feature
     *                   if on==true will also generate bounding boxes where it can
     */
    private void turnOnOffBvh(Geometries geometries, boolean on) {
        for (Intersectable geometry : geometries.IntersectableList) {
            if (on && geometry.boundingBox == null)
                geometry.setBoundingBox();
            if (geometry instanceof Geometries subGeometries)
                turnOnOffBvh(subGeometries, on);
        }
    }

    /**
     * Builds a bounding volume hierarchy (BVH) tree for the geometries in this object.
     * This method flattens the list of geometries, sets an imperfect bounding box,
     * and then recursively builds the BVH tree starting from the bounding box.
     * It also checks for any geometries that could not be bounded by a bounding box.
     */
    public void buildBvhTree() {
        flatten();
        setImperfectBoundingBox();
        if (boundingBox == null)
            return;
        buildBvhTree(boundingBox);
        checkForUnBoundability();
    }

    /**
     * Recursively builds the BVH tree for the geometries in this object
     * within a specified bounding box. The method divides the given
     * bounding box into sub-boxes and assigns the geometries to these
     * sub-boxes based on their bounding volumes.
     *
     * @param box the bounding box that contains the geometries to be organized
     *            into a BVH tree. The method will attempt to place geometries into
     *            sub-boxes and recursively build the BVH tree for each sub-box.
     */
    public void buildBvhTree( BoundingBox box) {
        boolean areThereAny = false;

        // Check which geometries can be contained within the bounding box
        for (var geometry : getIntersectableList()) {
            if (geometry instanceof Geometries innerGeometries)
                innerGeometries.setImperfectBoundingBox();

            if (geometry.boundingBox == null)
                geometry.setBoundingBox();
            if (geometry.boundingBox == null)
                continue;

            if (box.contain(geometry.boundingBox))
                areThereAny = true;
        }

        if (!areThereAny)
            return;


        List<BoundingBox> subBoxes = box.divide();

        buildBvhTree(subBoxes.getFirst()); // Recursively build BVH tree for the first sub-box
        buildBvhTree(subBoxes.getLast()); // Recursively build BVH tree for the last sub-box

        Geometries forThisBox = new Geometries(); // Intersectable for geometries within this box

        // Assign geometries to this bounding box if they are contained within it
        for (var geometry : getIntersectableList()) {
            if (geometry.boundingBox == null)
                geometry.setBoundingBox(); // Ensure the geometry has a bounding box

            if (box.contain(geometry.boundingBox)) {
                forThisBox.add(geometry); // Add geometry to this box's Intersectable
            }
        }
        for (var g : forThisBox.getIntersectableList()) {
            remove(g);
        }
        add(forThisBox);
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
        // flatten the list of Geometries
        this.flatten();
        Intersectable[] IntersectablesInArray = IntersectableList.toArray(new Intersectable[0]);
        double distanceSquared, best;
        boolean allAreBounded = false;
        Intersectable bestGeometry1, bestGeometry2, geometry1 = null, geometry2 = null;

        while (!allAreBounded) {
            bestGeometry1 = bestGeometry2 = null;
            best = Double.POSITIVE_INFINITY;
            allAreBounded = true;

            for (int i = 0; i < IntersectablesInArray.length; i++) {
                geometry1 = IntersectablesInArray[i];
                if (geometry1.boundingBox == null)
                    geometry1.setBoundingBox();
                if (geometry1.boundingBox == null)
                    continue;

                for (int j = i + 1; j < IntersectablesInArray.length; j++) {
                    geometry2 = IntersectablesInArray[j];
                    if (geometry2.boundingBox == null)
                        geometry2.setBoundingBox();
                    if (geometry2.boundingBox == null)
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
                //will add and remove from IntersectableList
                add(combined);
                remove(bestGeometry1, bestGeometry2);

                int i;
                for (i = 0; i < IntersectablesInArray.length; i++) {
                    if (IntersectablesInArray[i].equals(bestGeometry1) || IntersectablesInArray[i].equals(bestGeometry2)) {
                        IntersectablesInArray[i] = combined;
                        break;
                    }
                }
                for (; i < IntersectablesInArray.length; i++) {
                    if (IntersectablesInArray[i].equals(bestGeometry1) || IntersectablesInArray[i].equals(bestGeometry2)) {
                        break;
                    }
                }

                Intersectable[] newArray = new Intersectable[IntersectablesInArray.length - 1];
                for (int k = 0, j = 0; k < IntersectablesInArray.length; k++) {
                    if (k != i) {
                        newArray[j++] = IntersectablesInArray[k];
                    }
                }

                IntersectablesInArray = newArray;
            }
        }
    }

    @Override
    public void setBoundingBox() {
        if (!setImperfectBoundingBox())
            boundingBox = null;
    }

    /**
     * set an imperfect bounding box that will contain only bound-able elements and will ignore unbound-able ones
     * important: needs to be used carefully , usually with the second function that will put back null if necessary
     *
     * @return whether the box is actually perfect or it should be null
     */
    private boolean setImperfectBoundingBox() {
        boolean isPerfect = true;
        if (IntersectableList.isEmpty()) {
            boundingBox = null;
            return false;
        }
        double xMin, xMax, yMin, yMax, zMin, zMax;
        xMin = yMin = zMin = Double.POSITIVE_INFINITY;
        xMax = yMax = zMax = Double.NEGATIVE_INFINITY;
        BoundingBox box;
        for (var Intersectable : IntersectableList) {
            if (Intersectable instanceof Geometries innerGeometries) {
                //set the imperfect box for the innerGeometries
                if (!innerGeometries.setImperfectBoundingBox())
                    isPerfect = false;
            } else if (Intersectable.boundingBox == null) {
                Intersectable.setBoundingBox();
            }
            box = Intersectable.boundingBox;

            if (box == null) {
                isPerfect = false;
                continue;
            }
            xMin = Math.min(xMin, box.getMinX());
            yMin = Math.min(yMin, box.getMinY());
            zMin = Math.min(zMin, box.getMinZ());
            xMax = Math.max(xMax, box.getMaxX());
            yMax = Math.max(yMax, box.getMaxY());
            zMax = Math.max(zMax, box.getMaxZ());

        }

        boundingBox = new BoundingBox(
                new Point(xMin, yMin, zMin),
                new Point(xMax, yMax, zMax)
        );

        return isPerfect;
    }

    /**
     * Checks if any geometries in this collection are unboundable.
     * If any geometry is found that cannot be bounded, the bounding box
     * of this Geometries instance is set to null.
     */
    private void checkForUnBoundability() {
        checkForUnBoundability(this);
    }

    /**
     * Recursively checks if any geometries in the given Geometries instance are unboundable.
     * If any geometry is found that cannot be bounded, the bounding box of the Geometries instance
     * is set to null.
     *
     * @param geometries the Geometries instance to check for unboundable geometries
     */
    public void checkForUnBoundability(Geometries geometries) {
        for (Intersectable geometry : geometries.getIntersectableList()) {
            geometry.setBoundingBox();
            if (geometry.boundingBox == null) {
                boundingBox = null;
                return;
            }
        }
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
                " | Children: " + IntersectableList.size() +
                " | BoundingBox: " + (boundingBox != null ? boundingBox : "null"));
        for (Intersectable child : IntersectableList) {
            if (child instanceof Geometries) {
                ((Geometries) child).printTree(depth + 1);
            } else {
                System.out.println(indent + "  Leaf: " + child.getClass().getSimpleName() +
                        (child.boundingBox != null ? " | BoundingBox: " + child.boundingBox : ""));
            }
        }
    }



}