package backend.academy.imageProcessors;

import backend.academy.model.Color;
import backend.academy.model.FractalImage;
import backend.academy.model.Pixel;

public class GammaCorrection implements ImageProcessor {

    public static final double GAMMA = 2.2;

    @Override
    public void process(FractalImage image) {
        double max = 0.0;

        for (int row = 0; row < image.width(); row++) {
            for (int col = 0; col < image.height(); col++) {
                if (image.data()[row][col].hitCount() != 0) {
                    double normal = Math.log10(image.data()[row][col].hitCount());
                    max = Math.max(max, normal);
                }
            }
        }

        for (int row = 0; row < image.width(); row++) {
            for (int col = 0; col < image.height(); col++) {

                double normal;
                if (image.data()[row][col].hitCount() == 0) {
                    normal = 0;
                } else {
                    normal = Math.log10(image.data()[row][col].hitCount()) / max;
                }
                double gammaCorrectionFactor = Math.pow(normal, 1.0 / GAMMA);

                int red = (int) Math.round(image.data()[row][col].color().r() * gammaCorrectionFactor);
                int green = (int) Math.round(image.data()[row][col].color().g() * gammaCorrectionFactor);
                int blue = (int) Math.round(image.data()[row][col].color().b() * gammaCorrectionFactor);

                red = Math.max(0, Math.min(Color.MAX_BRIGHTNESS, red));
                green = Math.max(0, Math.min(Color.MAX_BRIGHTNESS, green));
                blue = Math.max(0, Math.min(Color.MAX_BRIGHTNESS, blue));

                image.data()[row][col] = new Pixel(new Color(red, green, blue), image.data()[row][col].hitCount());
            }
        }
    }
}
