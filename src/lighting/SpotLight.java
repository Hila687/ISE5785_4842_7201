package lighting;

import primitives.Color;
import primitives.*;

import static primitives.Util.alignZero;

public class SpotLight extends PointLight {
    private final Vector direction;

    private double narrowBeam = 1.0; // Default value for narrow beam effect

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        // Calculate the distance from the light to the point
        double directionFactor = direction.dotProduct(getL(p));
        // If the angle is too wide, return black (no light)
        if (directionFactor <= 0) return Color.BLACK;
        // Calculate the distance from the light to the point
        return super.getIntensity(p).scale(Math.pow(directionFactor, narrowBeam));
    }

    @Override
    public Vector getL(Point p) {
        return super.getL(p).normalize();
    }

    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = alignZero(narrowBeam);
        return this;
    }


}
