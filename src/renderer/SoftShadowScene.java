package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

public class SoftShadowScene {
    public static void main(String[] args) {
        Scene scene = new Scene("soft shadow test")
                .setBackground(new Color(200, 200, 200))
                .setAmbientLight(new AmbientLight(new Color(30, 30, 30).scale(0.03)));

        Material material = new Material().setKD(0.7).setKS(0.3).setShininess(60);

        // רצפה אפורה
        scene.geometries.add(
                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1))
                        .setEmission(new Color(70, 70, 70))
                        .setMaterial(material)
        );

        // כדור
        scene.geometries.add(
                new Sphere(40, new Point(-40, -60, 50))
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(material)
        );

        // קובייה - תיבה (6 פאות)
        double cubeSize = 70;
        Point cubeBase = new Point(30, 40, cubeSize/2 );

        Vector[] directions = {
                new Vector(-1, 0, 0), new Vector(1, 0, 0),
                new Vector(0, -1, 0), new Vector(0, 1, 0),
                new Vector(0, 0, -1), new Vector(0, 0, 1)
        };

        for (Vector dir : directions) {
            scene.geometries.add(
                    new Polygon(
                            cubeBase.add(dir.scale(cubeSize / 2).add(new Vector(-cubeSize / 2, -cubeSize / 2, -cubeSize / 2))),
                            cubeBase.add(dir.scale(cubeSize / 2).add(new Vector(cubeSize / 2, -cubeSize / 2, -cubeSize / 2))),
                            cubeBase.add(dir.scale(cubeSize / 2).add(new Vector(cubeSize / 2, cubeSize / 2, -cubeSize / 2))),
                            cubeBase.add(dir.scale(cubeSize / 2).add(new Vector(-cubeSize / 2, cubeSize / 2, -cubeSize / 2)))
                    ).setEmission(new Color(80, 80, 80)).setMaterial(material)
            );
        }

        // גליל
        scene.geometries.add(
                new Cylinder(25, new Ray(new Point(50, -80, -10), new Vector(0, 0, 1)),  110)
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(material)
        );

        // אור נקודתי עם רדיוס (soft shadow)
        scene.lights.add(
                new PointLight(new Color(500, 500, 500), new Point(130, 170, 190))
                        .setRadius(30)
                        .setKl(0.0008)
                        .setKq(0.0001)
        );

        // מצלמה
        Camera camera = new Camera.Builder()
                .setLocation(new Point(70, 80, 320))
                .setDirection(new Point(0, 0, 0), new Vector(0, 0, 1))
                .setVpDistance(330)
                .setVpSize(350, 350)
                .setResolution(500, 500)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(-1)
                .setBvhMode(Camera.BvhMode.HIERARCHY_AUTO)
                .build();

        camera.renderImage();
        camera.writeToImage("SoftShadowBoxSphereCylinder");


    }
}
