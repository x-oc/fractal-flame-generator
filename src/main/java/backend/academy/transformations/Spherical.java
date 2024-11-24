package backend.academy.transformations;

import backend.academy.model.Point;

public class Spherical implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point(point.x() / (r * r), point.y() / (r * r));
    }
}
