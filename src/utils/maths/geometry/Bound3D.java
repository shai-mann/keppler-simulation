package utils.maths.geometry;


public class Bound3D {

    utils.maths.geometry.Bound bounds2d;
    Range boundsZ;

    public Bound3D(Range xRange, Range yRange, Range zRange) {
        this.bounds2d = new utils.maths.geometry.Bound(xRange, yRange);
        this.boundsZ = zRange;
    }

    public boolean inBounds(Point3D p) {
        return inBounds(p.x, p.y, p.z);
    }

    public boolean inBounds(int x, int y, int z) {
        return bounds2d.inBounds(x, y) && boundsZ.inRange(z);
    }
}
