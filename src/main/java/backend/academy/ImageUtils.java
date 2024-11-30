package backend.academy;

import backend.academy.model.FractalImage;
import backend.academy.model.ImageFormat;
import backend.academy.model.Pixel;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public final class ImageUtils {

    private ImageUtils() {}

    public static void save(FractalImage image, Path filename, ImageFormat format) {
        if (format == ImageFormat.PNG) {
            savePNG(image, filename);
        }
    }

    private static void savePNG(FractalImage image, Path filename) {
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = bufferedImage.getRaster();

        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                if (pixel != null) {
                    raster.setSample(x, y, 0, pixel.color().r());
                    raster.setSample(x, y, 1, pixel.color().g());
                    raster.setSample(x, y, 2, pixel.color().b());
                }
            }
        }

        try {
            ImageIO.write(bufferedImage, "png", filename.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
