package primitives;

/**
 * The Material class represents the optical properties of a surface material
 * including ambient, diffuse, specular reflection, shininess,
 * transparency, and reflection attenuation.
 * @author Hila Rosental & Miller
 */
public class Material {

    /**
     * Ambient reflection coefficient (kA).
     */
    public Double3 KA = Double3.ONE;

    /**
     * Diffuse reflection coefficient (kD).
     */
    public Double3 KD = Double3.ZERO;

    /**
     * Specular reflection coefficient (kS).
     */
    public Double3 KS = Double3.ZERO;

    /**
     * Shininess coefficient (nSh).
     */
    public int nSh = 0;

    /**
     * Transparency attenuation coefficient (kT).
     */
    public Double3 kT = Double3.ZERO;

    /**
     * Reflection attenuation coefficient (kR).
     */
    public Double3 kR = Double3.ZERO;

    /**
     * Sets the ambient reflection coefficient.
     *
     * @param KA the ambient reflection coefficient
     * @return the current Material object for chaining
     */
    public Material setKA(Double3 KA) {
        this.KA = KA;
        return this;
    }

    /**
     * Sets the ambient reflection coefficient from a scalar.
     *
     * @param ka the scalar ambient reflection coefficient
     * @return the current Material object for chaining
     */
    public Material setKa(double ka) {
        this.KA = new Double3(ka);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient.
     *
     * @param KD the diffuse reflection coefficient
     * @return the current Material object for chaining
     */
    public Material setKD(Double3 KD) {
        this.KD = KD;
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient from a scalar.
     *
     * @param kd the scalar diffuse reflection coefficient
     * @return the current Material object for chaining
     */
    public Material setKD(double kd) {
        this.KD = new Double3(kd);
        return this;
    }

    /**
     * Sets the specular reflection coefficient.
     *
     * @param KS the specular reflection coefficient
     * @return the current Material object for chaining
     */
    public Material setKS(Double3 KS) {
        this.KS = KS;
        return this;
    }

    /**
     * Sets the specular reflection coefficient from a scalar.
     *
     * @param ks the scalar specular reflection coefficient
     * @return the current Material object for chaining
     */
    public Material setKS(double ks) {
        this.KS = new Double3(ks);
        return this;
    }

    /**
     * Sets the shininess coefficient.
     *
     * @param nSh the shininess coefficient
     * @return the current Material object for chaining
     */
    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient.
     *
     * @param kT the transparency attenuation coefficient
     * @return the current Material object for chaining
     */
    public Material setKT(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient from a scalar.
     *
     * @param kt the scalar transparency attenuation coefficient
     * @return the current Material object for chaining
     */
    public Material setKT(double kt) {
        this.kT = new Double3(kt);
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient.
     *
     * @param kR the reflection attenuation coefficient
     * @return the current Material object for chaining
     */
    public Material setKR(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient from a scalar.
     *
     * @param kr the scalar reflection attenuation coefficient
     * @return the current Material object for chaining
     */
    public Material setKR(double kr) {
        this.kR = new Double3(kr);
        return this;
    }
}
