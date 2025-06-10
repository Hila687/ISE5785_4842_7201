package lighting;

import primitives.Color;
import primitives.*;

/**
 * PointLight represents a light source that emits light equally in all directions
 * from a specific position in space. Its intensity diminishes with distance
 * according to a physical attenuation model.
 * @author Hila Rosental & Miller
 */
public class PointLight extends Light implements LightSource {

    /** The position of the point light in space */
    private final Point position;

    /** Constant attenuation factor (default: 1) */
    private double kC = 1;

    /** Linear attenuation factor (default: 0) */
    private double kL = 0;

    /** Quadratic attenuation factor (default: 0) */
    private double kQ = 0;

    /** Radius of the light source area for soft shadows (default: 0) */
    private double radius = 0;


    /**
     * Constructs a point light with the given intensity and position.
     *
     * @param intensity the base intensity (color) of the light
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Returns the position of the point light source.
     * @return the light's position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets the constant attenuation factor (kC).
     *
     * @param kC the constant attenuation coefficient
     * @return the current PointLight instance (for chaining)
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL).
     *
     * @param kL the linear attenuation coefficient
     * @return the current PointLight instance (for chaining)
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ).
     *
     * @param kQ the quadratic attenuation coefficient
     * @return the current PointLight instance (for chaining)
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Returns the attenuated light intensity at the given point.
     *
     * @param p the point where the intensity is evaluated
     * @return the attenuated light intensity
     */
    @Override
    public Color getIntensity(Point p) {
        double d = p.distance(position); // Distance from light to point
        double attenuation = kC + kL * d + kQ * d * d; // Attenuation formula
        return intensity.scale(1 / attenuation);
    }

    /**
     * Returns the normalized direction vector from the light source to the given point.
     *
     * @param p the point to which the direction is calculated
     * @return the direction vector (L)
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

    /**
     * Sets the radius of the light source area for soft shadows.
     * @param radius the radius value to set
     * @return this light (for method chaining)
     */
    public PointLight setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    /**
     * Gets the radius of the light source area.
     * Used for soft shadows calculation.
     * @return the radius
     */
    @Override
    public double getRadius() {
        return radius;
    }
}
