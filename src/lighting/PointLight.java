package lighting;

import primitives.Color;
import primitives.*;

public class PointLight extends Light implements LightSource {
    private final Point position;
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    public PointLight(Color intensity, Point position) {
        super(intensity); // אם יש ctor ב־Light
        this.position = position;
    }

    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }


    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = p.distance(position);
        double attenuation = kC + kL * d + kQ * d * d;
        return intensity.scale(1 / attenuation);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }



}
