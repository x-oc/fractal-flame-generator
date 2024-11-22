package backend.academy.transformations;

import backend.academy.Point;

public final class Linear implements Transformation {

    @Override
    public Point apply(Point point) {
        return new Point(point.x(), point.y());
    }
}
