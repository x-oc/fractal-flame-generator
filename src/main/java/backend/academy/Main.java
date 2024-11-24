package backend.academy;

import backend.academy.model.FlameRenderParams;
import backend.academy.model.FractalImage;
import backend.academy.model.ImageFormat;
import backend.academy.model.Rect;
import backend.academy.renderers.FlameRenderer;
import backend.academy.renderers.SingleThreadRenderer;
import backend.academy.transformations.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        testMode();
    }

    public static void testMode() {

        long time = System.currentTimeMillis();

        FlameRenderer renderer = new SingleThreadRenderer();
        FlameRenderParams.FlameRenderParamsBuilder params = FlameRenderParams.builder();

        List<Transformation> transformations = new ArrayList<>();
        transformations.add(new Heart());
        params.variations(transformations);

        List<AffineTransformation> affineTransformations = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            affineTransformations.add(AffineTransformation.random());
        }
        params.affineTransforms(affineTransformations);

        params.canvas(FractalImage.create(1920, 1080));
        params.world(new Rect(-1.777, -1, 3.555, 2));
        params.symmetry(1);
        params.samples(10000);
        params.iterPerSample(1000);
        params.random(new Random());

        FractalImage image = renderer.render(params.build());

        ImageUtils.save(image, Paths.get("fractal.png"), ImageFormat.PNG);

        System.out.println((double)(System.currentTimeMillis() - time) / 1000);
    }

    public static void userMode() {
        FlameRenderer renderer = UserInterface.getFlameRenderer();
        FlameRenderParams params = UserInterface.getParams();
        Path fileName = UserInterface.getOutputFileName();
        ImageFormat imageFormat = UserInterface.getOutputFileFormat();

        FractalImage image = renderer.render(params);
        ImageUtils.save(image, fileName, imageFormat);
    }
}
