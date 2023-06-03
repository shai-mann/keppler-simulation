package utils.maths.geometry;

public class Point {

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /*  MOVEMENT METHODS */

    // attempt to translate points within certain boundary
    public boolean translate(double xMovement, double yMovement, Bound boundary) {
        return this.translate(new Point(xMovement, yMovement), boundary);
    }

    public boolean translate(Point movement, Bound boundary) {
        this.translate(movement);

        if (!boundary.inBounds(this.x, this.y)) {
            this.translate(movement.scale(-1.0)); // undo translation
            return false; // movement failed - bounds exited
        }

        return true; // movement succeeded
    }

    // translate the point by values of given point
    public void translate(Point p) {
        this.translate(p.x, p.y);
    }

    //
    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /* MATH METHODS */

    // scales the point by a value
    public Point scale(double scale) {
        return new Point(this.x * scale, this.y * scale);
    }

    /* GETTER METHODS  */

    // get x value as an int
    public int getX() {
        return (int) this.x;
    }

    // get y value as an int
    public int getY() {
        return (int) this.y;
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }

}
