package backend.academy.transformations;

import backend.academy.model.Color;
import backend.academy.model.Point;
import java.security.SecureRandom;
import java.util.Random;

public record AffineTransformation(double a, double b, double c,
                                   double d, double e, double f,
                                   Color color)
    implements Transformation {

    public static AffineTransformation colored(Random random, Color color) {

        double a = random.nextDouble(-1, 1);
        double b = random.nextDouble(-1, 1);
        double c = random.nextDouble(-1, 1);
        double d = random.nextDouble(-1, 1);
        double e = random.nextDouble(-1, 1);
        double f = random.nextDouble(-1, 1);

        while (!isCompressive(a, b, d, e)) {
            a = random.nextDouble(-1, 1);
            b = random.nextDouble(-1, 1);
            d = random.nextDouble(-1, 1);
            e = random.nextDouble(-1, 1);
        }

        return new AffineTransformation(a, b, c, d, e, f, color);
    }

    public static AffineTransformation colored(Color color) {
        return colored(new SecureRandom(), color);
    }

    public static AffineTransformation random(Random random) {

        return colored(random, Color.random(random));
    }

    public static AffineTransformation random() {
        return random(new SecureRandom());
    }

    private static boolean isCompressive(double a, double b, double d, double e) {
        return a * a + d * d < 1
            && b * b + e * e < 1
            && a * a + b * b + d * d + e * e < 1 + (a * e - b * d) * (a * e - b * d);
    }

    @Override
    public Point apply(Point point) {
        return new Point(a * point.x() + b * point.y() + c,
                         d * point.x() + e * point.y() + f);
    }
}
