package backend.academy;

import backend.academy.model.FlameRenderParams;
import backend.academy.model.FractalImage;
import backend.academy.model.ImageFormat;
import backend.academy.model.Rect;
import backend.academy.renderers.FlameRenderer;
import backend.academy.renderers.MultiThreadRenderer;
import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Spherical;
import backend.academy.transformations.Transformation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final static int MILLIS_IN_SECOND = 1000;

    public static void main(String[] args) {
        long time = System.currentTimeMillis();

        testMode();

        LOGGER.log(Level.INFO, String.valueOf((double) (System.currentTimeMillis() - time) / MILLIS_IN_SECOND));
    }

    @SuppressWarnings("MagicNumber")
    public static void testMode() {
        double scale = 2;
        double relation = 0.5625;

        FlameRenderer renderer = new MultiThreadRenderer();
        FlameRenderParams.FlameRenderParamsBuilder params = FlameRenderParams.builder();

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new Spherical());
        params.variations(transformations);

        int affineCount = 10;
        List<AffineTransformation> affineTransformations = new ArrayList<>(affineCount);
        for (int i = 0; i < affineCount; i++) {
            affineTransformations.add(AffineTransformation.random());
        }
        params.affineTransforms(affineTransformations);

        params.canvas(FractalImage.create(1920 * 2, 1080 * 2));
        params.world(new Rect(-scale, -scale * relation, 2 * scale, 2 * scale * relation));
        params.symmetry(3);
        params.samples(1000);
        params.iterPerSample(10000);

        FractalImage image = renderer.render(params.build());
        ImageUtils.save(image, Paths.get("fractal.png"), ImageFormat.PNG);
    }

    public static void userMode() {
        FlameRenderer renderer = UserInterface.getFlameRenderer();
        FlameRenderParams params = UserInterface.getFlameParams();
        Path fileName = UserInterface.getOutputFileName();
        ImageFormat imageFormat = UserInterface.getOutputFileFormat();

        FractalImage image = renderer.render(params);
        ImageUtils.save(image, fileName, imageFormat);
    }
}
