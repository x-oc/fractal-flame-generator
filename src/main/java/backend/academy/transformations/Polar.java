package backend.academy.transformations;

import backend.academy.model.Point;

public class Polar implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        double theta = Math.atan(point.y() / point.x());
        return new Point(theta / Math.PI, r - 1);
    }
}
