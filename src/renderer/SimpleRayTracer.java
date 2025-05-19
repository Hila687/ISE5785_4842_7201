package renderer;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static primitives.Util.isZero;

/**
 * SimpleRayTracer is a basic implementation of a ray tracer that renders a scene.
 * It extends the RayTracerBase class and provides functionality to trace rays through the scene.
 */
public class SimpleRayTracer extends RayTracerBase {
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersection> intersectionPoints = scene.geometries.calculateIntersections(ray);
        if (intersectionPoints == null || intersectionPoints.isEmpty()) {
            return scene.backgroundColor;
        }
        Intersection closestIntersection = ray.findClosestIntersection(intersectionPoints);
        return calcColor(closestIntersection, ray);
    }

    public Color calcColor(Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }
        Color ambientIntensity = scene.ambientLight.getIntensity();
        Double3 ka = intersection.geometry.getMaterial().KA;
        Color ambientLight = ambientIntensity.scale(ka);
        Color localEffects = calcColorLocalEffects(intersection);
        return ambientLight.add(localEffects);
    }

    public boolean preprocessIntersection(Intersection intersection, Vector v) {
        if (intersection == null) {
            return false;
        }
        intersection.v = v;
        intersection.n = intersection.geometry.getNormal(intersection.point);
        intersection.nv = intersection.n.dotProduct(v);
        return !isZero(intersection.nv);
    }

    public boolean setLightSource(Intersection intersection, LightSource lightSource) {
        if (intersection == null || lightSource == null) {
            return false;
        }
        intersection.light = lightSource;
        intersection.l = lightSource.getL(intersection.point);
        intersection.nl = intersection.n.dotProduct(intersection.l);
        return !(isZero(intersection.nv) && isZero(intersection.nl));
    }

    private Color calcColorLocalEffects(Intersection intersection) {
        Color color = intersection.geometry.getEmission();

        for (LightSource lightSource : scene.lights) {
            if (!setLightSource(intersection, lightSource)) {
                continue;
            }
            Color lightIntensity = lightSource.getIntensity(intersection.point);
            Double3 diffusive = calcDiffusive(intersection);
            Double3 specular = calcSpecular(intersection);
            Double3 contribution = diffusive.add(specular);
            color = color.add(lightIntensity.scale(contribution));
        }

        return color;
    }

    private Double3 calcSpecular(Intersection intersection) {
        Vector r = intersection.l.subtract(intersection.n.scale(2 * intersection.nl)).normalize();
        double vr = -intersection.v.dotProduct(r);
        if (vr <= 0) return Double3.ZERO;
        double specularFactor = Math.pow(vr, intersection.material.nSh);
        return intersection.material.KS.scale(specularFactor);
    }

    private Double3 calcDiffusive(Intersection intersection) {
        if (intersection.nl <= 0) return Double3.ZERO;
        return intersection.material.KD.scale(intersection.nl);
    }
}
