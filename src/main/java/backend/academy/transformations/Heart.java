package backend.academy.transformations;

import backend.academy.Point;

public class Heart implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point(r * Math.sin(r * Math.atan(point.y()) / point.x()),
                         -r * Math.cos(r * Math.atan(point.y()) / point.x()));
    }
}
