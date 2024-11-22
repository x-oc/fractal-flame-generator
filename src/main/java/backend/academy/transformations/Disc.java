package backend.academy.transformations;

import backend.academy.Point;

public class Disc implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point(Math.atan(point.y() / point.x()) * Math.sin(Math.PI * r) / Math.PI,
                         Math.atan(point.y() / point.x()) * Math.cos(Math.PI * r) / Math.PI);
    }
}
