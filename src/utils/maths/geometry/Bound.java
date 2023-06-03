package utils.maths.geometry;

public class Bound {

    Range xRange;
    Range yRange;

    public Bound(int lowerX, int upperX, int lowerY, int upperY) {
        this.xRange = new Range(lowerX, upperX);
        this.yRange = new Range(lowerY, upperY);
    }

    public Bound(Range xRange, Range yRange) {
        this(xRange.lowerBound, xRange.upperBound, yRange.lowerBound, yRange.upperBound);
    }

    public boolean inBounds(utils.maths.geometry.Point p) {
        return this.inBounds(p.x, p.y);
    }

    public boolean inBounds(double x, double y) {
        return this.xRange.inRange(x) && this.yRange.inRange(y);
    }

}
