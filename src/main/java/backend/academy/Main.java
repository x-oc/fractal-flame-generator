package backend.academy;

import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Disc;
import backend.academy.transformations.Transformation;
import lombok.experimental.UtilityClass;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        FlameRenderer renderer = new FlameRenderer();

        List<Transformation> transformations = new ArrayList<>();
        List<AffineTransformation> affineTransformations = new ArrayList<>();

        transformations.add(new Disc());
        for (int i = 0; i < 10; i++) {
            affineTransformations.add(AffineTransformation.random());
        }

        FractalImage image = renderer.render(FractalImage.create(1920, 1080), new Rect(-1.8, -1, 3.6, 2),
            transformations, affineTransformations, 1, 10, 1000000);

        ImageUtils.save(image, Paths.get("fractal.png"), ImageFormat.PNG);
    }
}
