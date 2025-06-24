package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.stream.IntStream;


import static primitives.Util.alignZero;
import static primitives.Util.isZero;
/**
 * The Camera class represents a virtual camera in a 3D scene.
 * It defines the camera's position, orientation, and view plane properties.
 * The class also provides a builder for constructing a camera with specific parameters.
 *
 * @author Hila Rosental & Hila Miller
 */
public class Camera implements Cloneable {

    /** Amount of threads to use fore rendering image by the camera */
    private int threadsCount = 0;
    /**
     * Amount of threads to spare for Java VM threads:<br>
     * Spare threads if trying to use all the cores
     */
    private static final int SPARE_THREADS = 2;
    /**
     * Debug print interval in seconds (for progress percentage)<br>
     * if it is zero - there is no progress output
     */
    private double printInterval = 0;
    /**
     * Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * </ul>
     */
    private PixelManager pixelManager;

    /** The forward direction vector of the camera */
    private Vector vTo = null;

    /** The upward direction vector of the camera */
    private Vector vUp = null;

    /** The rightward direction vector of the camera */
    private Vector vRight = null;

    /** The position of the camera */
    private Point p0 = null;

    /** The center point of the view plane */
    private Point pcenter = null;

    /** The distance from the camera to the view plane */
    private double distance = 0.0;

    /** The width of the view plane */
    private double width = 0.0;

    /** The height of the view plane */
    private double height = 0.0;

    /** The ray tracer used for rendering the scene */
    private RayTracerBase rayTracerBase;

    /** The image writer used for rendering the image */
    private ImageWriter imageWriter;

    /** The number of pixels in the x-axis */
    private int nX = 1;

    /** The number of pixels in the y-axis */
    private int nY = 1;

    /**
     * Enum representing the different modes of BVH (Bounding Volume Hierarchy) acceleration.
     */
    public enum BvhMode {
        /**
         * No BVH acceleration, brute-force intersection check.
         */
        OFF,
        /**
         * Flat Conservative Boundary Region (AABB, no hierarchy).
         * This is a simple bounding box representation without any hierarchy.
         */
        CBR,
        /**
         * Manually organized BVH hierarchy.
         * This allows for a custom hierarchy of bounding volumes for better performance.
         */
        HIERARCHY_MANUAL,
        /**
         * Automatically constructed BVH tree.
         * This enables the camera to use a binary BVH tree for efficient ray-geometry intersections.
         */
        HIERARCHY_AUTO
    }





    /**
     * Private constructor to enforce the use of the Builder class.
     * This ensures the camera is created only via the builder pattern.
     */
    private Camera() {}

    /**
     * Returns a builder for constructing a Camera object.
     *
     * @return a Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray through a specific pixel in the view plane.
     *
     * @param nX The number of pixels in the x-axis
     * @param nY The number of pixels in the y-axis
     * @param j  The column index of the pixel
     * @param i  The row index of the pixel
     * @return A ray from the camera through the specified pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the size of each pixel in the view plane
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        // Calculate the center offset of the pixel in the view plane
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = (i - (nY - 1) / 2.0) * pixelHeight;

        // Start from the center point of the view plane
        Point pIJ = pcenter;

        // Move the point along the right vector if xJ is not zero
        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }

        // Move the point along the up vector if yI is not zero (negative direction because image Y axis is downward)
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(-yI));
        }

        // Return a ray from the camera position to the calculated point on the view plane
        return new Ray(p0, pIJ.subtract(p0));
    }

    /**
     * @return the forward direction vector of the camera
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * @return the upward direction vector of the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * @return the rightward direction vector of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * @return the camera's position
     */
    public Point getP0() {
        return p0;
    }

    /**
     * @return the center point of the view plane
     */
    public Point getPcenter() {
        return pcenter;
    }

    /**
     * @return the distance from the camera to the view plane
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the height of the view plane
     */
    public double getHeight() {
        return height;
    }


    /**
     * Draws a grid on the image by coloring pixels at specified intervals.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return the Camera instance for method chaining
     */
    public Camera printGrid(int interval, Color color) {
        // Loop through all rows and columns of the view plane
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // If the pixel is on a horizontal or vertical grid line
                if (i % interval == 0 || j % interval == 0) {
                    // Color the pixel with the grid color
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file using the image writer.
     *
     * @param fileName the name of the output file (without extension)
     * @return the Camera instance for method chaining
     */
    public Camera writeToImage(String fileName) {
        // Delegate the writing operation to the image writer
        imageWriter.writeToImage(fileName);
        return this;
    }

    /** This function renders image's pixel color map from the scene
     * included in the ray tracer object
     * @return the camera object itself
     */
    public Camera renderImage() {

        pixelManager = new PixelManager(nY, nX, printInterval);
        return switch (threadsCount) {
            case 0 -> renderImageNoThreads();
            case -1 -> renderImageStream();
            default -> renderImageRawThreads();
        };
    }


    /**
     * Render image using multi-threading by parallel streaming
     * @return the camera object itself
     */
    private Camera renderImageStream() {
        IntStream.range(0, nY).parallel()
                .forEach(i -> IntStream.range(0, nX).parallel()
                        .forEach(j -> castRay(j, i)));
        return this;
    }
    /**
     * Render image without multi-threading
     * @return the camera object itself
     */
    private Camera renderImageNoThreads() {
        for (int i = 0; i < nY; ++i)
            for (int j = 0; j < nX; ++j)
                castRay(j, i);
        return this;
    }
    /**
     * Render image using multi-threading by creating and running raw threads
     * @return the camera object itself
     */
    private Camera renderImageRawThreads() {
        var threads = new LinkedList<Thread>();
        while (threadsCount-- > 0)
            threads.add(new Thread(() -> {
                PixelManager.Pixel pixel;
                while ((pixel = pixelManager.nextPixel()) != null)
                    castRay(pixel.col(), pixel.row());
            }));
        for (var thread : threads) thread.start();
        try {
            for (var thread : threads) thread.join();
        } catch (InterruptedException ignored) {}
        return this;
    }

    /**
     * Casts a ray through the specified pixel and sets the pixel color
     * according to the ray tracer's result.
     *
     * @param ix the x-coordinate (column) of the pixel
     * @param iy the y-coordinate (row) of the pixel
     */
    private void castRay(int ix, int iy) {
        // Construct a ray through the pixel
        Ray ray = constructRay(nX, nY, ix, iy);

        // Trace the ray to get the color at the intersection point
        Color color = rayTracerBase.traceRay(ray);

        // Color the pixel using the traced color
        imageWriter.writePixel(ix, iy, color);

        // Update pixel manager with the current pixel
        pixelManager.pixelDone();

    }

    /**
     * Builder class for constructing Camera instances.
     * This builder follows the Builder design pattern.
     */
    public static class Builder {
        /**
         * The camera object being built.
         */
        private final Camera camera = new Camera();

        /**
         * The point where the camera is looking at.
         */
        private Point target = null;

        /**
         * The error message for zero values.
         */
        private final String zeroErr = " cannot be zero";

        /**
         * The BVH mode for the scene geometries.
         * Default is OFF, meaning no BVH acceleration is used.
         */
        private BvhMode bvhMode = BvhMode.OFF;


        public Builder setBvhMode(BvhMode mode) {
            this.bvhMode = mode;
            return this;
        }

        /**
         * Set multi-threading <br>
         * Parameter value meaning:
         * <ul>
         * <li>-2 - number of threads is number of logical processors less 2</li>
         * <li>-1 - stream processing parallelization (implicit multi-threading) is used</li>
         * <li>0 - multi-threading is not activated</li>
         * <li>1 and more - literally number of threads</li>
         * </ul>
         * @param threads number of threads
         * @return builder object itself
         */
        public Builder setMultithreading(int threads) {
            if (threads < -3)
                throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
            if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            } else
                camera.threadsCount = threads;
            return this;
        }
        /**
         * Set debug printing interval. If it's zero - there won't be printing at all
         * @param interval printing interval in %
         * @return builder object itself
         */
        public Builder setDebugPrint(double interval) {
            if (interval < 0) throw new IllegalArgumentException("interval parameter must be non-negative");
            camera.printInterval = interval;
            return this;
        }

        /**
         * Sets the direction vectors of the camera.
         *
         * @param vTo the forward direction vector
         * @param vUp the upward direction vector
         * @return the Builder instance
         * @throws IllegalArgumentException if vectors are null, zero, or not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("vTo and vUp must not be null");
            }
            if (isZero(vTo.lengthSquared()) || isZero(vUp.lengthSquared())) {
                throw new IllegalArgumentException("vTo" + zeroErr + " and vUp" + zeroErr);
            }
            if (!isOrthogonal(vTo, vUp)) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }


            this.target = null;
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            return this;
        }

        /**
         * Sets the direction of the camera using a target point.
         * Assumes Y-axis as default upward direction if possible.
         *
         * @param target the target point
         * @return the Builder instance
         * @throws IllegalArgumentException if target is null, equals to camera position, or aligned with Y-axis
         */
        public Builder setDirection(Point target) {
            if (target == null) {
                throw new IllegalArgumentException("Target point must not be null");
            }
            if (camera.p0 == null) {
                throw new IllegalStateException("Camera location (p0) must be set before setting direction by target");
            }

            if (target.equals(Point.ZERO)) {
                throw new IllegalArgumentException("target" + zeroErr);
            }

            if (target.equals(camera.p0)) {
                throw new IllegalArgumentException("Camera cannot be located at the target point");
            }

            // Check if the target is aligned with the Y-axis
            if (isParallel(target.subtract(camera.p0).normalize(), Vector.AXIS_Y)) {
                throw new IllegalArgumentException("Cannot set direction aligned with Y-axis without specifying an up vector");
            }

            this.target = target;
            //vUp, vRight, and vTo will be set in the validate method
            return this;
        }

        /**
         * Sets the direction using a target point and upward vector.
         *
         * @param target the target point
         * @param vUp the upward direction vector
         * @return the Builder instance
         * @throws IllegalArgumentException if inputs are invalid or vectors are parallel
         */
        public Builder setDirection(Point target, Vector vUp) {
            if (target == null || vUp == null) {
                throw new IllegalArgumentException("Target and vUp must not be null");
            }
            if (camera.p0 == null) {
                throw new IllegalStateException("Camera location (p0) must be set before setting direction");
            }
            if (target.equals(camera.p0)) {
                throw new IllegalArgumentException("Camera cannot be located at the target point");
            }
            if (isZero(vUp.lengthSquared())) {
                throw new IllegalArgumentException("vUp must be a non-zero vector");
            }

            if (isParallel(target.subtract(camera.p0).normalize(), vUp)) {
                throw new IllegalArgumentException("vTo and vUp cannot be parallel");
            }

            this.target = target;
            camera.vUp = vUp.normalize();
            //vTo and vRight will be set in the validate method
            return this;
        }



        /**
         * Sets the position of the camera.
         *
         * @param p0 the position point
         * @return the Builder instance
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the view plane distance
         * @return the Builder instance
         * @throws IllegalArgumentException if the distance is not positive
         */
        public Builder setVpDistance(double distance) {
            if (alignZero(distance) <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the size (width and height) of the view plane.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the Builder instance
         * @throws IllegalArgumentException if either dimension is not positive
         */
        public Builder setVpSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }


        /**
         * Builds and returns a Camera object after validating all fields.
         *
         * @return the constructed Camera instance (cloned), or null if cloning fails
         */
        public Camera build() {
            try {
                // Validate the configuration before building
                validate(camera);

                // Clone the camera instance
                Camera cam = (Camera) camera.clone();

                // Apply BVH mode to the geometries (if ray tracer and scene are set)
                if (cam.rayTracerBase instanceof SimpleRayTracer simple) {
                    Scene scene = simple.getScene();
                    if (scene != null && scene.geometries != null) {

                        switch (bvhMode) {
                            case OFF -> scene.geometries.turnOnOffBvh(false);
                            case CBR -> {
                                scene.geometries.turnOnOffBvh(true);
                                scene.geometries.flatten();
                            }
                            case HIERARCHY_MANUAL -> scene.geometries.turnOnOffBvh(true);
                            case HIERARCHY_AUTO -> {
                                scene.geometries.turnOnOffBvh(true);
                                scene.geometries.buildBinaryBvhTree();
                            }
                        }
                    }
                }

                return cam;
            } catch (CloneNotSupportedException ignored) {
                return null;
            }
        }


        /**
         * Validates the camera parameters before building.
         *
         * @param camera the Camera object to validate
         * @throws MissingResourceException if required fields are missing
         * @throws IllegalStateException    if resolution is invalid
         */
        private void validate(Camera camera) {
            // Constants for error messages
            String className = "Camera";
            String msg = "Missing render data: ";

            // Check view plane dimensions
            if (isZero(camera.width) || isZero(camera.height)) {
                throw new MissingResourceException(
                        msg + "View plane size is not set",
                        className,
                        "width or height" + zeroErr);
            }

            // Default camera location if not set
            if (camera.p0 == null) {
                camera.p0 = Point.ZERO;
            }

            // Check view plane distance
            if (isZero(camera.distance)) {
                throw new MissingResourceException(
                        msg + "Distance to view plane is not set",
                        className,
                        "distance" + zeroErr);
            }

            // If target point was given, compute vTo from position to target
            if (target != null) {
                camera.vTo = target.subtract(camera.p0).normalize();
                if (camera.vUp == null || isParallel(camera.vTo, camera.vUp)) {
                    camera.vUp = isParallel(camera.vTo, Vector.AXIS_Y) ? Vector.AXIS_X : Vector.AXIS_Y;
                }
            }

            // Set default vTo if still null
            if (camera.vTo == null) {
                camera.vTo = Vector.AXIS_Z;
            }

            // Set default vUp if still null
            if (camera.vUp == null) {
                camera.vUp = Vector.AXIS_Y;
            }

            // Ensure vUp and vTo are orthogonal
            if (!isOrthogonal(camera.vTo, camera.vUp)) {
                camera.vUp = camera.vTo.crossProduct(camera.vUp).crossProduct(camera.vTo).normalize();
            }

            // Resolution check
            if (camera.nX <= 0) {
                throw new MissingResourceException(
                        msg + "nX is not set",
                        className,
                        "Number of pixels in x direction must be positive");
            }

            if (camera.nY <= 0) {
                throw new MissingResourceException(
                        msg + "nY is not set",
                        className,
                        "Number of pixels in y direction must be positive");
            }

            // Calculate vRight from vTo and vUp
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Compute view plane center point
            camera.pcenter = camera.p0.add(camera.vTo.scale(camera.distance));

            // Clear target after use
            target = null;

            // Use default ray tracer if not set
            if (camera.rayTracerBase == null) {
                camera.rayTracerBase = new SimpleRayTracer(new Scene(null));
            }

            // Initialize image writer
            if (camera.imageWriter == null) {
                camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
            }
        }

        /**
         * Checks if two vectors are parallel.
         *
         * @param v1 first vector
         * @param v2 second vector
         * @return true if vectors are parallel, false otherwise
         */
        private boolean isParallel(Vector v1, Vector v2) {
            // Normalize both vectors
            Vector n1 = v1.normalize();
            Vector n2 = v2.normalize();

            // Check if dot product is close to 1
            double dot = Math.abs(n1.dotProduct(n2));
            return isZero(dot - 1);
        }

        /**
         * Checks if two vectors are orthogonal.
         */
        private boolean isOrthogonal(Vector v1, Vector v2) {
            return isZero(v1.dotProduct(v2));
        }

        /**
         * Sets the resolution of the image (number of pixels in x and y).
         *
         * @param nx number of horizontal pixels
         * @param ny number of vertical pixels
         * @return the Builder instance
         */
        public Builder setResolution(int nx, int ny) {
            // Save values to camera
            camera.imageWriter = new ImageWriter(nx, ny);
            camera.nX = nx;
            camera.nY = ny;
            return this;
        }

        /**
         * Sets the ray tracer for the camera.
         *
         * @param scene          the scene to trace rays in
         * @param rayTracerType  the type of ray tracer to use
         * @return the Builder instance
         */
        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {

            // Currently only SIMPLE type is supported
            switch (rayTracerType) {
                case SIMPLE:
                    camera.rayTracerBase = new SimpleRayTracer(scene);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid ray tracer type");
            }
            return this;
        }
    }
}
