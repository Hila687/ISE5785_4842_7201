package lighting;

import primitives.Color;

/**
 * Represents ambient light in a 3D scene.
 * Ambient light provides a uniform base illumination that affects all objects equally,
 * regardless of their position or orientation.
 */
public class AmbientLight {

    /**
     * A constant representing no ambient light (black color).
     * Can be used as a default value when no ambient illumination is desired.
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * The intensity (color) of the ambient light.
     */
    private final Color intensity;

    /**
     * Constructs an ambient light source with the specified intensity.
     *
     * @param IA the color/intensity of the ambient light
     */
    public AmbientLight(Color IA) {
        this.intensity = IA;
    }

    /**
     * Returns the intensity of the ambient light.
     *
     * @return the color representing the ambient light's intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}