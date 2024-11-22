package backend.academy.transformations;

import backend.academy.Point;

public class Swirl implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point(point.x() * Math.sin(r * r) - point.y() * Math.cos(r * r),
                         point.x() * Math.cos(r * r) + point.y() * Math.sin(r * r));
    }
}
