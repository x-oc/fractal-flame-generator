package backend.academy.transformations;

import backend.academy.model.Point;

public class Sinusoidal implements Transformation {

    @Override
    public Point apply(Point point) {
        return new Point(Math.sin(point.x()), Math.sin(point.y()));
    }
}
