package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric shape in 3D space.
 * All geometric shapes must implement a method to return the normal vector at a given point.
 */
public abstract class Geometry extends Intersectable {

    protected Color emission = Color.BLACK; // Default emission color

    private Material material = new Material(); // Default material
    /**
     * Sets the material of the geometry.
     *
     * @param material the material to set
     * @return the current geometry instance
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Returns the material of the geometry.
     *
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission the emission color to set
     * @return the current geometry instance
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Returns the emission color of the geometry.
     *
     * @return the emission color
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Returns the normal vector to the geometry at the specified point.
     *
     * @param p1 the point on the geometry surface
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point p1);
}