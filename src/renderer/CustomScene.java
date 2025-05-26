package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import static java.awt.Color.WHITE;
import static primitives.Util.*; // אם יש שימוש ב-Util

public class CustomScene {
    public static void main(String[] args) {
        // Step 1: יצירת סצנה
        Scene scene = new Scene("Custom Effects Scene")
                .setBackground(new Color(20, 30, 50)) // רקע כהה
                .setAmbientLight(new AmbientLight(new Color(255, 255, 255).scale(0.1)));

        // Step 2: הוספת גיאומטריות
        scene.geometries.add(
                // מישור משתקף
                new Plane(new Point(0, -100, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(40, 40, 40))
                        .setMaterial(new Material().setKR(0.5)),

                // כדור שקוף בלבד
                new Sphere(30, new Point(-40, 0, -100))
                        .setEmission(new Color(0, 100, 150))
                        .setMaterial(new Material().setKT(0.6).setKD(0.3).setKS(0.2).setShininess(100)),

                // כדור משתקף בלבד
                new Sphere(30, new Point(50, 0, -120))
                        .setEmission(new Color(100, 30, 30))
                        .setMaterial(new Material().setKR(0.8).setKS(0.4).setShininess(150)),

                // משולש אטום
                new Triangle(
                        new Point(-80, 40, -150),
                        new Point(-20, 60, -150),
                        new Point(-50, 100, -150)
                ).setEmission(new Color(20, 80, 20))
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(60))
        );

        // Step 3: הוספת מקור אור
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 600), new Point(0, 100, 50), new Vector(0, -1, -1))
                        .setKl(0.0001).setKq(0.00001)
        );

        // Step 4: בניית מצלמה בעזרת Builder
        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 500))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(500)
                .setVpSize(200, 200)
                .setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        // Step 5: רנדור ושמירת תמונה
        camera.renderImage();
        camera.writeToImage("custom_scene");
    }
}
