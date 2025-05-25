package lighting;

import primitives.Color;
import primitives.*;

import static primitives.Util.alignZero;

/**
 * SpotLight represents a point light source that emits light in a specific direction,
 * creating a focused beam like a flashlight or spotlight.
 * The beam can be adjusted using a "narrow beam" factor.
 * @author Hila Rosental & Miller
 */
public class SpotLight extends PointLight {

    /** The central direction of the spotlight beam (normalized) */
    private final Vector direction;

    /** Beam narrowing factor – higher values create a tighter, more focused beam */
    private double narrowBeam = 1.0;

    /**
     * Constructs a spotlight with the given intensity, position, and beam direction.
     *
     * @param intensity the color intensity of the light
     * @param position  the origin point of the light
     * @param direction the direction the spotlight is facing
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Sets the constant attenuation factor (kC) using method chaining.
     *
     * @param kC the constant attenuation coefficient
     * @return this spotlight instance (for chaining)
     */
    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    /**
     * Sets the linear attenuation factor (kL) using method chaining.
     *
     * @param kL the linear attenuation coefficient
     * @return this spotlight instance (for chaining)
     */
    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * Sets the quadratic attenuation factor (kQ) using method chaining.
     *
     * @param kQ the quadratic attenuation coefficient
     * @return this spotlight instance (for chaining)
     */
    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * Sets the narrow beam factor that controls how tightly the spotlight is focused.
     *
     * @param narrowBeam a positive value (typically ≥ 1); higher = narrower beam
     * @return this spotlight instance (for chaining)
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = alignZero(narrowBeam);
        return this;
    }

    /**
     * Returns the light intensity at a given point, considering both distance attenuation
     * and spotlight direction (beam focus).
     *
     * @param p the point to evaluate intensity at
     * @return the scaled intensity or black if the point is outside the beam
     */
    @Override
    public Color getIntensity(Point p) {
        // Compute how well the point aligns with the spotlight direction
        double directionFactor = direction.dotProduct(getL(p));

        // If the point lies outside the beam's cone, return no light
        if (directionFactor <= 0) return Color.BLACK;

        // Attenuate intensity by direction and distance
        return super.getIntensity(p).scale(Math.pow(directionFactor, narrowBeam));
    }

    /**
     * Returns the normalized direction vector from the light to the given point.
     *
     * @param p the point to evaluate direction toward
     * @return the normalized light vector L
     */
    @Override
    public Vector getL(Point p) {
        return super.getL(p).normalize(); // Ensures correct normalization
    }
}
