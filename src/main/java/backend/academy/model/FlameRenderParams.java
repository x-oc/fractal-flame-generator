package backend.academy.model;

import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Transformation;
import java.util.List;
import java.util.Random;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FlameRenderParams {

    private FractalImage canvas;
    private Rect world;
    private List<Transformation> variations;
    private List<AffineTransformation> affineTransforms;
    private int symmetry;
    private int samples;
    private int iterPerSample;
    private Random random;

}
