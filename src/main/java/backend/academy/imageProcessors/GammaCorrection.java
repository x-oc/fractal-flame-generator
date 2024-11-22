package backend.academy.imageProcessors;

import backend.academy.Color;
import backend.academy.FractalImage;
import backend.academy.Pixel;

public class GammaCorrection implements ImageProcessor {

    @Override
    public void process(FractalImage image) {
        double max = 0.0;
        double gamma = 2.2;

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
                double normal = image.data()[row][col].hitCount() == 0 ? 0 : Math.log10(image.data()[row][col].hitCount()) / max;
                double gammaCorrectionFactor = Math.pow(normal, 1.0 / gamma);

                int red = (int) Math.round(image.data()[row][col].color().r() * gammaCorrectionFactor);
                int green = (int) Math.round(image.data()[row][col].color().g() * gammaCorrectionFactor);
                int blue = (int) Math.round(image.data()[row][col].color().b() * gammaCorrectionFactor);

                red = Math.max(0, Math.min(255, red));
                green = Math.max(0, Math.min(255, green));
                blue = Math.max(0, Math.min(255, blue));

                image.data()[row][col] = new Pixel(new Color(red, green, blue), image.data()[row][col].hitCount());
            }
        }
    }
}
