package backend.academy.transformations;

import backend.academy.model.Point;

public class Horseshoe implements Transformation {

    @Override
    public Point apply(Point point) {
        double r = Math.sqrt(point.x() * point.x() + point.y() * point.y());
        return new Point((point.x() - point.y()) * (point.x() + point.y()) / r, 2 * point.x() * point.y());
    }
}
