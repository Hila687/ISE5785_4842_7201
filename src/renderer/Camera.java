package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/* The Camera class represents a virtual camera in a 3D scene.
 * It defines the camera's position, orientation, and view plane properties.
 * The class also provides a builder for constructing a camera with specific parameters.*/
public class Camera implements Cloneable {
    private static  int NX  ;
    private static  int NY  ;
    private Vector vTo = null; // The forward direction vector of the camera
    private Vector vUp = null; // The upward direction vector of the camera
    private Vector vRight = null; // The rightward direction vector of the camera
    private Point p0 = null; // The position of the camera
    private Point pcenter = null; // The center point of the view plane
    private double distance = 0.0; // The distance from the camera to the view plane
    private double width = 0.0; // The width of the view plane
    private double height = 0.0; // The height of the view plane
    private RayTracerBase rayTracerBase;
    private ImageWriter imageWriter;

    private Camera() {
    }

    /* Returns a builder for constructing a Camera object.
     * @return A Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /* Constructs a ray through a specific pixel in the view plane.
     * @param nX The number of pixels in the x-axis.
     * @param nY The number of pixels in the y-axis.
     * @param j  The column index of the pixel.
     * @param i  The row index of the pixel.
     * @return A ray from the camera through the specified pixel.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the size of each pixel in the view plane
        double pixelWidth = width / nX;
        double pixelHeight = height / nY;

        // Calculate the center of the pixel
        double xJ = (j - (nX - 1) / 2.0) * pixelWidth;
        double yI = (i - (nY - 1) / 2.0) * pixelHeight;

        // Calculate the point on the view plane
        Point pIJ = pcenter;
        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(-yI));
        }
        // Return a ray from the camera position to the point on the view plane
        return new Ray(p0, pIJ.subtract(p0));

    }

    // Getters for camera properties
    public Vector getVTo() {
        return vTo;
    }

    public Vector getVUp() {
        return vUp;
    }

    public Vector getVRight() {
        return vRight;
    }

    public Point getP0() {
        return p0;
    }

    public Point getPcenter() {
        return pcenter;
    }

    public double getDistance() {
        return distance;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    /* The Builder class is used to construct a Camera object with specific parameters.*/
    public static class Builder {
        private final Camera camera = new Camera();
        private Point target = null; // The target point the camera is looking at

        /* Sets the direction vectors of the camera.
         * @param vTo The forward direction vector.
         * @param vUp The upward direction vector.
         * @return The Builder instance.
         * @throws IllegalArgumentException if the direction vectors are not orthogonal.
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            // Ensure the direction vectors are orthogonal
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("Direction vectors must be orthogonal");
            }
            this.target = null; // Will be calculated in validate
            // Normalize and set the direction vectors
            camera.vUp = vUp.normalize();
            camera.vTo = vTo.normalize();
            camera.vRight = camera.vTo.crossProduct(camera.vUp);
            return this;
        }

        /* Sets the target point the camera is looking at.
         * @param target The target point.
         * @return The Builder instance.
         * @throws IllegalArgumentException if the target point is the same as the camera position.
         */
        public Builder setDirection(Point target) {
            // Ensure the target point is not the same as the camera position
            if (target != null && target.equals(camera.p0)) {
                throw new IllegalArgumentException("Camera cannot be at the target point");
            }
            this.target = target;
            camera.vTo = null; // Will be calculated in validate
            camera.vUp = null; // Will be calculated in validate
            camera.vRight = null; // Will be calculated in validate
            return this;
        }

        /* Sets the target point and the upward direction vector.
         * @param target The target point.
         * @param vUp    The upward direction vector.
         * @return The Builder instance.
         * @throws IllegalArgumentException if the target point is the same as the camera position.
         */
        public Builder setDirection(Point target, Vector vUp) {
            // Ensure the target point is not the same as the camera position
            if (target != null && target.equals(camera.p0)) {
                throw new IllegalArgumentException("Camera cannot be at the target point");
            }
            this.target = target;
            camera.vUp = vUp.normalize();
            camera.vTo = null; // Will be calculated in validate
            camera.vRight = null; // Will be calculated in validate
            return this;
        }

        /* Sets the location of the camera.
         * @param p0 The position of the camera.
         * @return The Builder instance.
         */
        public Builder setLocation(Point p0) {
            // Set the camera's position
            camera.p0 = p0;
            return this;
        }

        /* Sets the distance from the camera to the view plane.
         * @param distance The distance to the view plane.
         * @return The Builder instance.
         * @throws IllegalArgumentException if the distance is not positive.
         */
        public Builder setVpDistance(double distance) {
            // Ensure the distance is positive
            if (alignZero(distance) <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            // Set the distance
            camera.distance = distance;
            return this;
        }

        /* Sets the size of the view plane.
         * @param width  The width of the view plane.
         * @param height The height of the view plane.
         * @return The Builder instance.
         * @throws IllegalArgumentException if the width or height is not positive.
         */
        public Builder setVpSize(double width, double height) {
            // Ensure the width and height are positive
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            // Set the view plane size
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Builds and returns the constructed Camera object.
         * This method ensures immutability by cloning the Camera instance.
         *
         * @return The constructed Camera object, or null if cloning is not supported.
         */
        public Camera build() {
            try {
                validate(camera);
                // Clone the camera to ensure immutability
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException ignored) {
                return null;
            }
        }

        /* Validates the camera parameters.
         * @param camera The Camera object to validate.
         * @throws MissingResourceException if the view plane size is not set.
         * @throws IllegalStateException    if the distance to the view plane is not set.
         */
        private void validate(Camera camera) throws MissingResourceException {
            // Ensure the view plane size is set
            if (camera.width == 0 || camera.height == 0) {
                throw new MissingResourceException(
                        "View plane size is not set",
                        "Camera",
                        "width or height");
            }
            // Ensure the camera position is set
            if (camera.p0 == null) {
                camera.p0 = Point.ZERO;
            }

            // Ensure the distance to the view plane is set
            if (isZero(camera.distance)) {
                throw new MissingResourceException(
                        "Distance to view plane is not set",
                        "Camera",
                        "distance");
            }

            // If the target point is set, calculate the direction vectors
            if (target != null) {
                camera.vTo = target.subtract(camera.p0).normalize();
                if (camera.vUp == null || isParallel(camera.vTo, camera.vUp)) {
                    camera.vUp = isParallel(camera.vTo, Vector.AXIS_Y) ? Vector.AXIS_X : Vector.AXIS_Y;
                }
            }

            // If the direction vectors are not set, use default values
            if (camera.vTo == null) {
                camera.vTo = Vector.AXIS_Z;
            }

            // If the up vector is not set, use a default value
            if (camera.vUp == null) {
                camera.vUp = Vector.AXIS_Y;
            }

            // Ensure the up vector is orthogonal to the forward direction vector
            if (!isZero(camera.vTo.dotProduct(camera.vUp))) {
                camera.vUp = camera.vTo.crossProduct(camera.vUp).crossProduct(camera.vTo).normalize();
            }

            // Calculate the right direction vector
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Calculate the center point of the view plane
            camera.pcenter = camera.p0.add(camera.vTo.scale(camera.distance));
            target = null;
        }

        /* Checks if two vectors are parallel.
         * @param v1 The first vector.
         * @param v2 The second vector.
         * @return true if the vectors are parallel, false otherwise.
         */
        private boolean isParallel(Vector v1, Vector v2) {
            Vector n1 = v1.normalize();
            Vector n2 = v2.normalize();
            double dot = Math.abs(n1.dotProduct(n2));
            return isZero(dot - 1);
        }


        public Builder setResolution(int nx, int ny) {
            //todo
            camera.imageWriter = new ImageWriter(nx, ny);
            camera.NX = nx;
            camera.NY = ny;
            return this;
        }

        public Camera renderImage() {
            //todo
            return this;
        }

        public Builder setRayTracer(Scene scene, RayTracerType rayTracerType) {
            switch (rayTracerType){
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