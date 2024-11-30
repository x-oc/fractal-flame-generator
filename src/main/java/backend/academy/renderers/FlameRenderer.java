package backend.academy.renderers;

import backend.academy.imageProcessors.GammaCorrection;
import backend.academy.imageProcessors.ImageProcessor;
import backend.academy.model.Color;
import backend.academy.model.FlameRenderParams;
import backend.academy.model.FractalImage;
import backend.academy.model.Pixel;
import backend.academy.model.Point;
import backend.academy.model.Rect;
import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Transformation;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public abstract class FlameRenderer {

    private static final int NORMALIZATION = 20;

    public FractalImage render(FlameRenderParams params) {

        List<ImageProcessor> processors = new ArrayList<>();
        processors.add(new GammaCorrection());

        if (params.random() == null) {
            params.random(new SecureRandom());
        }

        processPoints(params);

        for (ImageProcessor processor : processors) {
            processor.process(params.canvas());
        }

        return params.canvas();
    }

    abstract void processPoints(FlameRenderParams params);

    protected void processRandomPoint(FlameRenderParams params) {

        Point point = Point.random(params.world(), params.random());
        for (int step = -NORMALIZATION; step < params.iterPerSample(); ++step) {

            Transformation transformation = params.variations().get(
                params.random().nextInt(params.variations().size()));
            AffineTransformation affineTransformation = params.affineTransforms().get(
                params.random().nextInt(params.affineTransforms().size()));
            point = affineTransformation.apply(point);
            point = transformation.apply(point);

            if (step >= 0) {
                double theta2 = 0.0;

                for (int s = 0; s < params.symmetry(); ++s) {
                    point = rotate(point, theta2);
                    setPoint(params.world(), point, params.canvas(), affineTransformation);
                    theta2 += Math.PI * 2 / params.symmetry();
                }
            }
        }
    }

    private static Point rotate(Point p, double theta) {
        return new Point(p.x() * Math.cos(theta) - p.y() * Math.sin(theta),
                         p.x() * Math.sin(theta) + p.y() * Math.cos(theta));
    }

    private void setPoint(Rect world, Point point, FractalImage canvas, AffineTransformation transformation) {
        Point pointOnCanvas = mapPointToCanvas(world, point, canvas);

        Pixel pixel = canvas.pixel((int) pointOnCanvas.x(), (int) pointOnCanvas.y());
        if (pixel == null) {
            return;
        }

        if (pixel.hitCount() == 0) {
            canvas.data()[(int) pointOnCanvas.x()][(int) pointOnCanvas.y()] = new Pixel(
                transformation.color(), 1);
        } else {
            canvas.data()[(int) pointOnCanvas.x()][(int) pointOnCanvas.y()] = new Pixel(Color.blend(
                transformation.color(), pixel.color()), pixel.hitCount() + 1);
        }
    }

    private static Point mapPointToCanvas(Rect world, Point p, FractalImage canvas) {
        return new Point((p.x() - world.x()) * canvas.width() / world.width(),
                         (p.y() - world.y()) * canvas.height() / world.height());
    }
}
