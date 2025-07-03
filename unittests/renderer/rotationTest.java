package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

/**
 * Artistic rendering of a mystical forest scene.
 * Demonstrates use of reflection, transparency, lighting, shadows and diverse geometry.
 * Created as a creative variation of the complex scene example.
 */
public class rotationTest {
    private final Scene scene = new Scene("Mystic Forest");

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(scene, RayTracerType.SIMPLE);

    @Test
    void renderMysticForestScene() {
        prepareForestScene();
        cameraBuilder
                .setLocation(new Point(0, 100, 500))
                .setDirection(new Point(0, 0, 0))
                .setVpDistance(600)
                .setVpSize(400, 300)
                .setResolution(1000, 750)
                .setMultithreading(-1)
                .build()
                .renderImage()
                .writeToImage("mysticForestScene");
    }

    private void prepareForestScene() {
        // Ambient light to simulate soft forest lighting
        scene.setAmbientLight(new AmbientLight(new Color(12, 18, 15)));

        // Sunlight streaming through trees
        scene.lights.add(new SpotLight(new Color(1000, 950, 900),
                new Point(0, 300, 400),
                new Vector(0, -1, -1))
                .setKl(0.0002)
                .setKq(0.00002));

        // Magical glow lights
        scene.lights.add(new PointLight(new Color(100, 200, 255), new Point(100, 50, 100))
                .setKl(0.0003).setKq(0.00004));
        scene.lights.add(new PointLight(new Color(255, 120, 180), new Point(-120, 70, -90))
                .setKl(0.0002).setKq(0.00002));

        // Forest floor
        scene.geometries.add(new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                .setEmission(new Color(30, 50, 30))
                .setMaterial(new Material().setKR(0.1).setKD(0.4).setKS(0.3).setShininess(50)));

        // Path through the forest made of polygons
        for (int i = -3; i <= 3; i++) {
            scene.geometries.add(new Polygon(
                    new Point(i * 20 - 10, -99.9, -200 + i * 60),
                    new Point(i * 20 + 10, -99.9, -200 + i * 60),
                    new Point(i * 20 + 10, -99.9, -160 + i * 60),
                    new Point(i * 20 - 10, -99.9, -160 + i * 60))
                    .setEmission(new Color(70, 50, 30))
                    .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(30)));
        }

        // Trees - cylinders
        for (int x = -150; x <= 150; x += 60) {
            for (int z = -250; z <= 250; z += 100) {
                scene.geometries.add(new Cylinder(6,
                        new Ray(new Point(x, -100, z), new Vector(0, 1, 0)), 200)
                        .setEmission(new Color(60, 40, 20))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(60)));

                // Foliage spheres
                scene.geometries.add(new Sphere(25, new Point(x, 120, z))
                        .setEmission(new Color(30, 100, 40))
                        .setMaterial(new Material().setKD(0.3).setKS(0.4).setKR(0.2).setShininess(70)));
            }
        }

        // Floating glowing orbs (magic)
        scene.geometries.add(new Sphere(15, new Point(-50, 50, 0))
                .setEmission(new Color(120, 200, 255))
                .setMaterial(new Material().setKT(0.6).setKD(0.2).setKS(0.5).setShininess(90)));

        scene.geometries.add(new Sphere(10, new Point(70, 60, -40))
                .setEmission(new Color(255, 180, 120))
                .setMaterial(new Material().setKT(0.5).setKD(0.2).setKS(0.3).setShininess(80)));

        // Small reflective crystal
        scene.geometries.add(new Sphere(12, new Point(0, -90, -100))
                .setEmission(new Color(100, 200, 220))
                .setMaterial(new Material().setKR(0.7).setKD(0.1).setKS(0.9).setShininess(200)));
    }

    @Test
    void renderMysticForestMultiAngles() {
        prepareForestScene();

        Point target = new Point(0, 0, 0.1);

        Camera cam0 = cameraBuilder
                .setLocation(new Point(0, 100, 500))
                .setDirection(target)
                .setVpDistance(600).setVpSize(400, 300)
                .setResolution(800, 600)
                .setBvhMode(Camera.BvhMode.HIERARCHY_MANUAL)
                .setMultithreading(-1)
                .build();
        cam0.renderImage().writeToImage("mysticForest_front");

        Camera cam1 = Camera.rotateCameraAroundTarget(cam0, target, 45, scene).build();
        cam1.renderImage().writeToImage("mysticForest_right");

        Camera cam2 = Camera.rotateCameraAroundTarget(cam0, target, -45, scene).build();
        cam2.renderImage().writeToImage("mysticForest_left");

        Camera cam3 = Camera.getBuilder(cam0)
                .rotateAroundVTo(25)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();
        cam3.renderImage().writeToImage("mysticForest_tilted");
    }

}
