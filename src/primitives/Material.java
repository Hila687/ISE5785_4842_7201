package primitives;

public class Material {
    public Double3 KA = Double3.ONE; // Ambient reflection coefficient

    public Double3 KD = Double3.ZERO; // Diffuse reflection coefficient

    public Double3 KS = Double3.ZERO; // Specular reflection coefficient

    public int nSh = 0; // Shininess coefficient

    public Material setKA(Double3 KA) {
        this.KA = KA;
        return this;
    }

    public Material setKa(double ka) {
        this.KA = new Double3(ka);
        return this;
    }

    public Material setKD(Double3 KD) {
        this.KD = KD;
        return this;
    }

    public Material setKD(double kd) {
        this.KD = new Double3(kd);
        return this;
    }

    public Material setKS(Double3 KS) {
        this.KS = KS;
        return this;
    }

    public Material setKS(double ks) {
        this.KS = new Double3(ks);
        return this;
    }

    public Material setShininess(int nSh) {
        this.nSh = nSh;
        return this;
    }
}
