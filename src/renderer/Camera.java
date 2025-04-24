package renderer;

import primitives.*;

public class Camera implements Cloneable{

    private Point p0 = Point.ZERO;
    private Vector vTo = new Vector(0,0,-1);
    private  Vector vup = new Vector(0,1,0);
    private  Vector vRight = new Vector(1,0,0);
    private  double width = 0.0;
    private  double height = 0.0;
    private  double distance = 0.0;


    public Point getP0() {
        return p0;
    }

    public Vector getvTo() {
        return vTo;
    }

    public double getDistance() {
        return distance;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public Vector getvRight() {
        return vRight;
    }

    public Vector getVup() {
        return vup;
    }




    private Camera(){
    }

    public  Builder getBuilder(){
        return new Builder();
    }


    public static class Builder{
        private Camera camera= new Camera();
        public Builder setResolution(int nx, int ny) {
            return this;
        }

        public Camera build(){
            try {
                return (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

    }
}
