package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.random;
import static primitives.Util.isZero;

/**
 * The Board class represents a board in a 3D space, allowing for the generation of points
 * in either a square or circular pattern based on specified parameters.
 *
 * @author Hila Rosental and Hila Miller
 */
public class Board {
    /**
     * The center point of the board.
     */
    private Point center;

    /**
     * The up vector defining the orientation of the board.
     */
    private Vector VUp;

    /**
     * The right vector defining the orientation of the board.
     */
    private Vector VRight;

    /**
     * The size of the board.
     */
    private double size;

    /**
     * Indicates whether the board should be treated as a circle.
     */
    private boolean circle = false;

    /**
     * Constructs a Board with the specified center point, up vector, right vector, and size.
     *
     * @param center the center point of the board
     * @param VUp    the up vector defining the orientation of the board
     * @param VRight the right vector defining the orientation of the board
     * @param size   the size of the board
     */
    public Board(Point center, Vector VUp, Vector VRight, double size) {
        this.center = center;
        this.VUp = VUp;
        this.VRight = VRight;
        this.size = size;
    }

    /**
     * Sets whether the board should be treated as a circle and returns the updated Board object.
     *
     * @param circle true if the board should be treated as a circle, false otherwise
     * @return the updated Board object with the circle property set
     */
    public Board setCircle(boolean circle) {
        this.circle = circle;
        return this;
    }


    /**
     * Generates a list of points evenly distributed in a square pattern on the board,
     * with a small random offset inside each sub-square for soft shadow sampling.
     *
     * @param numberOfSamplesInRow the number of samples per row (and column)
     * @return a list of points in a square pattern centered around the light source
     */
    private List<Point> getPointsSquare(int numberOfSamplesInRow) {
        // Size of each sub-square within the main square area
        double subPixelSize = size / numberOfSamplesInRow;

        // List to hold the generated sample points
        List<Point> points = new LinkedList<>();

        Point point;
        double x, y;

        // Loop through the rows and columns of the square grid
        for (int i = 0; i < numberOfSamplesInRow; i++) {
            for (int j = 0; j < numberOfSamplesInRow; j++) {
                // Compute y-offset from the center (in world units), flipped vertically
                // and add random jitter within the sub-square to break symmetry
                y = (-(i - (numberOfSamplesInRow - 1.0) / 2.0) * subPixelSize)
                        + (random() - 0.5) * subPixelSize;

                // Compute x-offset from the center (in world units) with random jitter
                x = ((j - (numberOfSamplesInRow - 1.0) / 2.0) * subPixelSize)
                        + (random() - 0.5) * subPixelSize;

                // Start from the center of the light source
                point = center;

                // Add horizontal offset along VRight direction
                if (!isZero(x)) {
                    point = point.add(VRight.scale(x));
                }

                // Add vertical offset along VUp direction
                if (!isZero(y)) {
                    point = point.add(VUp.scale(y));
                }

                // Add the final computed point to the list
                points.add(point);
            }
        }

        // Return the complete list of sampled points
        return points;
    }


    /**
     * Filters a square-distributed set of points and returns only those
     * that lie within a circular area centered at the light source.
     *
     * @param numberOfSamplesPerRow the number of samples per row (and column)
     * @return a list of points within a circular pattern
     */
    private List<Point> getPointsCircle(int numberOfSamplesPerRow) {
        // Generate a square grid of points with random jitter
        List<Point> pointsSquare = getPointsSquare(numberOfSamplesPerRow);

        // Prepare a list to hold only the points that fall inside the circle
        List<Point> pointsCircled = new LinkedList<>();

        // Calculate the squared radius of the circle
        // Since the circle is inscribed in a square of size 'size',
        // its radius is size / 2 → and radius^2 = size^2 / 4
        double radiusSquared = size * size / 4d;

        // Iterate through all points in the square
        for (Point point : pointsSquare)
            // Check if the point is inside the circle (by comparing squared distances)
            if (point.distanceSquared(center) < radiusSquared)
                // Add the point to the circular list if it falls inside the radius
                pointsCircled.add(point);

        // Return only the points within the circular area
        return pointsCircled;
    }


    /**
     * Generates a list of points based on the number of samples per row.
     * The pattern can be either square or circular depending on the circle property.
     *
     * @param numberOfSamplesPerRow the number of samples per row
     * @return a list of points in either a square or circular pattern
     */
    public List<Point> getPoints(int numberOfSamplesPerRow) {
        if (circle)
            return getPointsCircle(numberOfSamplesPerRow);
        else
            return getPointsSquare(numberOfSamplesPerRow);
    }

}
