package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {
    @Test
    void createYellowSubmarineTest(){
        Color Red = new Color(java.awt.Color.RED);
        Color Yellow = new Color(java.awt.Color.YELLOW);
        int nx = 800;
        int ny = 500;
        int gap= 50;
        ImageWriter imageWriter= new ImageWriter(nx, ny);

        for(int i=0; i<nx; i++){
            for(int j=0; j<ny; j++){
                if(i%gap==0 || j%gap==0){
                    imageWriter.writePixel(i, j, Red);
                }
                else{
                    imageWriter.writePixel(i, j, Yellow);
                }
            }
        }
        imageWriter.writeToImage("yellowSubmarine");
    }

}