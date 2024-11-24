package backend.academy.model;

import java.util.Random;

public record Point(double x, double y) {
    public static Point random(Rect rect, Random random) {
        double x = rect.x() + random.nextDouble() * rect.width();
        double y = rect.y() + random.nextDouble() * rect.height();
        return new Point(x, y);
    }
}
