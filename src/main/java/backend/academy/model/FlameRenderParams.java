package backend.academy.model;

import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Transformation;
import java.util.List;
import java.util.Random;
import lombok.Builder;

@Builder
public record FlameRenderParams(FractalImage canvas,
                                Rect world,
                                List<Transformation> variations,
                                List<AffineTransformation> affineTransforms,
                                int symmetry,
                                int samples,
                                int iterPerSample,
                                Random random) {

}
