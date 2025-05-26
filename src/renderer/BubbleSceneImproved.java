
package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.Random;

public class BubbleSceneImproved {
    public static void main(String[] args) {
        Scene scene = new Scene("Floating Soap Bubbles Improved")
                .setBackground(Color.BLACK)
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30))); // soft ambient

        Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            double x = rand.nextDouble() * 400 - 200;
            double y = rand.nextDouble() * 300 - 150;
            double z = -150 - rand.nextDouble() * 150;

            double radius = 5 + rand.nextDouble() * 20;

            Color color = new Color(
                    50 + rand.nextInt(200),
                    50 + rand.nextInt(200),
                    50 + rand.nextInt(200)
            );

            scene.geometries.add(
                    new Sphere(radius, new Point(x, y, z))
                            .setEmission(color)
                            .setMaterial(new Material()
                                    .setKT(0.8)
                                    .setKR(0.1)
                                    .setKD(0.05)
                                    .setKS(0.6)
                                    .setShininess(200))
            );
        }

        scene.lights.add(
                new SpotLight(new Color(1000, 1000, 1400), new Point(0, 200, 100), new Vector(0, -1, -1))
                        .setKl(0.0004).setKq(0.00002)
        );

        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 500))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(600)
                .setVpSize(500, 500)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        camera.renderImage();
        camera.writeToImage("bubble_wonderland_improved");
    }
}
