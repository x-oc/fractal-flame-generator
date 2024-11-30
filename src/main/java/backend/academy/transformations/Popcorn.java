package backend.academy.transformations;

import backend.academy.model.Point;

public class Popcorn implements Transformation {

    private static final int COEFFICIENT = 3;

    @Override
    public Point apply(Point point) {
        double x = point.x() + Math.sin(Math.tan(COEFFICIENT * point.y()));
        double y = point.y() + Math.sin(Math.tan(COEFFICIENT * point.x()));
        return new Point(x, y);
    }
}
