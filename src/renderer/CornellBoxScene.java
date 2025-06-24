package renderer;

import geometries.Cylinder;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

public class CornellBoxScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Cornell Box Inspired")
        .setBackground(new Color(5, 5, 15)); // רקע כמעט שחור כחול עמוק
        scene.setAmbientLight(new AmbientLight(new Color(10, 10, 10)));


        Material wallMat = new Material().setKD(0.8).setKS(0.1).setShininess(50);
        Material whiteMat = new Material().setKD(0.9).setKS(0.1).setShininess(100);
        Material cubeMat = new Material().setKD(0.7).setKS(0.2).setShininess(80);
        Material sphereMat = new Material().setKD(0.3).setKS(0.5).setShininess(300).setKR(0.3);
        Material tableMaterial = new Material()
                .setKD(0.8)       // פיזור חזק
                .setKS(0.1)       // ברק נמוך
                .setShininess(30);  // רכות גבוהה, ללא נצנוץ חזק

        // Floor
        scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                .setEmission(new Color(150, 75, 20))
                .setMaterial(cubeMat));

        // Ceiling
        scene.geometries.add(new Plane(new Point(0, 100, 0), new Vector(0, -1, 0))
                .setEmission(new Color(70, 70, 70))
                .setMaterial(cubeMat));


        // Back Wall (Neutral Gray)
        scene.geometries.add(new Plane(new Point(0, 0, -200), new Vector(0, 0, 1))
                .setEmission(new Color(90, 90, 90))  // אפור פחות בהיר
                .setMaterial(whiteMat));

        // Left Wall (Warm Purple)
        scene.geometries.add(new Plane(new Point(-100, 0, 0), new Vector(1, 0, 0))
                .setEmission(new Color(120, 40, 120))  // פחות ורוד, יותר בורדו-סגול
                .setMaterial(wallMat));


        // Right Wall (Cool Blue)
        scene.geometries.add(new Plane(new Point(100, 0, 0), new Vector(-1, 0, 0))
                .setEmission(new Color(40, 120, 160))  // פחות טורקיז בוהק
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
                new Polygon(p1, p2, p3, p4).setEmission(new Color(255, 70, 70)).setMaterial(cubeMat), // front
                new Polygon(p5, p6, p7, p8).setEmission(new Color(255, 70, 70)).setMaterial(cubeMat), // back
                new Polygon(p1, p2, p6, p5).setEmission(new Color(255, 70, 70)).setMaterial(cubeMat), // bottom
                new Polygon(p4, p3, p7, p8).setEmission(new Color(255, 70, 70)).setMaterial(cubeMat), // top
                new Polygon(p2, p3, p7, p6).setEmission(new Color(255, 70, 70)).setMaterial(cubeMat), // right
                new Polygon(p1, p4, p8, p5).setEmission(new Color(255, 70, 70)).setMaterial(cubeMat)  // left
        );

        // ========== FRAME ==========
        scene.geometries.add(new Polygon(
                new Point(15, 20, -199.9),
                new Point(35, 20, -199.9),
                new Point(35, 40, -199.9),
                new Point(15, 40, -199.9))
                .setEmission(new Color(10, 10, 10))  // dark frame
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(100))
        );

// ========== INNER BACKGROUND ==========
        scene.geometries.add(new Polygon(
                new Point(17, 22, -199.8),
                new Point(33, 22, -199.8),
                new Point(33, 38, -199.8),
                new Point(17, 38, -199.8))
                .setEmission(new Color(200, 200, 200))  // light gray paper
                .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(30))
        );

// ========== MOUNTAIN (TRIANGLE) ==========
        // הר ראשון - משולש שמאלי
        scene.geometries.add(
                new Polygon(
                        new Point(18, 24, -199.7),
                        new Point(22, 30, -199.7),
                        new Point(25, 24, -199.7)
                ).setEmission(new Color(40, 40, 40))  // צבע כהה
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(60))
        );

// הר שני - משולש אמצעי גבוה
        scene.geometries.add(
                new Polygon(
                        new Point(25, 24, -199.7),
                        new Point(29, 33, -199.7),
                        new Point(32, 24, -199.7)
                ).setEmission(new Color(40, 40, 40))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(60))
        );

// חיבור בסיס הרים - קו גובה אחיד בין כל ההרים
        scene.geometries.add(
                new Polygon(
                        new Point(22, 24, -199.7),
                        new Point(25, 24.5, -199.7),  // שינינו קצת את הגובה
                        new Point(29, 24.5, -199.7),
                        new Point(25, 24, -199.7)
                ).setEmission(new Color(40, 40, 40))
                        .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(60))
        );


// ========== SUN (SPHERE) ==========
        scene.geometries.add(new Sphere(1, new Point(30, 36, -199.6))  // sun above mountain
                .setEmission(new Color(70, 70, 70))  // soft gray
                .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(20))
        );

        // Sphere (right side)
        scene.geometries.add(new Sphere(20, new Point(40, -30, -110))
                .setEmission(new Color(120, 120, 255))
                .setMaterial(sphereMat));

        // Sphere (right side)
        scene.geometries.add(new Sphere(13, new Point(20, -40, -40))
                .setEmission(new Color(230, 42, 180))
                .setMaterial(sphereMat));
        //===== חוט תליה =====
        scene.geometries.add(
                new Cylinder(0.6, new Ray(new Point(0, 99, -100), new Vector(0, -1, 0)), 30)
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(80))
        );


// ===== כדור אור גדול מהתקרה =====
        scene.geometries.add(
                new Sphere(7, new Point(0, 30, 0))  // רדיוס 3.5 במקום 4 או 20
                        .setEmission(new Color(1000, 160, 30))  // כתום חם
                        .setMaterial(new Material().setKD(0.1)        // מעט פיזור
                                .setKS(0.8)        // הרבה ברק
                                .setShininess(150)
                                .setKT(1))    // שקיפות גבוהה
        );


        // =======================
// Plasma Ball Structure
// =======================

// Outer transparent glowing sphere
        scene.geometries.add(
                new Sphere(15, new Point(50, -35, -120)) // outer sphere
                        .setEmission(new Color(140, 80, 255))  // subtle glow
                        .setMaterial(new Material()
                                .setKD(0.1)       // low diffuse
                                .setKS(0.6)       // medium specular
                                .setShininess(300)
                                .setKT(0.8))      // high transparency
        );

// Optional: inner denser core to simulate energy
        scene.geometries.add(
                new Sphere(5, new Point(50, -35, -120)) // inner glowing core
                        .setEmission(new Color(200, 100, 255)) // bright purple
                        .setMaterial(new Material()
                                .setKD(0.3)
                                .setKS(0.7)
                                .setShininess(200)
                                .setKT(0.3)) // slightly transparent
        );

// Base (dark stand)
        scene.geometries.add(
                new Cylinder(7, new Ray(new Point(50, -50, -120), new Vector(0, 1, 0)), 8)
                        .setEmission(new Color(20, 20, 20)) // dark color
                        .setMaterial(new Material()
                                .setKD(0.4)
                                .setKS(0.1)
                                .setShininess(50))
        );

        scene.lights.add(
                new SpotLight(
                        new Color(500, 200, 800),  // purple glow
                        new Point(50, -35, -120),  // center of plasma ball
                        new Vector(0, -0.3, -0.1)) // slight downward-forward direction
                        .setKl(0.004)
                        .setKq(0.0006).setRadius(3)
        );


        //lamp light (simulated using SpotLight for a focused beam)
        scene.lights.add(new SpotLight(
                new Color(700, 540, 360),  // עוצמה נמוכה יותר
                new Point(0, 30, -65),     // מקור אור
                new Vector(0, -1, 0))      // כיוון כלפי מטה
                .setKl(0.015)              // דעיכה חזקה יותר
                .setKq(0.0005).setRadius(3));            // להפחתת פיזור

        scene.lights.add(new SpotLight(
                new Color(700, 400, 250),  // פחות עוצמה, אור כתום חם
                new Point(0, 30, -60),     // מיקום קרוב יותר למרכז
                new Vector(0, -1, -0.3))   // בזווית קלה כלפי מטה ולפנים
                .setKl(0.02).setKq(0.002).setRadius(3));

        // only english comments
        scene.lights.add(new SpotLight(
                new Color(100, 100, 100),  // נמוך מאוד
                new Point(90, 99, -170), new Vector(0, -1, 0))
                .setKl(0.01).setKq(0.005).setRadius(3));

        // Camera
        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(300)
                .setVpSize(300, 400)
                .setResolution(400, 600)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .build();

        camera.renderImage();
        camera.writeToImage("cornell_box_scene");
    }
}