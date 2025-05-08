package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents an infinite tube in 3D space.
 * A tube is defined by a central axis (as a ray) and a constant radius.
 */
public class Tube extends RadialGeometry {

    /**
     * The central axis of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with the specified radius and axis.
     *
     * @param radius the radius of the tube
     * @param axis   the central axis of the tube
     */
    public Tube(double radius, Ray axis) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the tube at the specified point on its surface.
     * <p><b>Note:</b> This method is currently not implemented and returns {@code null}.</p>
     *
     * @param p the point on the surface of the tube
     * @return the normal vector at the given point (currently {@code null})
     */
    @Override
    public Vector getNormal(Point p) {
        //projection of p-o on the ray
        double t = p.subtract(axis.getHead()).dotProduct(axis.getDirection());// (p-po)*v(t=u*v)
        return p.subtract(axis.getHead().add(axis.getDirection().scale(t))).normalize();// p-(po+u*v)
    }

    @Override
    public List<Point> findIntersections(Ray ray) {

        Point P0 = ray.getHead();
        Vector v = ray.getDirection();

        Point Pa = axis.getHead();
        Vector Va = axis.getDirection();

        //At^2 + Bt + C equation.
        double A, B, C;

        //(v,u) = v dot product u

        //calculate the vector: V-(V,Va)Va
        Vector VecA = v;
        double Vva = v.dotProduct(Va);

        try {
            if (!isZero(Vva))
                VecA = v.subtract(Va.scale(Vva));

            //A = (V-(V,Va)Va)^2
            A = VecA.lengthSquared();
        }

        //if A = 0 return null (there are no intersections)
        catch (IllegalArgumentException ex) { // the ray is parallel to the axisRay
            return null;
        }

        //if A != 0 continue to calculate B and C
        try {

            //calculate deltaP (delP) vector, P-Pa
            Vector DeltaP = P0.subtract(Pa);

            //The vector: delP - (delP,Va)Va
            Vector DeltaPMinusDeltaPVaVa = DeltaP;
            double DeltaPVa = DeltaP.dotProduct(Va);

            if (!isZero(DeltaPVa))
                DeltaPMinusDeltaPVaVa = DeltaP.subtract(Va.scale(DeltaPVa));

            //B = 2(V - (V,Va)Va , delP - (delP,Va)Va)
            B = 2 * (VecA.dotProduct(DeltaPMinusDeltaPVaVa));

            //C = (delP - (delP,Va)Va)^2 - r^2
            C = DeltaPMinusDeltaPVaVa.lengthSquared() - radius * radius;
        }
        //in case delP = 0, or delP - (delP,Va)Va = (0, 0, 0)
        catch (IllegalArgumentException ex) {
            B = 0;
            C = -1 * radius * radius;
        }

        //solving At^2 + Bt + C = 0

        //the discrimation, B^2 - 4AC
        double Disc = alignZero(B * B - 4 * A * C);

        //no solutions for the equation. disc = 0 means that the ray parallel to the tube
        if (Disc <= 0)
            return null;

        //the solutions for the equation
        double t1, t2;

        t1 = alignZero(-B + Math.sqrt(Disc)) / (2 * A);
        t2 = alignZero(-B - Math.sqrt(Disc)) / (2 * A);

        //taking all positive solutions
        List<Point> intersections = new LinkedList<>();
        if (t1 > 0) intersections.add(ray.getPoint(t1));
        if (t2 > 0 && !isZero(t2 - t1)) intersections.add(ray.getPoint(t2));

        return intersections.isEmpty() ? null : intersections;

    }

}
