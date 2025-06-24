
package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

public class OpticalEffectsShowcase {
    public static void main(String[] args) {
        Scene scene = new Scene("Optical Effects Showcase")
                .setBackground(new Color(10, 10, 10)) // dark background
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30))); // subtle ambient

        // Materials
        Material matte = new Material().setKD(0.5).setKS(0.3).setShininess(100);
        Material glossy = new Material().setKD(0.2).setKS(0.7).setShininess(300);
        Material reflective = new Material().setKD(0.3).setKS(0.4).setShininess(200).setKR(0.6);
        Material transparent = new Material().setKD(0.1).setKS(0.3).setShininess(200).setKT(0.7);
        Material glassy = new Material().setKD(0.1).setKS(0.6).setShininess(400).setKT(0.9).setKR(0.1);

        // Geometries (at least 10)
        scene.geometries.add(
            // Floor
            new Plane(new Point(0, -60, 0), new Vector(0, 1, 0))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(reflective),

            // Rear Wall
            new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                .setEmission(new Color(15, 15, 15))
                .setMaterial(matte),

            // Spheres
            new Sphere(15, new Point(-40, -40, -100)).setEmission(new Color(100, 255, 255)).setMaterial(glassy),
            new Sphere(10, new Point(-10, -40, -80)).setEmission(new Color(255, 0, 0)).setMaterial(matte),
            new Sphere(10, new Point(20, -40, -90)).setEmission(new Color(0, 255, 0)).setMaterial(transparent),
            new Sphere(8, new Point(40, -40, -60)).setEmission(new Color(255, 255, 255)).setMaterial(glossy),

            // Reflective triangle
            new Triangle(new Point(-30, -60, -120), new Point(-10, -30, -100), new Point(-50, -30, -90))
                .setEmission(new Color(255, 255, 200))
                .setMaterial(reflective),

            // Transparent triangle
            new Triangle(new Point(30, -60, -120), new Point(10, -30, -100), new Point(50, -30, -90))
                .setEmission(new Color(255, 200, 255))
                .setMaterial(transparent),

            // Emissive plane (like a screen or portal)
            new Plane(new Point(0, 60, -120), new Vector(0, -1, 0))
                .setEmission(new Color(120, 0, 200))
                .setMaterial(new Material().setKD(0.1).setKS(0.1)),

            // Glass ball in center
            new Sphere(12, new Point(0, -25, -100)).setEmission(new Color(230, 230, 255)).setMaterial(glassy)
        );

        // Lights
        scene.lights.add(
            new SpotLight(new Color(1000, 800, 600),
                    new Point(60, 50, 20),
                    new Vector(-1, -1, -2))
                .setKl(0.0008)
                .setKq(0.0002)
        );

        // Camera
        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(120)
                .setVpSize(200, 200)
                .setResolution(1000, 1000)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        camera.renderImage();
        camera.writeToImage("optical_effects_scene");
    }
}
