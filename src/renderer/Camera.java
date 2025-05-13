package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.MissingResourceException;

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
    private static int nX = 1;

    /** The number of pixels in the y-axis */
    private static int nY = 1;

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

    /**
     * Renders the image by casting rays through each pixel
     * and determining the color using the ray tracer.
     *
     * @return the Camera instance for method chaining
     */
    public Camera renderImage() {
        // Loop over each pixel in the view plane
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                // Render this pixel by casting a ray and writing the color
                castRay(j, i);
            }
        }
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
    }

    /**
     * Builder class for constructing Camera instances.
     * This builder follows the Builder design pattern.
     */
    public static class Builder {
        private final Camera camera = new Camera();
        private Point target = null; // The target point the camera is looking at

        /**
         * Sets the direction vectors of the camera.
         *
         * @param vTo the forward direction vector
         * @param vUp the upward direction vector
         * @return the Builder instance
         * @throws IllegalArgumentException if the direction vectors are not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            // Check orthogonality of vTo and vUp
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("Direction vectors must be orthogonal");
            }

            // Normalize and assign vectors
            this.target = null; // target is unused in this case
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp);
            return this;
        }

        /**
         * Sets the target point the camera is looking at.
         *
         * @param target the target point
         * @return the Builder instance
         * @throws IllegalArgumentException if the target equals the camera's position
         */
        public Builder setDirection(Point target) {
            if (target != null && target.equals(camera.p0)) {
                throw new IllegalArgumentException("Camera cannot be at the target point");
            }
            this.target = target;
            camera.vTo = null;
            camera.vUp = null;
            camera.vRight = null;
            return this;
        }

        /**
         * Sets the target point and upward direction vector for the camera.
         *
         * @param target the point the camera is directed at
         * @param vUp    the upward direction vector
         * @return the Builder instance
         * @throws IllegalArgumentException if the target equals the camera's position
         */
        public Builder setDirection(Point target, Vector vUp) {
            if (target != null && target.equals(camera.p0)) {
                throw new IllegalArgumentException("Camera cannot be at the target point");
            }
            this.target = target;
            camera.vUp = vUp.normalize();
            camera.vTo = null;
            camera.vRight = null;
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

                // Return a cloned copy of the fully initialized camera
                return (Camera) camera.clone();
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
        private void validate(Camera camera) throws MissingResourceException {
            // Check view plane dimensions
            if (camera.width == 0 || camera.height == 0) {
                throw new MissingResourceException("View plane size is not set", "Camera", "width or height");
            }

            // Default camera location if not set
            if (camera.p0 == null) {
                camera.p0 = Point.ZERO;
            }

            // Check view plane distance
            if (isZero(camera.distance)) {
                throw new MissingResourceException("Distance to view plane is not set", "Camera", "distance");
            }

            // If target point was given, compute vTo from position to target
            if (target != null) {
                camera.vTo = target.subtract(camera.p0).normalize();

                // If vUp is not set or parallel to vTo, pick a default up direction
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
            if (!isZero(camera.vTo.dotProduct(camera.vUp))) {
                // Orthogonalize vUp using double cross product
                camera.vUp = camera.vTo.crossProduct(camera.vUp).crossProduct(camera.vTo).normalize();
            }

            // Calculate vRight from vTo and vUp
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Compute view plane center point
            camera.pcenter = camera.p0.add(camera.vTo.scale(camera.distance));

            // Clear target after use
            target = null;

            // Resolution check
            if (camera.nX <= 0 || camera.nY <= 0) {
                throw new IllegalStateException("Resolution nX and nY must be positive integers");
            }

            // Initialize image writer
            camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

            // Use default ray tracer if not set
            if (camera.rayTracerBase == null) {
                camera.rayTracerBase = new SimpleRayTracer(new Scene(null));
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
