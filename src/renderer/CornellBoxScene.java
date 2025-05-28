
package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

public class CornellBoxScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Cornell Box Inspired")
                .setBackground(new Color(0, 0, 0))
                .setAmbientLight(new AmbientLight(new Color(20, 20, 20)));

        Material wallMat = new Material().setKD(0.8).setKS(0.1).setShininess(50);
        Material whiteMat = new Material().setKD(0.9).setKS(0.1).setShininess(100);
        Material cubeMat = new Material().setKD(0.7).setKS(0.2).setShininess(80);
        Material sphereMat = new Material().setKD(0.3).setKS(0.5).setShininess(300).setKR(0.3);

        // Floor
        scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                .setEmission(new Color(100, 100, 100))
                .setMaterial(cubeMat));

        // Ceiling
        scene.geometries.add(new Plane(new Point(0, 100, 0), new Vector(0, -1, 0))
                .setEmission(new Color(100, 100, 100))
                .setMaterial(cubeMat));


        // Back Wall
        scene.geometries.add(new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                .setEmission(new Color(130, 130, 130))
                .setMaterial(whiteMat));

        // Left Wall (Red)
        scene.geometries.add(new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0))
                .setEmission(new Color(150, 30, 30))
                .setMaterial(wallMat));

        // Right Wall (Green)
        scene.geometries.add(new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))
                .setEmission(new Color(30, 150, 30))
                .setMaterial(wallMat));

        // Cube (center-left)
        Point p1 = new Point(-50, -50, -120);
        Point p2 = new Point(-10, -50, -120);
        Point p3 = new Point(-10, -10, -120);
        Point p4 = new Point(-50, -10, -120);
        Point p5 = new Point(-50, -50, -160);
        Point p6 = new Point(-10, -50, -160);
        Point p7 = new Point(-10, -10, -160);
        Point p8 = new Point(-50, -10, -160);

        scene.geometries.add(
            new Polygon(p1, p2, p3, p4).setEmission(new Color(140, 70, 12)).setMaterial(cubeMat), // front
            new Polygon(p5, p6, p7, p8).setEmission(new Color(200, 200, 200)).setMaterial(cubeMat), // back
            new Polygon(p1, p2, p6, p5).setEmission(new Color(200, 200, 200)).setMaterial(cubeMat), // bottom
            new Polygon(p4, p3, p7, p8).setEmission(new Color(200, 200, 200)).setMaterial(cubeMat), // top
            new Polygon(p2, p3, p7, p6).setEmission(new Color(200, 200, 200)).setMaterial(cubeMat), // right
            new Polygon(p1, p4, p8, p5).setEmission(new Color(200, 200, 200)).setMaterial(cubeMat)  // left
        );

        // Sphere (right side)
        scene.geometries.add(new Sphere(20, new Point(40, -30, -110))
                .setEmission(new Color(120, 120, 255))
                .setMaterial(sphereMat));
// ===== חוט תליה =====
        scene.geometries.add(
                new Cylinder(0.3, new Ray(new Point(0, 99, -100), new Vector(0, -1, 0)), 0.5)
                        .setEmission(new Color(80, 80, 80))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(80))
        );

// ===== כדור אור גדול מהתקרה =====
        scene.geometries.add(
                new Sphere(17, new Point(0, 70, -100))  // רדיוס 3.5 במקום 4 או 20
                        .setEmission(new Color(240, 240, 220))  // נראה מואר, בלי לשרוף
                        .setMaterial(new Material().setKD(0.4).setKS(0.4).setShininess(250).setKT(0.8))
        );

// ===== מקור אור מתוך הכדור =====
        scene.lights.add(
                new PointLight(new Color(700, 650, 600), new Point(0, 88.5, -100)) // לא בדיוק במרכז
                        .setKl(0.0015).setKq(0.0004)
        );


//        // ===== כדור אור משתלשל מהתקרה =====
//
//// החוט (גליל דק)
//        scene.geometries.add(
//                new Cylinder(0.3, new Ray(new Point(0, 95, -100), new Vector(0, -1, 0)), 10)
//                        .setEmission(new Color(100, 100, 100))
//                        .setMaterial(new Material().setKD(0.4).setKS(0.2).setShininess(100))
//        );
//
//// הכדור עצמו
//        scene.geometries.add(
//                new Sphere(20, new Point(0, 85, -100))
//                        .setEmission(new Color(255, 255, 200)) // צבע חמים
//                        .setMaterial(new Material().setKD(0.3).setKS(0.6).setShininess(300))
//        );
//
//// מקור אור מהכדור כלפי מטה
//        scene.lights.add(new SpotLight(new Color(500, 500, 400),
//                new Point(0, 85, -100),  // מאותו מיקום של הכדור
//                new Vector(0, -1, 0))
//                .setKl(0.0015).setKq(0.0003));

         //Ceiling Light (simulated using PointLight for simplicity)
        scene.lights.add(new SpotLight(new Color(400, 400, 400),  // היה 1000
                new Point(0, 95, -100), new Vector(0, -1, 0))
                .setKl(0.002).setKq(0.0003));  // הגברנו דעיכה


        // Camera
        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(300)
                .setVpSize(300, 400)
                .setResolution(1000, 1400)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        camera.renderImage();
        camera.writeToImage("cornell_box_scene");
    }
}
