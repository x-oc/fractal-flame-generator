package backend.academy.transformations;

import backend.academy.model.Point;

public class Popcorn implements Transformation {

    @Override
    public Point apply(Point point) {
        double x = point.x() + Math.sin(Math.tan(3 * point.y()));
        double y = point.y() + Math.sin(Math.tan(3 * point.x()));
        return new Point(x, y);
    }
}
