package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

/**
 * Class to prepare a room scene with various geometries and materials.
 * This class is used for testing the rendering capabilities of the scene.
 * * @author Hadasa and Sara
 */
public class RoomSceneTest {

    /**
     * default constructor for RoomScene.
     */
    public RoomSceneTest() {
    }

    /**
     * Prepares the room scene with all geometries, materials, and lights.
     *
     * @return a Camera.Builder object configured for the room scene.
     */
    static Camera.Builder prepareRoom() {
        Scene scene = new Scene("RoomSceneCopy")
                .setBackground(new Color(5, 5, 15))
                .setAmbientLight(new AmbientLight(new Color(10, 10, 10)));

        // ===== Materials =====
        Material matteGray = new Material()
                .setKD(0.8)
                .setKS(0.1)
                .setShininess(30); // General matte surfaces (floor, ceiling, back wall, right wall)

        Material wallMaterial = new Material()
                .setKD(0.8)
                .setKS(0.2)
                .setShininess(30); // For detailed wall segments (left wall parts)

        Material floorMaterial = new Material()
                .setKD(0.8)
                .setKS(0.1)
                .setShininess(30)
                .setKR(0.1); // General matte surfaces (floor)

        Material windowFrameMaterial = new Material()
                .setKD(0.5)
                .setKS(0.3)
                .setShininess(100); // For window frames

        Material blindMaterial = new Material()
                .setKD(0.5)
                .setKS(0.2)
                .setShininess(20); // For window blinds

        Material woodMaterial = new Material()
                .setKD(0.7)
                .setKS(0.2)
                .setShininess(50); // For furniture (shelves, table legs)

        Material bookMaterial = new Material()
                .setKD(0.5)
                .setKS(0.3)
                .setShininess(30); // For book covers

        Material dollMaterial = new Material()
                .setKD(0.6)
                .setKS(0.4)
                .setShininess(80); // For all doll parts

        Material tableMaterial = new Material()
                .setKD(0.7)
                .setKS(0.2)
                .setShininess(80); // Table-top

        Material glassMaterial = new Material()
                .setKD(0.05)
                .setKS(0.8)
                .setShininess(150)
                .setKT(0.85); // Transparent glass cup

        Material juiceMaterial = new Material()
                .setKD(0.9)
                .setKS(0.2)
                .setShininess(30); // Orange juice

        Material strawMaterial = new Material()
                .setKD(0.5)
                .setKS(0.4)
                .setShininess(80); // Drinking straw

        Material laptopBaseMaterial = new Material()
                .setKD(0.3)
                .setKS(0.4)
                .setShininess(50); // Laptop base

        Material screenMaterial = new Material()
                .setKD(0.1)
                .setKS(0.3)
                .setShininess(100); // Laptop screen

        Material sphereMat = new Material()
                .setKD(0.3)
                .setKS(0.5)
                .setShininess(300)
                .setKR(0.3); // Decorative sphere (reflective)

        Material metalBase = new Material()
                .setKD(0.4)
                .setKS(0.3)
                .setShininess(100); // Ceiling lamp base


        // ===== Colors =====
        Color wallColor = new Color(80, 80, 80);
        Color blindColor = new Color(30, 30, 30);
        Color frameColor = new Color(40, 20, 20); // dark brown/black
        Color dollSkin = new Color(240, 200, 180);                   // Face skin tone
        Color tableColor = new Color(100, 55, 25);                   // Main table color
        Color juiceColor = new Color(180, 80, 20);                   // Orange juice
        Color strawColor = new Color(250, 200, 230);                 // Pink straw

        Geometries geometries = new Geometries();
        geometries.add(
                new Plane(new Point(0, -35, 0), new Vector(0, 1, 0))     // Floor
                        .setEmission(new Color(150, 75, 20))
                        .setMaterial(floorMaterial),

                new Plane(new Point(0, 50, 0), new Vector(0, -1, 0))     // Ceiling
                        .setEmission(new Color(70, 70, 70))
                        .setMaterial(matteGray),

                new Plane(new Point(0, 0, -45), new Vector(0, 0, 1))     // Back wall
                        .setEmission(new Color(90, 90, 90))
                        .setMaterial(matteGray),

                new Plane(new Point(50, 0, 0), new Vector(-1, 0, 0))     // Right wall
                        .setEmission(new Color(120, 200, 240))
                        .setMaterial(matteGray)
        );


        // Bottom segment under window
        geometries.add(new Polygon(
                new Point(-50, -35, -10),
                new Point(-50, -35, -40),
                new Point(-50, 2, -40),
                new Point(-50, 2, -10))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // Top segment above window
        geometries.add(new Polygon(
                new Point(-50, 28, -10),
                new Point(-50, 28, -40),
                new Point(-50, 50, -40),
                new Point(-50, 50, -10))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // Right strip next to window
        geometries.add(new Polygon(
                new Point(-50, -35, -50),
                new Point(-50, -35, -40),
                new Point(-50, 50, -40),
                new Point(-50, 50, -50))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // Left strip next to window
        geometries.add(new Polygon(
                new Point(-50, -35, -10),
                new Point(-50, -35, 0),
                new Point(-50, 50, 0),
                new Point(-50, 50, -10))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // ===== Window Frame =====

        // Top
        geometries.add(new Polygon(
                new Point(-49.95, 28, -12),
                new Point(-49.95, 28, -38),
                new Point(-49.95, 29, -38),
                new Point(-49.95, 29, -12))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // Bottom
        geometries.add(new Polygon(
                new Point(-49.95, 1, -12),
                new Point(-49.95, 1, -38),
                new Point(-49.95, 2, -38),
                new Point(-49.95, 2, -12))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // Left
        geometries.add(new Polygon(
                new Point(-49.95, 2, -38),
                new Point(-49.95, 28, -38),
                new Point(-49.95, 28, -40),
                new Point(-49.95, 2, -40))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // Right
        geometries.add(new Polygon(
                new Point(-49.95, 1.4, -10),
                new Point(-49.95, 28.6, -10),
                new Point(-49.95, 28.6, -12),
                new Point(-49.95, 1.4, -12))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // ===== Blinds =====
        double yStart = 4;
        double yEnd = 26;
        double step = 2.5;
        double thickness = 0.6;

        for (double y = yStart; y <= yEnd; y += step) {
            geometries.add(new Polygon(
                    new Point(-49.91, y, -12),
                    new Point(-49.91, y, -38),
                    new Point(-49.91, y + thickness, -38),
                    new Point(-49.91, y + thickness, -12))
                    .setEmission(blindColor).setMaterial(blindMaterial));
        }

        // ===== Sky Patch (visible through window) =====
        geometries.add(new Polygon(
                new Point(-50, -20, -44.9),
                new Point(-50, 40, -44.9),
                new Point(-130, 40, -44.9),
                new Point(-130, -20, -44.9))
                .setEmission(new Color(120, 180, 255))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10)));


        // ===== Furniture =====

        // ===== Picture Frame =====
        geometries.add(new Polygon(
                new Point(13, 10, -44.95),
                new Point(35, 10, -44.95),
                new Point(35, 40, -44.95),
                new Point(13, 40, -44.95))
                .setEmission(new Color(10, 10, 10)) // dark frame
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(100)));

        // Inner background (light paper)
        geometries.add(new Polygon(
                new Point(15, 12, -44.9),
                new Point(33, 12, -44.9),
                new Point(33, 38, -44.9),
                new Point(15, 38, -44.9))
                .setEmission(new Color(200, 200, 200))
                .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(30)));

        // Mountain (three dark triangles)
        geometries.add(new Polygon(
                new Point(17, 18, -44.85),
                new Point(21, 28, -44.85),
                new Point(25, 22, -44.85))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)));

        geometries.add(new Polygon(
                new Point(25, 22, -44.85),
                new Point(27, 30, -44.85),
                new Point(31, 18, -44.85))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)));

        geometries.add(new Polygon(
                new Point(17, 18, -44.85),
                new Point(25, 22, -44.85),
                new Point(31, 18, -44.85))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)));

        // Sun above mountain
        geometries.add(new Sphere(
                1.3, new Point(24, 32, -44.85))
                .setEmission(new Color(50, 50, 50))
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(50)));

        // Legs (4 thin cylinders)
        geometries.add(
                new Cylinder(0.8, new Ray(new Point(-39, -35, -30), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-20, -35, -30), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-40, -35, -40), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-21, -35, -40), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial)
        );

        // Bottom panel
        geometries.add(new Polygon(
                new Point(-40, -30, -30),
                new Point(-20, -30, -30),
                new Point(-20, -30, -40),
                new Point(-40, -30, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Middle shelf
        geometries.add(new Polygon(
                new Point(-40, -15, -30),
                new Point(-20, -15, -30),
                new Point(-20, -15, -40),
                new Point(-40, -15, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Top shelf
        geometries.add(new Polygon(
                new Point(-40, -5, -30),
                new Point(-20, -5, -30),
                new Point(-20, -5, -40),
                new Point(-40, -5, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );


        // Left side wall
        geometries.add(new Polygon(
                new Point(-40, -30, -30),
                new Point(-40, -5, -30),
                new Point(-40, -5, -40),
                new Point(-40, -30, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Right side wall
        geometries.add(new Polygon(
                new Point(-20, -30, -30),
                new Point(-20, -5, -30),
                new Point(-20, -5, -40),
                new Point(-20, -30, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Back panel
        geometries.add(new Polygon(
                new Point(-40, -30, -40),
                new Point(-20, -30, -40),
                new Point(-20, -5, -40),
                new Point(-40, -5, -40))
                .setEmission(new Color(90, 50, 25))
                .setMaterial(woodMaterial)
        );

        // Top front strip (optional visual detail)
        geometries.add(new Polygon(
                new Point(-40, -5, -30),
                new Point(-20, -5, -30),
                new Point(-20, -4, -30),
                new Point(-40, -4, -30))
                .setEmission(new Color(110, 65, 35))
                .setMaterial(woodMaterial)
        );

        // ===== Books on Middle Shelf =====
        // Book 1
        geometries.add(new Polygon(
                new Point(-22, -15, -31),
                new Point(-21, -15, -31),
                new Point(-21, -5, -31),
                new Point(-22, -5, -31))
                .setEmission(new Color(60, 20, 20))  // Dark red
                .setMaterial(bookMaterial)
        );

        // Book 2
        geometries.add(new Polygon(
                new Point(-23, -15, -31),
                new Point(-22, -15, -31),
                new Point(-22, -6, -31),
                new Point(-23, -6, -31))
                .setEmission(new Color(20, 60, 20))  // Dark green
                .setMaterial(bookMaterial)
        );

        // Book 3
        geometries.add(new Polygon(
                new Point(-24.2, -15, -31),
                new Point(-23.2, -15, -31),
                new Point(-23.2, -7, -31),
                new Point(-24.2, -7, -31))
                .setEmission(new Color(20, 20, 60))  // Dark blue
                .setMaterial(bookMaterial)
        );

        // Book 4
        geometries.add(new Polygon(
                new Point(-25.3, -15, -31),
                new Point(-24.5, -15, -31),
                new Point(-24.5, -6.5, -31),
                new Point(-25.3, -6.5, -31))
                .setEmission(new Color(100, 100, 30))  // Yellowish
                .setMaterial(bookMaterial)
        );


        // ===== Dolls (3 sizes) =====

        // Large doll
        geometries.add(
                new Sphere(1.8, new Point(-34, -2.2, -35)) // body
                        .setEmission(new Color(180, 60, 60)).setMaterial(dollMaterial),
                new Sphere(1.0, new Point(-34, 0.3, -34.8) ) // face
                        .setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(1.3, new Point(-34, 0.3, -35.3) ) // hood
                        .setEmission(new Color(180, 60, 60)).setMaterial(dollMaterial));

        // Medium doll
        geometries.add(
                new Sphere(1.4, new Point(-31.3, -2.5, -35)) // body
                        .setEmission(new Color(60, 140, 200)).setMaterial(dollMaterial),
                new Sphere(0.7, new Point(-31.3, -0.2, -34.8)) // face
                        .setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(1.0, new Point(-31.3, -0.2, -35.3)) // hood
                        .setEmission(new Color(60, 140, 200)).setMaterial(dollMaterial));

        // Small doll
        geometries.add(
                new Sphere(1.0, new Point(-29, -2.8, -35)) // body
                        .setEmission(new Color(240, 200, 60)).setMaterial(dollMaterial),
                new Sphere(0.5, new Point(-29, -1.1, -34.8)) // face
                        .setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(0.67, new Point(-29, -1.1, -35.3)) // hood
                        .setEmission(new Color(240, 200, 60)).setMaterial(dollMaterial));
        // ===== Tape Device =====

        // Body
        geometries.add(new Polygon(
                new Point(-37, -30, -35.1),
                new Point(-23, -30, -35.1),
                new Point(-23, -24, -35.1),
                new Point(-37, -24, -35.1))
                .setEmission(new Color(70, 70, 70))
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(40)));

        // Left speaker
        scene.geometries.add(new Sphere(
                1.25, new Point(-34, -27, -35))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(20)));

        // Right speaker
        scene.geometries.add(new Sphere(
                1.25, new Point(-26, -27, -35))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(20)));

        // Antenna
        scene.geometries.add(new Cylinder(
                0.15, new Ray(new Point(-23.5, -24, -35), new Vector(0, 1, 0)), 3)
                .setEmission(new Color(150, 150, 150))
                .setMaterial(new Material().setKD(0.4).setKS(0.5).setShininess(100)));

        // ===== Table =====
        geometries.add(
                new Cylinder(0.9, new Ray(new Point(-17, -35, -13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(17, -35, -13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(-17, -35, 13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(17, -35, 13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial)
        );


        geometries.add(new Polygon(
                new Point(-20, -17, -15),
                new Point(20, -17, -15),
                new Point(20, -17, 15),
                new Point(-20, -17, 15))
                .setEmission(tableColor)
                .setMaterial(tableMaterial)
        );


        geometries.add(new Polygon(
                new Point(-20, -18, -15),
                new Point(20, -18, -15),
                new Point(20, -18, 15),
                new Point(-20, -18, 15))
                .setEmission(tableColor)
                .setMaterial(tableMaterial)
        );


        // ===== Transparent Glass with Orange Juice =====
        scene.geometries.add(new Cylinder(2.5, new Ray(new Point(10, -17.5, 5), new Vector(0, 1, 0)), 6)
                .setEmission(new Color(100, 130, 180)) // glass tint
                .setMaterial(glassMaterial));

        scene.geometries.add(new Cylinder(2.2, new Ray(new Point(10, -17.5, 5), new Vector(0, 1, 0)), 4.8)
                .setEmission(juiceColor)
                .setMaterial(juiceMaterial));

        // Straw
        scene.geometries.add(new Cylinder(0.15,
                new Ray(new Point(10.5, -12.5, 5), new Vector(0.3, 1, 0).normalize()), 6)
                .setEmission(strawColor)
                .setMaterial(strawMaterial));

        // ===== Laptop =====

        // Keyboard base
        geometries.add(new Polygon(
                new Point(-14, -16.5, 4),
                new Point(-0.5, -16.5, 4),
                new Point(-0.5, -16.5, -1),
                new Point(-14, -16.5, -1))
                .setEmission(new Color(30, 30, 30))
                .setMaterial(laptopBaseMaterial));

        // Screen front
        geometries.add(new Polygon(
                new Point(-13.7, -16.3, -1),
                new Point(-0.7, -16.3, -1),
                new Point(-0.7, -10.505, -4),
                new Point(-13.7, -10.505, -4))
                .setEmission(new Color(80, 180, 255))
                .setMaterial(screenMaterial));

        // Screen back
        geometries.add(new Polygon(
                new Point(-14, -16.5, -1),
                new Point(-0.5, -16.5, -1),
                new Point(-0.5, -10.5, -4),
                new Point(-14, -10.5, -4))
                .setEmission(new Color(30, 30, 30))
                .setMaterial(screenMaterial));

        // Windows logo (Z = -2)
        double z = -2;

        geometries.add(new Polygon( // Red
                new Point(-8.0, -12.2, z),
                new Point(-7.2, -12.2, z),
                new Point(-7.2, -12.9, z),
                new Point(-8.0, -12.9, z))
                .setEmission(new Color(255, 0, 0)).setMaterial(screenMaterial));

        geometries.add(new Polygon( // Yellow
                new Point(-6.95, -12.2, z),
                new Point(-6.15, -12.2, z),
                new Point(-6.15, -12.9, z),
                new Point(-6.95, -12.9, z))
                .setEmission(new Color(255, 204, 0)).setMaterial(screenMaterial));

        geometries.add(new Polygon( // Blue
                new Point(-8.0, -13.05, z),
                new Point(-7.2, -13.05, z),
                new Point(-7.2, -13.85, z),
                new Point(-8.0, -13.85, z))
                .setEmission(new Color(0, 120, 215)).setMaterial(screenMaterial));

        geometries.add(new Polygon( // Green
                new Point(-6.95, -13.05, z),
                new Point(-6.15, -13.05, z),
                new Point(-6.15, -13.85, z),
                new Point(-6.95, -13.85, z))
                .setEmission(new Color(0, 204, 0)).setMaterial(screenMaterial));

        // ===== Decorative Sphere on Floor =====
        scene.geometries.add(new Sphere(7, new Point(-30, -29, 10))
                .setEmission(new Color(120, 120, 255))
                .setMaterial(sphereMat));

        // ===== Ceiling Lamp =====

        // Hanging string
        scene.geometries.add(new Cylinder(0.6, new Ray(new Point(0, 60, 0), new Vector(0, -1, 0)), 25)
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(80)));

        // Glass lamp
        scene.geometries.add(new Sphere(7, new Point(0, 30, 0))
                .setEmission(new Color(1000, 160, 30))
                .setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(150).setKT(1)));

        // Ceiling base
        scene.geometries.add(new Cylinder(4.5, new Ray(new Point(0, 49.9, 0), new Vector(0, -1, 0)), 1)
                .setEmission(new Color(20, 20, 20))
                .setMaterial(metalBase));

        // ===== Lights =====
        //lamp light (simulated using SpotLight for a focused beam)
        scene.lights.add(new SpotLight(
                new Color(1000, 700, 500),
                new Point(0, 30, 0),
                new Vector(0, -1, 0))
                .setKl(0.000000035).setKq(0.0006)
                .setRadius(6));


        // ----------- Sun geometry (outside the room) -----------
        scene.geometries.add(new Sphere(
                3, new Point(-53, 15, -19))  // שמאלה וגבוה, מחוץ לחדר
                .setEmission(new Color(255, 240, 180))
                .setMaterial(new Material().setKD(0.2).setKS(0.6).setShininess(200))
        );



        scene.geometries.add(geometries);

        // ----------- Sunlight (as SpotLight through the window) -----------
        scene.lights.add(new SpotLight(
                new Color(1500, 1200, 1000),
                new Point(-70, 30, -25),
                new Vector(2.5, -2.5, 0.5))
                .setKl(0.0002).setKq(0.0004)
                .setNarrowBeam(14)
                .setRadius(6)
        );

        return Camera.getBuilder()
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(300)
                .setVpSize(300, 400)
                .setResolution(1000, 1400) //
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setDebugPrint(0.1);
    }


    /**
     * Creates a manual hierarchy scene with a single polygon.
     *
     * @param name    the name of the scene
     * @param bvhMode the BVH mode to use
     * @param threads the number of threads to use for rendering
     */
    void testRoomConfig(String name, Camera.BvhMode bvhMode, int threads) {
        Camera.Builder builder;
        builder = bvhMode == Camera.BvhMode.HIERARCHY_MANUAL
                ? Camera.getBuilder()
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(300)
                .setVpSize(300, 400)
                .setResolution(1000, 1400)
                .setRayTracer(createManualHierarchyScene(), RayTracerType.SIMPLE)
                .setDebugPrint(0.1)
                : prepareRoom();

        builder.setBvhMode(bvhMode)
                .setMultithreading(threads);

        Camera camera = builder.build();
        long start = System.nanoTime();
        camera.renderImage();
        long end = System.nanoTime();
        System.out.println(name + " Render time: " + ((end - start) / 1_000_000.0) + " ms");
        camera.writeToImage(name);
    }


    /**
     * Tests the room configuration with no BVH and no multithreading.
     */
    @Test
    void room_noBVH_noMT() {
        testRoomConfig("room_noBVH_noMT", Camera.BvhMode.OFF, 0);
    }

    /**
     * Tests the room configuration with no BVH and multithreading.
     */
    @Test
    void room_noBVH_MT() {
        testRoomConfig("room_noBVH_MT", Camera.BvhMode.OFF, -1);
    }

    /**
     * Tests the room configuration with CBR and no multithreading.
     */
    @Test
    void room_CBR_noMT() {
        testRoomConfig("room_CBR_noMT", Camera.BvhMode.CBR, 0);
    }

    /**
     * Tests the room configuration with CBR and multithreading.
     */
    @Test
    void room_CBR_MT() {
        testRoomConfig("room_CBR_MT", Camera.BvhMode.CBR, -1);
    }

    /**
     * Tests the room configuration with manual BVH and no multithreading.
     */
    @Test
    void room_BVH_MANUAL_noMT() {
        testRoomConfig("room_BVH_MANUAL_noMT", Camera.BvhMode.HIERARCHY_MANUAL, 0);
    }

    /**
     * Tests the room configuration with manual BVH and multithreading.
     */
    @Test
    void room_BVH_MANUAL_MT() {
        testRoomConfig("room_BVH_MANUAL_MT", Camera.BvhMode.HIERARCHY_MANUAL, -1);
    }

    /**
     * Tests the room configuration with automatic BVH and no multithreading.
     */
    @Test
    void room_BVH_AUTO_noMT() {
        testRoomConfig("room_BVH_AUTO_noMT", Camera.BvhMode.HIERARCHY_AUTO, 0);
    }

    /**
     * Tests the room configuration with automatic BVH and multithreading.
     */
    @Test
    void room_BVH_AUTO_MT() {
        testRoomConfig("room_BVH_AUTO_MT", Camera.BvhMode.HIERARCHY_AUTO, -1);
    }

    @Test
    @Disabled
    void roomTest() {
        room_noBVH_noMT();
        room_noBVH_MT();
        room_CBR_noMT();
        room_CBR_MT();
        room_BVH_MANUAL_noMT();
        room_BVH_MANUAL_MT();
        room_BVH_AUTO_noMT();
        room_BVH_AUTO_MT();
    }

    /**
     * Creates a scene with a manual hierarchy.
     *
     * @return a Scene object with a manual hierarchy.
     */
    public static Scene createManualHierarchyScene() {
        Scene scene = new Scene("RoomSceneCopy")
                .setBackground(new Color(5, 5, 15))
                .setAmbientLight(new AmbientLight(new Color(10, 10, 10)));

        // ===== Materials =====
        Material floorMaterial = new Material().setKD(0.8).setKS(0.1).setShininess(30).setKR(0.1);
        Material matteGray = new Material().setKD(0.8).setKS(0.1).setShininess(30);
        Material wallMaterial = new Material().setKD(0.8).setKS(0.2).setShininess(30);
        Material windowFrameMaterial = new Material().setKD(0.5).setKS(0.3).setShininess(100);
        Material blindMaterial = new Material().setKD(0.5).setKS(0.2).setShininess(20);
        Material woodMaterial = new Material().setKD(0.7).setKS(0.2).setShininess(50);
        Material bookMaterial = new Material().setKD(0.5).setKS(0.3).setShininess(30);
        Material dollMaterial = new Material().setKD(0.6).setKS(0.4).setShininess(80);
        Material tableMaterial = new Material().setKD(0.7).setKS(0.2).setShininess(80);
        Material glassMaterial = new Material().setKD(0.05).setKS(0.8).setShininess(150).setKT(0.85);
        Material juiceMaterial = new Material().setKD(0.9).setKS(0.2).setShininess(30);
        Material strawMaterial = new Material().setKD(0.5).setKS(0.4).setShininess(80);
        Material laptopBaseMaterial = new Material().setKD(0.3).setKS(0.4).setShininess(50);
        Material screenMaterial = new Material().setKD(0.1).setKS(0.3).setShininess(100);
        Material sphereMat = new Material().setKD(0.3).setKS(0.5).setShininess(300).setKR(0.3);
        Material metalBase = new Material().setKD(0.4).setKS(0.3).setShininess(100);

        // ===== Colors =====
        Color wallColor = new Color(80, 80, 80);
        Color blindColor = new Color(30, 30, 30);
        Color frameColor = new Color(40, 20, 20);
        Color dollSkin = new Color(240, 200, 180);
        Color tableColor = new Color(100, 55, 25);
        Color juiceColor = new Color(180, 80, 20);
        Color strawColor = new Color(250, 200, 230);

        // ===== Walls =====
        Geometries walls = new Geometries(
                new Plane(new Point(0, -35, 0), new Vector(0, 1, 0)).setEmission(new Color(150, 75, 20)).setMaterial(floorMaterial),
                new Plane(new Point(0, 50, 0), new Vector(0, -1, 0)).setEmission(new Color(70, 70, 70)).setMaterial(matteGray),
                new Plane(new Point(0, 0, -45), new Vector(0, 0, 1)).setEmission(new Color(90, 90, 90)).setMaterial(matteGray),
                new Plane(new Point(50, 0, 0), new Vector(-1, 0, 0)).setEmission(new Color(120, 200, 240)).setMaterial(matteGray)
        );

        // ===== Left Wall & Window =====
        Geometries leftWall = new Geometries(
                // Bottom segment under window
                new Polygon(new Point(-50, -35, -10), new Point(-50, -35, -40), new Point(-50, 2, -40), new Point(-50, 2, -10))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                // Top segment above window
                new Polygon(new Point(-50, 28, -10), new Point(-50, 28, -40), new Point(-50, 50, -40), new Point(-50, 50, -10))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                // Right strip next to window
                new Polygon(new Point(-50, -35, -50), new Point(-50, -35, -40), new Point(-50, 50, -40), new Point(-50, 50, -50))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                // Left strip next to window
                new Polygon(new Point(-50, -35, -10), new Point(-50, -35, 0), new Point(-50, 50, 0), new Point(-50, 50, -10))
                        .setEmission(wallColor).setMaterial(wallMaterial)
        );

        // ===== Window Frame =====
        Geometries windowFrame = new Geometries(
                new Polygon(new Point(-49.95, 28, -12), new Point(-49.95, 28, -38), new Point(-49.95, 29, -38), new Point(-49.95, 29, -12))
                        .setEmission(frameColor).setMaterial(windowFrameMaterial),
                new Polygon(new Point(-49.95, 1, -12), new Point(-49.95, 1, -38), new Point(-49.95, 2, -38), new Point(-49.95, 2, -12))
                        .setEmission(frameColor).setMaterial(windowFrameMaterial),
                new Polygon(new Point(-49.95, 2, -38), new Point(-49.95, 28, -38), new Point(-49.95, 28, -40), new Point(-49.95, 2, -40))
                        .setEmission(frameColor).setMaterial(windowFrameMaterial),
                new Polygon(new Point(-49.95, 1.4, -10), new Point(-49.95, 28.6, -10), new Point(-49.95, 28.6, -12), new Point(-49.95, 1.4, -12))
                        .setEmission(frameColor).setMaterial(windowFrameMaterial)
        );

        // ===== Blinds =====
        Geometries blinds = new Geometries();
        double yStart = 4, yEnd = 26, step = 2.5, thickness = 0.6;
        for (double y = yStart; y <= yEnd; y += step) {
            blinds.add(new Polygon(
                    new Point(-49.91, y, -12),
                    new Point(-49.91, y, -38),
                    new Point(-49.91, y + thickness, -38),
                    new Point(-49.91, y + thickness, -12)
            ).setEmission(blindColor).setMaterial(blindMaterial));
        }

        // ===== Sky Patch (for window) =====
        Geometries sky = new Geometries(
                new Polygon(new Point(-50, -20, -44.9), new Point(-50, 40, -44.9), new Point(-130, 40, -44.9), new Point(-130, -20, -44.9))
                        .setEmission(new Color(120, 180, 255)).setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10))
        );

        // ===== Picture Frame =====
        Geometries pictureFrame = new Geometries(
                new Polygon(new Point(13, 10, -44.95), new Point(35, 10, -44.95), new Point(35, 40, -44.95), new Point(13, 40, -44.95))
                        .setEmission(new Color(10, 10, 10)).setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(100)),
                new Polygon(new Point(15, 12, -44.9), new Point(33, 12, -44.9), new Point(33, 38, -44.9), new Point(15, 38, -44.9))
                        .setEmission(new Color(200, 200, 200)).setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(30)),
                // Mountains, sun, etc...
                new Polygon(new Point(17, 18, -44.85), new Point(21, 28, -44.85), new Point(25, 22, -44.85))
                        .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)),
                new Polygon(new Point(25, 22, -44.85), new Point(27, 30, -44.85), new Point(31, 18, -44.85))
                        .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)),
                new Polygon(new Point(17, 18, -44.85), new Point(25, 22, -44.85), new Point(31, 18, -44.85))
                        .setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)),
                new Sphere( 1.3,new Point(24, 32, -44.85))
                        .setEmission(new Color(50, 50, 50)).setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(50))
        );

        // ===== Books =====
        Geometries books = new Geometries(
                new Polygon(new Point(-22, -15, -31), new Point(-21, -15, -31), new Point(-21, -5, -31), new Point(-22, -5, -31))
                        .setEmission(new Color(60, 20, 20)).setMaterial(bookMaterial),
                new Polygon(new Point(-23, -15, -31), new Point(-22, -15, -31), new Point(-22, -6, -31), new Point(-23, -6, -31))
                        .setEmission(new Color(20, 60, 20)).setMaterial(bookMaterial),
                new Polygon(new Point(-24.2, -15, -31), new Point(-23.2, -15, -31), new Point(-23.2, -7, -31), new Point(-24.2, -7, -31))
                        .setEmission(new Color(20, 20, 60)).setMaterial(bookMaterial),
                new Polygon(new Point(-25.3, -15, -31), new Point(-24.5, -15, -31), new Point(-24.5, -6.5, -31), new Point(-25.3, -6.5, -31))
                        .setEmission(new Color(100, 100, 30)).setMaterial(bookMaterial)
        );

        // ===== Dolls =====
        Geometries dolls = new Geometries(
                // Large doll
                new Sphere(1.8,new Point(-34, -2.2, -35)).setEmission(new Color(180, 60, 60)).setMaterial(dollMaterial),
                new Sphere(1.0, new Point(-34, 0.3, -34.8)).setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(1.3,new Point(-34, 0.3, -35.3)).setEmission(new Color(180, 60, 60)).setMaterial(dollMaterial),
                // Medium doll
                new Sphere(1.4, new Point(-31.3, -2.5, -35)).setEmission(new Color(60, 140, 200)).setMaterial(dollMaterial),
                new Sphere(0.7, new Point(-31.3, -0.2, -34.8)).setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(1.0, new Point(-31.3, -0.2, -35.3)).setEmission(new Color(60, 140, 200)).setMaterial(dollMaterial),
                // Small doll
                new Sphere(1.0, new Point(-29, -2.8, -35)).setEmission(new Color(240, 200, 60)).setMaterial(dollMaterial),
                new Sphere(0.5, new Point(-29, -1.1, -34.8)).setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(0.67, new Point(-29, -1.1, -35.3)).setEmission(new Color(240, 200, 60)).setMaterial(dollMaterial)
        );

        // ===== Furniture (shelves, table, etc) =====
        Geometries furniture = new Geometries(
                // Shelves, panels...
                new Cylinder(0.8, new Ray(new Point(-39, -35, -30), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-20, -35, -30), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-40, -35, -40), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-21, -35, -40), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                // Bottom/mid/top panels
                new Polygon(new Point(-40, -30, -30), new Point(-20, -30, -30), new Point(-20, -30, -40), new Point(-40, -30, -40)).setEmission(new Color(100, 60, 30)).setMaterial(woodMaterial),
                new Polygon(new Point(-40, -15, -30), new Point(-20, -15, -30), new Point(-20, -15, -40), new Point(-40, -15, -40)).setEmission(new Color(100, 60, 30)).setMaterial(woodMaterial),
                new Polygon(new Point(-40, -5, -30), new Point(-20, -5, -30), new Point(-20, -5, -40), new Point(-40, -5, -40)).setEmission(new Color(100, 60, 30)).setMaterial(woodMaterial),
                new Polygon(new Point(-40, -30, -30), new Point(-40, -5, -30), new Point(-40, -5, -40), new Point(-40, -30, -40)).setEmission(new Color(100, 60, 30)).setMaterial(woodMaterial),
                new Polygon(new Point(-20, -30, -30), new Point(-20, -5, -30), new Point(-20, -5, -40), new Point(-20, -30, -40)).setEmission(new Color(100, 60, 30)).setMaterial(woodMaterial),
                new Polygon(new Point(-40, -30, -40), new Point(-20, -30, -40), new Point(-20, -5, -40), new Point(-40, -5, -40)).setEmission(new Color(90, 50, 25)).setMaterial(woodMaterial),
                new Polygon(new Point(-40, -5, -30), new Point(-20, -5, -30), new Point(-20, -4, -30), new Point(-40, -4, -30)).setEmission(new Color(110, 65, 35)).setMaterial(woodMaterial)
        );

        // ===== Table (legs, top, etc) =====
        Geometries table = new Geometries(
                new Cylinder(0.9, new Ray(new Point(-17, -35, -13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(17, -35, -13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(-17, -35, 13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(17, -35, 13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Polygon(new Point(-20, -17, -15), new Point(20, -17, -15), new Point(20, -17, 15), new Point(-20, -17, 15)).setEmission(tableColor).setMaterial(tableMaterial),
                new Polygon(new Point(-20, -18, -15), new Point(20, -18, -15), new Point(20, -18, 15), new Point(-20, -18, 15)).setEmission(tableColor).setMaterial(tableMaterial)
        );

        // ===== Laptop (keyboard, screen, logo) =====
        Geometries laptop = new Geometries(
                new Polygon(new Point(-14, -16.5, 4), new Point(-0.5, -16.5, 4), new Point(-0.5, -16.5, -1), new Point(-14, -16.5, -1)).setEmission(new Color(30, 30, 30)).setMaterial(laptopBaseMaterial),
                new Polygon(new Point(-13.7, -16.3, -1), new Point(-0.7, -16.3, -1), new Point(-0.7, -10.505, -4), new Point(-13.7, -10.505, -4)).setEmission(new Color(80, 180, 255)).setMaterial(screenMaterial),
                new Polygon(new Point(-14, -16.5, -1), new Point(-0.5, -16.5, -1), new Point(-0.5, -10.5, -4), new Point(-14, -10.5, -4)).setEmission(new Color(30, 30, 30)).setMaterial(screenMaterial),
                // Logo (red, yellow, blue, green)
                new Polygon(new Point(-8.0, -12.2, -2), new Point(-7.2, -12.2, -2), new Point(-7.2, -12.9, -2), new Point(-8.0, -12.9, -2)).setEmission(new Color(255, 0, 0)).setMaterial(screenMaterial),
                new Polygon(new Point(-6.95, -12.2, -2), new Point(-6.15, -12.2, -2), new Point(-6.15, -12.9, -2), new Point(-6.95, -12.9, -2)).setEmission(new Color(255, 204, 0)).setMaterial(screenMaterial),
                new Polygon(new Point(-8.0, -13.05, -2), new Point(-7.2, -13.05, -2), new Point(-7.2, -13.85, -2), new Point(-8.0, -13.85, -2)).setEmission(new Color(0, 120, 215)).setMaterial(screenMaterial),
                new Polygon(new Point(-6.95, -13.05, -2), new Point(-6.15, -13.05, -2), new Point(-6.15, -13.85, -2), new Point(-6.95, -13.85, -2)).setEmission(new Color(0, 204, 0)).setMaterial(screenMaterial)
        );

        // ===== Glass and Juice =====
        Geometries glassAndJuice = new Geometries(
                new Cylinder(2.5, new Ray(new Point(10, -17.5, 5), new Vector(0, 1, 0)), 6).setEmission(new Color(100, 130, 180)).setMaterial(glassMaterial),
                new  Cylinder(2.2, new Ray(new Point(10, -17.5, 5), new Vector(0, 1, 0)), 4.8).setEmission(juiceColor).setMaterial(juiceMaterial),
                new Cylinder(0.15, new Ray(new Point(10.5, -12.5, 5), new Vector(0.3, 1, 0).normalize()), 6).setEmission(strawColor).setMaterial(strawMaterial)
        );

        // ===== Decorative Sphere =====
        Geometries decorations = new Geometries(
                new Sphere(7, new Point(-30, -29, 10)).setEmission(new Color(120, 120, 255)).setMaterial(sphereMat)
        );

        // ===== Ceiling Lamp =====
        Geometries ceilingLamp = new Geometries(
                new Cylinder(0.6, new Ray(new Point(0, 60, 0), new Vector(0, -1, 0)), 25).setEmission(new Color(20, 20, 20)).setMaterial(new Material().setKD(0.3).setKS(0.2).setShininess(80)),
                new Sphere(7, new Point(0, 30, 0)).setEmission(new Color(1000, 160, 30)).setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(150).setKT(1)),
                new Cylinder(4.5, new Ray(new Point(0, 49.9, 0), new Vector(0, -1, 0)), 1).setEmission(new Color(20, 20, 20)).setMaterial(metalBase)
        );

        // ===== Sun (outside) =====
        Geometries sun = new Geometries(
                new Sphere(3, new Point(-53, 15, -19)).setEmission(new Color(255, 240, 180)).setMaterial(new Material().setKD(0.2).setKS(0.6).setShininess(200))
        );

        // ========== הרכבת כל הקבוצות לעץ ראשי ==========
        Geometries roomHierarchy = new Geometries(
                walls, leftWall, windowFrame, blinds, sky, pictureFrame, books, dolls, furniture, table, laptop, glassAndJuice, decorations, ceilingLamp, sun
        );

        scene.geometries.add(roomHierarchy);

        // ===== Lights =====
        scene.lights.add(new SpotLight(
                new Color(1000, 700, 500),
                new Point(0, 30, 0),
                new Vector(0, -1, 0))
                .setKl(0.000000035).setKq(0.0006)
                .setRadius(6));
        scene.lights.add(new SpotLight(
                new Color(1500, 1200, 1000),
                new Point(-70, 30, -25),
                new Vector(2.5, -2.5, 0.5))
                .setKl(0.0002).setKq(0.0004)
                .setNarrowBeam(14)
                .setRadius(6)
        );
        return scene;
    }
}


