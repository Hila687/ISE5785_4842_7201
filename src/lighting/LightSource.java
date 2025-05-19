package lighting;

import primitives.Color;
import primitives.*;

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
}
