package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric shape in 3D space.
 * All geometric shapes must implement a method to return the normal vector at a given point.
 
 */
public abstract class Geometry {

    /**
     * Returns the normal vector to the geometry at the specified point.
     *
     * @param p1 the point on the geometry surface
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p1);
}
