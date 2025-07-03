package renderer;

import geometries.*;
import lighting.*;
import primitives.*;
import scene.*;

public class RoomScene {
    public static void main(String[] args) {
        // ===== Scene Setup =====
        Scene scene = new Scene("room")
                .setBackground(new Color(5, 5, 15)) // deep blue-black background
                .setAmbientLight(new AmbientLight(new Color(10, 10, 10)));

        // ===== Materials =====
        Material matteGray = new Material()
                .setKD(0.8)
                .setKS(0.1)
                .setShininess(30); // General matte surfaces (floor, ceiling, back wall, right wall)

        Material floorMaterial = new Material()
                .setKD(0.8)
                .setKS(0.1)
                .setShininess(30)
                .setKR(0.1); // General matte surfaces (floor)

        Material wallMaterial = new Material()
                .setKD(0.8)
                .setKS(0.2)
                .setShininess(30); // For detailed wall segments (left wall parts)

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
                .setShininess(80); // Table top

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


        // ===== Room Planes =====
        scene.geometries.add(
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

        // ===== Mirror on Right Wall =====
        scene.geometries.add(new Polygon(
                new Point(49.9, -15, -25),
                new Point(49.9, -15, -5),
                new Point(49.9, 30, -5),
                new Point(49.9, 30, -25))
                .setEmission(new Color(30, 30, 30)) // Slightly brighter to help with the effect
                .setMaterial(new Material()
                        .setKD(0.05)     // Minimal diffuse
                        .setKS(1.0)      // Strong specular reflection
                        .setShininess(1500)  // Very sharp highlight
                        .setKR(0.9))     // Reflectivity
        );

        // Mirror Frame Background
        scene.geometries.add(new Polygon(
                new Point(49.95, -16, -26),
                new Point(49.95, -16, -4),
                new Point(49.95, 31, -4),
                new Point(49.95, 31, -26))
                .setEmission(new Color(20, 20, 20)) // Dark gray-black
                .setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(100)));

        // ========== Decorative Plant: Pot + Stem + Branches + Leaves + Flowers ==========

        // plant pot
        scene.geometries.add(new Cylinder(
                3, new Ray(new Point(35, -35, -39), new Vector(0, 1, 0)), 5)
                .setEmission(new Color(120, 60, 40)) // Terracotta color
                .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(50)));

        // main stem
        scene.geometries.add(new Cylinder(
                0.5, new Ray(new Point(35, -30, -39), new Vector(0, 1, 0)), 20)
                .setEmission(new Color(80, 50, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(30)));


        // right branch
        scene.geometries.add(new Cylinder(
                0.3, new Ray(new Point(35, -15, -39), new Vector(0.6, 0.8, 0).normalize()), 7)
                .setEmission(new Color(80, 50, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(30)));



        //right branch flower
        scene.geometries.add(new Sphere(
                0.6, new Point(38.5, -11, -38))
                .setEmission(new Color(255, 100, 150)) // Bright pink
                .setMaterial(new Material().setKD(0.7).setKS(0.4).setShininess(80)));


        // left branch
        scene.geometries.add(new Cylinder(
                0.3, new Ray(new Point(35, -13, -39), new Vector(-0.5, 0.9, 0.2).normalize()), 6)
                .setEmission(new Color(80, 50, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(30)));


        // left branch flower
        scene.geometries.add(new Sphere(
                0.6, new Point(32, -9, -37.7))
                .setEmission(new Color(255, 100, 150))
                .setMaterial(new Material().setKD(0.7).setKS(0.4).setShininess(80)));


        // back branch
        scene.geometries.add(new Cylinder(
                0.3, new Ray(new Point(35, -13.5, -39), new Vector(0, 0.9, -0.5).normalize()), 6.5)
                .setEmission(new Color(80, 50, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(30)));

        // back branch flower
        scene.geometries.add(new Sphere(
                0.6, new Point(35, -8, -43.6))
                .setEmission(new Color(255, 100, 150))
                .setMaterial(new Material().setKD(0.7).setKS(0.4).setShininess(80)));


        // Right leaf – Z = -38, lowered Y
        scene.geometries.add(new Polygon(
                new Point(39, -11.6, -38),
                new Point(40.2, -11, -38),
                new Point(39, -10.4, -38),
                new Point(37.8, -11, -38))  // All Z = -38
                .setEmission(new Color(20, 120, 20)) // Green
                .setMaterial(new Material().setKD(0.9).setKS(0.2).setShininess(40)));


        // Left leaf – Z = -37.7, lowered Y
        scene.geometries.add(new Polygon(
                new Point(31.5, -9.9, -37.7),
                new Point(32.8, -9.1, -37.7),
                new Point(31.5, -8.3, -37.7),
                new Point(30.2, -9.1, -37.7))  // All Z = -37.7
                .setEmission(new Color(20, 120, 20))
                .setMaterial(new Material().setKD(0.9).setKS(0.2).setShininess(40)));


        // Back leaf – Z = -43.6, lowered Y
        scene.geometries.add(new Polygon(
                new Point(35, -9.3, -43),
                new Point(36.2, -8.5, -43),
                new Point(35, -7.7, -43),
                new Point(33.8, -8.5, -43))  // All Z = -43.6
                .setEmission(new Color(20, 120, 20))
                .setMaterial(new Material().setKD(0.9).setKS(0.2).setShininess(40)));


        // ===== Left Wall Segments with Window Opening =====

        // Bottom segment under window
        scene.geometries.add(new Polygon(
                new Point(-50, -35, -10),
                new Point(-50, -35, -40),
                new Point(-50, 2, -40),
                new Point(-50, 2, -10))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // Top segment above window
        scene.geometries.add(new Polygon(
                new Point(-50, 28, -10),
                new Point(-50, 28, -40),
                new Point(-50, 50, -40),
                new Point(-50, 50, -10))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // Right strip next to window
        scene.geometries.add(new Polygon(
                new Point(-50, -35, -50),
                new Point(-50, -35, -40),
                new Point(-50, 50, -40),
                new Point(-50, 50, -50))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // Left strip next to window
        scene.geometries.add(new Polygon(
                new Point(-50, -35, -10),
                new Point(-50, -35, 0),
                new Point(-50, 50, 0),
                new Point(-50, 50, -10))
                .setEmission(wallColor).setMaterial(wallMaterial));

        // ===== Window Frame =====

        // Top
        scene.geometries.add(new Polygon(
                new Point(-49.95, 28, -12),
                new Point(-49.95, 28, -38),
                new Point(-49.95, 29, -38),
                new Point(-49.95, 29, -12))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // Bottom
        scene.geometries.add(new Polygon(
                new Point(-49.95, 1, -12),
                new Point(-49.95, 1, -38),
                new Point(-49.95, 2, -38),
                new Point(-49.95, 2, -12))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // Left
        scene.geometries.add(new Polygon(
                new Point(-49.95, 2, -38),
                new Point(-49.95, 28, -38),
                new Point(-49.95, 28, -40),
                new Point(-49.95, 2, -40))
                .setEmission(frameColor).setMaterial(windowFrameMaterial));

        // Right
        scene.geometries.add(new Polygon(
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
            scene.geometries.add(new Polygon(
                    new Point(-49.91, y, -12),
                    new Point(-49.91, y, -38),
                    new Point(-49.91, y + thickness, -38),
                    new Point(-49.91, y + thickness, -12))
                    .setEmission(blindColor).setMaterial(blindMaterial));
        }

        // ===== Sky Patch (visible through window) =====
        scene.geometries.add(new Polygon(
                new Point(-50, -20, -44.9),
                new Point(-50, 40, -44.9),
                new Point(-130, 40, -44.9),
                new Point(-130, -20, -44.9))
                .setEmission(new Color(120, 180, 255))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(10)));


        // ===== Furniture =====

        // ===== Picture Frame =====
        scene.geometries.add(new Polygon(
                new Point(13, 10, -44.95),
                new Point(35, 10, -44.95),
                new Point(35, 40, -44.95),
                new Point(13, 40, -44.95))
                .setEmission(new Color(10, 10, 10)) // dark frame
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(100)));

        // Inner background (light paper)
        scene.geometries.add(new Polygon(
                new Point(15, 12, -44.9),
                new Point(33, 12, -44.9),
                new Point(33, 38, -44.9),
                new Point(15, 38, -44.9))
                .setEmission(new Color(200, 200, 200))
                .setMaterial(new Material().setKD(0.7).setKS(0.1).setShininess(30)));

        // Mountain (three dark triangles)
        scene.geometries.add(new Polygon(
                new Point(17, 18, -44.85),
                new Point(21, 28, -44.85),
                new Point(25, 22, -44.85))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)));

        scene.geometries.add(new Polygon(
                new Point(25, 22, -44.85),
                new Point(27, 30, -44.85),
                new Point(31, 18, -44.85))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)));

        scene.geometries.add(new Polygon(
                new Point(17, 18, -44.85),
                new Point(25, 22, -44.85),
                new Point(31, 18, -44.85))
                .setEmission(new Color(20, 20, 20))
                .setMaterial(new Material().setKD(0.6).setKS(0.1).setShininess(40)));

        // Sun above mountain
        scene.geometries.add(new Sphere(
                1.3, new Point(24, 32, -44.85))
                .setEmission(new Color(50, 50, 50))
                .setMaterial(new Material().setKD(0.6).setKS(0.2).setShininess(50)));

        // Legs (4 thin cylinders)
        scene.geometries.add(
                new Cylinder(0.8, new Ray(new Point(-39, -35, -30), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-20, -35, -30), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-40, -35, -40), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial),
                new Cylinder(0.8, new Ray(new Point(-21, -35, -40), new Vector(0, 1, 0)), 5).setEmission(new Color(90, 50, 20)).setMaterial(woodMaterial)
        );

        // Bottom panel
        scene.geometries.add(new Polygon(
                new Point(-40, -30, -30),
                new Point(-20, -30, -30),
                new Point(-20, -30, -40),
                new Point(-40, -30, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Middle shelf
        scene.geometries.add(new Polygon(
                new Point(-40, -15, -30),
                new Point(-20, -15, -30),
                new Point(-20, -15, -40),
                new Point(-40, -15, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Top shelf
        scene.geometries.add(new Polygon(
                new Point(-40, -5, -30),
                new Point(-20, -5, -30),
                new Point(-20, -5, -40),
                new Point(-40, -5, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );


        // Left side wall
        scene.geometries.add(new Polygon(
                new Point(-40, -30, -30),
                new Point(-40, -5, -30),
                new Point(-40, -5, -40),
                new Point(-40, -30, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Right side wall
        scene.geometries.add(new Polygon(
                new Point(-20, -30, -30),
                new Point(-20, -5, -30),
                new Point(-20, -5, -40),
                new Point(-20, -30, -40))
                .setEmission(new Color(100, 60, 30))
                .setMaterial(woodMaterial)
        );

        // Back panel
        scene.geometries.add(new Polygon(
                new Point(-40, -30, -40),
                new Point(-20, -30, -40),
                new Point(-20, -5, -40),
                new Point(-40, -5, -40))
                .setEmission(new Color(90, 50, 25))
                .setMaterial(woodMaterial)
        );

        // Top front strip (optional visual detail)
        scene.geometries.add(new Polygon(
                new Point(-40, -5, -30),
                new Point(-20, -5, -30),
                new Point(-20, -4, -30),
                new Point(-40, -4, -30))
                .setEmission(new Color(110, 65, 35))
                .setMaterial(woodMaterial)
        );

        // ===== Books on Middle Shelf =====
        // Book 1
        scene.geometries.add(new Polygon(
                new Point(-22, -15, -31),
                new Point(-21, -15, -31),
                new Point(-21, -5, -31),
                new Point(-22, -5, -31))
                .setEmission(new Color(60, 20, 20))  // Dark red
                .setMaterial(bookMaterial)
        );

        // Book 2
        scene.geometries.add(new Polygon(
                new Point(-23, -15, -31),
                new Point(-22, -15, -31),
                new Point(-22, -6, -31),
                new Point(-23, -6, -31))
                .setEmission(new Color(20, 60, 20))  // Dark green
                .setMaterial(bookMaterial)
        );

        // Book 3
        scene.geometries.add(new Polygon(
                new Point(-24.2, -15, -31),
                new Point(-23.2, -15, -31),
                new Point(-23.2, -7, -31),
                new Point(-24.2, -7, -31))
                .setEmission(new Color(20, 20, 60))  // Dark blue
                .setMaterial(bookMaterial)
        );

        // Book 4
        scene.geometries.add(new Polygon(
                new Point(-25.3, -15, -31),
                new Point(-24.5, -15, -31),
                new Point(-24.5, -6.5, -31),
                new Point(-25.3, -6.5, -31))
                .setEmission(new Color(100, 100, 30))  // Yellowish
                .setMaterial(bookMaterial)
        );


        // ===== Dolls (3 sizes) =====

        // Large doll
        scene.geometries.add(
                new Sphere(1.8, new Point(-34, -2.2, -35)) // body
                        .setEmission(new Color(180, 60, 60)).setMaterial(dollMaterial),
                new Sphere(1.0, new Point(-34, 0.3, -34.8)) // face
                        .setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(1.3, new Point(-34, 0.3, -35.3)) // hood
                        .setEmission(new Color(180, 60, 60)).setMaterial(dollMaterial));

        // Medium doll
        scene.geometries.add(
                new Sphere(1.4, new Point(-31.3, -2.5, -35)) // body
                        .setEmission(new Color(60, 140, 200)).setMaterial(dollMaterial),
                new Sphere(0.7, new Point(-31.3, -0.2, -34.8)) // face
                        .setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(1.0, new Point(-31.3, -0.2, -35.3)) // hood
                        .setEmission(new Color(60, 140, 200)).setMaterial(dollMaterial));

        // Small doll
        scene.geometries.add(
                new Sphere(1.0, new Point(-29, -2.8, -35)) // body
                        .setEmission(new Color(240, 200, 60)).setMaterial(dollMaterial),
                new Sphere(0.5, new Point(-29, -1.1, -34.8)) // face
                        .setEmission(dollSkin).setMaterial(dollMaterial),
                new Sphere(0.67, new Point(-29, -1.1, -35.3)) // hood
                        .setEmission(new Color(240, 200, 60)).setMaterial(dollMaterial));
        // ===== Tape Device =====

        // Body
        scene.geometries.add(new Polygon(
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
        scene.geometries.add(
                new Cylinder(0.9, new Ray(new Point(-17, -35, -13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(17, -35, -13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(-17, -35, 13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial),
                new Cylinder(0.9, new Ray(new Point(17, -35, 13), new Vector(0, 1, 0)), 18).setEmission(tableColor).setMaterial(tableMaterial)
        );


        scene.geometries.add(new Polygon(
                new Point(-20, -17, -15),
                new Point(20, -17, -15),
                new Point(20, -17, 15),
                new Point(-20, -17, 15))
                .setEmission(tableColor)
                .setMaterial(tableMaterial)
        );


        scene.geometries.add(new Polygon(
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
        scene.geometries.add(new Polygon(
                new Point(-14, -16.5, 4),
                new Point(-0.5, -16.5, 4),
                new Point(-0.5, -16.5, -1),
                new Point(-14, -16.5, -1))
                .setEmission(new Color(30, 30, 30))
                .setMaterial(laptopBaseMaterial));

        // Screen front
        scene.geometries.add(new Polygon(
                new Point(-13.7, -16.3, -1),
                new Point(-0.7, -16.3, -1),
                new Point(-0.7, -10.505, -4),
                new Point(-13.7, -10.505, -4))
                .setEmission(new Color(100, 150, 255))
                .setMaterial(screenMaterial));

        // Screen back
        scene.geometries.add(new Polygon(
                new Point(-14, -16.5, -1),
                new Point(-0.5, -16.5, -1),
                new Point(-0.5, -10.5, -4),
                new Point(-14, -10.5, -4))
                .setEmission(new Color(30, 30, 30))
                .setMaterial(screenMaterial));

        // Windows logo (Z = -2)
        double z = -2;

        scene.geometries.add(new Polygon( // Red
                new Point(-8.0, -12.2, z),
                new Point(-7.2, -12.2, z),
                new Point(-7.2, -12.9, z),
                new Point(-8.0, -12.9, z))
                .setEmission(new Color(255, 0, 0)).setMaterial(screenMaterial));

        scene.geometries.add(new Polygon( // Yellow
                new Point(-6.95, -12.2, z),
                new Point(-6.15, -12.2, z),
                new Point(-6.15, -12.9, z),
                new Point(-6.95, -12.9, z))
                .setEmission(new Color(255, 204, 0)).setMaterial(screenMaterial));

        scene.geometries.add(new Polygon( // Blue
                new Point(-8.0, -13.05, z),
                new Point(-7.2, -13.05, z),
                new Point(-7.2, -13.85, z),
                new Point(-8.0, -13.85, z))
                .setEmission(new Color(0, 120, 215)).setMaterial(screenMaterial));

        scene.geometries.add(new Polygon( // Green
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

        // ----------- Sun geometry (outside the room) -----------
        scene.geometries.add(new Sphere(
                3, new Point(-53, 15, -19))  // שמאלה וגבוה, מחוץ לחדר
                .setEmission(new Color(255, 240, 180))
                .setMaterial(new Material().setKD(0.2).setKS(0.6).setShininess(200))
        );

        // ========== Crystal Sphere on Middle Shelf ==========

        // Base – small pink cylinder to rest the crystal on
        scene.geometries.add(new Cylinder(
                1.4,
                new Ray(new Point(-33.5, -15, -33.5), new Vector(0, 1, 0)),
                0.8)
                .setEmission(new Color(80, 60, 70)) // muted pink base
                .setMaterial(new Material().setKD(0.8).setKS(0.3).setShininess(100)));


        // Crystal sphere – transparent pink ball
        scene.geometries.add(new Sphere(
                2, new Point(-33.5, -12.6, -33.5)) // slightly above base
                .setEmission(new Color(500, 0, 0)) // gentle pink hue
                .setMaterial(new Material().setKD(0.1).setKS(0.8).setShininess(150).setKT(1))); // transparent and glossy

        // ===== Lights =====
        //lamp light (simulated using SpotLight for a focused beam)
        scene.lights.add(new SpotLight(
                new Color(1000, 700, 500),
                new Point(0, 30, 0),
                new Vector(0, -1, 0))
                .setKl(0.000000035).setKq(0.0006));

        // ----------- Sunlight (as SpotLight through the window) -----------
        scene.lights.add(new SpotLight(
                new Color(1500, 1200, 1000),
                new Point(-70, 30, -25),
                new Vector(2.5, -2.5, 0.5))
                .setKl(0.0002).setKq(0.0004)
                .setNarrowBeam(14)
        );

        // Glowing pink light inside the sphere
        scene.lights.add(new PointLight(
                new Color(500, 200, 350), // soft pink glow
                new Point(-33.5, -12.6, -33.5)) // center of sphere
                .setKl(0.006).setKq(0.038));

        // ===== Camera Setup =====
        Camera camera = new Camera.Builder()
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpDistance(300)
                .setVpSize(300, 400)
                .setResolution(1000, 1400)
                .setRayTracer(scene, RayTracerType.SIMPLE)
                .setMultithreading(-1)
                .setBvhMode(Camera.BvhMode.HIERARCHY_AUTO)
                .setLocation(new Point(0, 0, 100))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .build();

        // ===== Rendering =====
        camera.renderImage();
        camera.writeToImage("roomScene");
    }
}


