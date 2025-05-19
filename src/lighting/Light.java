package lighting;

import primitives.Color;

abstract class Light {
    /**
     * The intensity of the light.
     */
    protected final Color intensity;

    /**
     * Constructs a light with the specified intensity.
     *
     * @param intensity the intensity of the light
     */
    Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Returns the intensity of the light.
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
