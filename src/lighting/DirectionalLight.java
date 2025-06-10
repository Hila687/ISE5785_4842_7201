package lighting;

import primitives.Color;
import primitives.*;

/**
 * DirectionalLight represents a light source that shines in a constant direction,
 * like sunlight. It has no position (i.e., it's considered to be infinitely far away),
 * so its intensity does not diminish with distance.
 * @author Hila Rosental & Miller
 */
public class DirectionalLight extends Light implements LightSource {

    /** The fixed direction of the light (normalized) */
    private final Vector direction;

    /**
     * Constructs a directional light with the given intensity and direction.
     *
     * @param intensity the color intensity of the light
     * @param direction the direction the light is coming from
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**
     * Returns the light intensity at the given point.
     * Since the light has no position, intensity is constant.
     *
     * @param p the point where intensity is evaluated (ignored)
     * @return the constant light intensity
     */
    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    /**
     * Returns the direction from the light to the point.
     * For directional light, this is constant and independent of the point.
     *
     * @param p the point to which the light direction is requested (ignored)
     * @return the direction vector of the light
     */
    @Override
    public Vector getL(Point p) {
        return direction;
    }

    /**
     * Since directional light is from infinite distance, always returns infinity.
     * @param point the point in space (ignored)
     * @return positive infinity
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns infinity to indicate directional light is not a localized area source.
     * @return positive infinity
     */
    @Override
    public double getRadius() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the position of the light source.
     * For directional light, this is not applicable, so returns null.
     * @return null since directional light has no specific position
     */
    @Override
    public Point getPosition() {
        return null; // Directional light does not have a position
    }
}
