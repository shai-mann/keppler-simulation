package utils.maths.geometry;

public class Range {

    int lowerBound;
    int upperBound;

    public Range(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /* RANGE METHODS */

    public boolean inRange(int value) {
        return this.inRange(value * 1.0);
    }

    public boolean inRange(double value) {
        return value < this.upperBound && value > this.lowerBound;
    }

}
