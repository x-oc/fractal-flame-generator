package backend.academy;

import backend.academy.model.FlameRenderParams;
import backend.academy.model.FractalImage;
import backend.academy.model.Rect;
import backend.academy.renderers.FlameRenderer;
import backend.academy.renderers.MultiThreadRenderer;
import backend.academy.renderers.SingleThreadRenderer;
import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Linear;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class RenderTest {

    private static final FlameRenderParams params;

    static {
        FlameRenderParams.FlameRenderParamsBuilder paramsBuilder = FlameRenderParams.builder();
        paramsBuilder.variations(List.of(new Linear()));

        int affineCount = 10;
        List<AffineTransformation> affineTransformations = new ArrayList<>(affineCount);
        for (int i = 0; i < affineCount; i++) {
            affineTransformations.add(AffineTransformation.random());
        }
        paramsBuilder.affineTransforms(affineTransformations);

        paramsBuilder.canvas(FractalImage.create(100, 100));
        paramsBuilder.world(new Rect(-1, -1, 2, 2));
        paramsBuilder.symmetry(2);
        paramsBuilder.samples(100);
        paramsBuilder.iterPerSample(100);
        params = paramsBuilder.build();
    }

    @Test
    public void SingleThreadTest() {
        FlameRenderer renderer = new SingleThreadRenderer();
        Assertions.assertDoesNotThrow(() -> renderer.render(params));
    }

    @Test
    public void MultiThreadTest() {
        FlameRenderer renderer = new MultiThreadRenderer();
        Assertions.assertDoesNotThrow(() -> renderer.render(params));
    }

}
