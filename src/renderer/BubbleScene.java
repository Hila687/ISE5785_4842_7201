
package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.Random;

public class BubbleScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Soap Bubble Wonderland")
                .setBackground(Color.BLACK)
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30)));

        Random rand = new Random();

        // Add many small semi-transparent spheres
        for (int i = 0; i < 40; i++) {
            double x = rand.nextDouble() * 200 - 100;
            double y = rand.nextDouble() * 200 - 100;
            double z = rand.nextDouble() * -300;

            double radius = 5 + rand.nextDouble() * 7;

            Color bubbleColor = new Color(
                    50 + rand.nextInt(200),
                    50 + rand.nextInt(200),
                    50 + rand.nextInt(200)
            );

            scene.geometries.add(
                    new Sphere(radius, new Point(x, y, z))
                            .setEmission(bubbleColor)
                            .setMaterial(new Material()
                                    .setKT(0.7)
                                    .setKR(0.2)
                                    .setKD(0.1)
                                    .setKS(0.4)
                                    .setShininess(100))
            );
        }

        // Add soft spot light from above
        scene.lights.add(
                new SpotLight(new Color(700, 700, 1000), new Point(0, 100, 100), new Vector(0, -1, -1))
                        .setKl(0.0005).setKq(0.00005)
        );

        // Build the camera
        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 500))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(500)
                .setVpSize(300, 300)
                .setResolution(800, 800)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        camera.renderImage();
        camera.writeToImage("bubble_wonderland");
    }
}
