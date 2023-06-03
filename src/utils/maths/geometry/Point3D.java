package utils.maths.geometry;

public class Point3D {

    int x, y, z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /*  MOVEMENT METHODS */

    // attempt to translate points within certain boundary
    public boolean translate(int xMovement, int yMovement, int zMovement, utils.maths.geometry.Bound3D boundary) {
        return this.translate(new Point3D(xMovement, yMovement, zMovement), boundary);
    }

    public boolean translate(Point3D movement, utils.maths.geometry.Bound3D boundary) {
        this.translate(movement);

        if (!boundary.inBounds(this.x, this.y, this.z)) {
            this.translate(movement.scale(-1)); // undo translation
            return false; // movement failed - bounds exited
        }

        return true; // movement succeeded
    }

    // translate the point by values of given point
    public void translate(Point3D p) {
        this.translate(p.x, p.y, p.z);
    }

    //
    public void translate(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /* MATH METHODS */

    // scales the point by a value
    public Point3D scale(int scale) {
        return new Point3D(this.x * scale, this.y * scale, this.z * scale);
    }

    /* GETTER METHODS  */

    // get x value as a double
    public double getX() {
        return this.x;
    }

    // get y value as a double
    public double getY() {
        return this.y;
    }

    // get z value as a double
    public double getZ() {
        return this.z;
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }

}
