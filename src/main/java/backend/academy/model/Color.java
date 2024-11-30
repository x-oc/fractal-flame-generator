package backend.academy.model;

import java.util.Random;

public record Color(int r, int g, int b) {

    public static final int MAX_BRIGHTNESS = 255;

    public static Color blend(Color color, Color other) {
        return new Color((other.r + color.r) / 2,
                         (other.g + color.g) / 2,
                         (other.b + color.b) / 2);
    }

    public static Color random(Random random) {
        return new Color(random.nextInt(MAX_BRIGHTNESS + 1),
                         random.nextInt(MAX_BRIGHTNESS + 1),
                         random.nextInt(MAX_BRIGHTNESS + 1));
    }
}
