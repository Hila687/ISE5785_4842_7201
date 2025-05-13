package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link renderer.ImageWriter} class.
 * This test creates an image with a yellow background and a red grid every 50 pixels.
 */
class ImageWriterTest {

    /**
     * Generates an image with a yellow background and red grid lines.
     * The image has dimensions 800x500 pixels, and grid lines appear every 50 pixels.
     * The image is saved with the name "yellowSubmarine.png".
     */
    @Test
    void createYellowSubmarineTest() {
        Color Red = new Color(java.awt.Color.RED);
        Color Yellow = new Color(java.awt.Color.YELLOW);

        int nx = 800;
        int ny = 500;
        int gap = 50;

        // Create a new ImageWriter with the specified resolution
        ImageWriter imageWriter = new ImageWriter(nx, ny);

        // Loop through every pixel in the image
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                // Color the pixel red if it's on a grid line
                if (i % gap == 0 || j % gap == 0) {
                    imageWriter.writePixel(i, j, Red);
                }
                // Otherwise, color it yellow
                else {
                    imageWriter.writePixel(i, j, Yellow);
                }
            }
        }

        // Write the resulting image to file
        imageWriter.writeToImage("yellowSubmarine");
    }
}
