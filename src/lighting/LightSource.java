package lighting;

import primitives.Color;
import primitives.*;


/**
 * Interface representing a light source in a 3D scene.
 * All light sources should implement this interface to provide their specific behavior.
 *
 * @author Hila Rosental & Miller
 */
public interface LightSource {

    /**
     * Returns the intensity of the light at a given point in space.
     *
     * @param p the point in space
     * @return the color/intensity of the light at that point
     */
    Color getIntensity(Point p);

    /**
     * Returns the direction vector from the light source to a given point in space.
     *
     * @param p the point in space
     * @return the direction vector from the light source to the point
     */
    Vector getL(Point p);


    /**
     * Returns the distance from the light source to a given point in space.
     *
     * @param point the point in space
     * @return the distance from the light source to the point
     */
    double getDistance(Point point);

    /**
     * Returns the radius of the light source area for soft shadows.
     * Default is 0 for non-area lights.
     *
     * @return the radius of the light source area
     */
    default double getRadius() {
        return 0;
    }

    /**
     * Returns the position of the light source in space.
     * This is used for point lights and spotlights.
     *
     * @return the position of the light source
     */
    Point getPosition();
}
