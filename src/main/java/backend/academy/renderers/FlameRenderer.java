package backend.academy.renderers;

import backend.academy.model.Color;
import backend.academy.model.FlameRenderParams;
import backend.academy.model.FractalImage;
import backend.academy.model.Pixel;
import backend.academy.model.Point;
import backend.academy.model.Rect;
import backend.academy.imageProcessors.GammaCorrection;
import backend.academy.imageProcessors.ImageProcessor;
import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Transformation;
import java.util.ArrayList;
import java.util.List;

public abstract class FlameRenderer {

    public FractalImage render(FlameRenderParams params) {

        List<ImageProcessor> processors = new ArrayList<>();
        processors.add(new GammaCorrection());

        processPoints(params);

        for (ImageProcessor processor : processors) {
            processor.process(params.canvas());
        }

        return params.canvas();
    }

    abstract void processPoints(FlameRenderParams params);

    void processRandomPoint(FlameRenderParams params) {

        Point point = Point.random(params.world(), params.random());
        for (int step = -20; step < params.iterPerSample(); ++step) {

            Transformation transformation = params.variations().get(
                params.random().nextInt(params.variations().size()));
            AffineTransformation affineTransformation = params.affineTransforms().get(
                params.random().nextInt(params.affineTransforms().size()));
            point = transformation.apply(point);
            point = affineTransformation.apply(point);

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
        if (!world.contains(point)) return;

        point = mapPointToCanvas(world, point, canvas);

        Pixel pixel = canvas.pixel((int) point.x(), (int) point.y());
        if (pixel == null) return;

        synchronized (pixel) {
            if (pixel.hitCount() == 0) {
                canvas.data()[(int) point.x()][(int) point.y()] = new Pixel(
                    transformation.color(), 1);
            } else {
                canvas.data()[(int) point.x()][(int) point.y()] = new Pixel(Color.blend(
                    transformation.color(), pixel.color()), pixel.hitCount() + 1);
            }
        }
    }

    private static Point mapPointToCanvas(Rect world, Point p, FractalImage canvas) {
        return new Point((int) ((p.x() - world.x()) * canvas.width() / world.width()),
                         (int) ((p.y() - world.y()) * canvas.height() / world.height()));
    }
}
