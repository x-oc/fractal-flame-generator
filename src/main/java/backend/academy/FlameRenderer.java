package backend.academy;

import backend.academy.imageProcessors.GammaCorrection;
import backend.academy.imageProcessors.ImageProcessor;
import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Transformation;
import java.util.List;
import java.util.Random;

public class FlameRenderer {

    public FractalImage render(FractalImage canvas, Rect world, List<Transformation> transformations,
                        List<AffineTransformation> affineTransforms, int symmetry,
                        int samples, int iterPerSample, long seed) {

        Random random = new Random(seed);

        for (int num = 0; num < samples; ++num) {
            Point point = Point.random(world, random);

            for (int step = -20; step < iterPerSample; ++step) {

                Transformation transformation = transformations.get(
                    random.nextInt(transformations.size()));
                AffineTransformation affineTransformation = affineTransforms.get(
                    random.nextInt(affineTransforms.size()));
                point = transformation.apply(point);
                point = affineTransformation.apply(point);

                if (step >= 0) {
                    double theta2 = 0.0;

                    for (int s = 0; s < symmetry; ++s) {
                        point = rotate(point, theta2);
                        if (!world.contains(point)) continue;

                        Point pnt = map_range(world, point, canvas);

                        // todo: лок на время работы с пикселем

                        Pixel pixel = canvas.pixel((int) pnt.x(), (int) pnt.y());
                        if (pixel == null) continue;

                        if (pixel.hitCount() == 0) {
                            canvas.data()[(int) pnt.x()][(int) pnt.y()] = new Pixel(
                                affineTransformation.color(), 1);
                        } else {
                            canvas.data()[(int) pnt.x()][(int) pnt.y()] = new Pixel(Color.blend(
                                affineTransformation.color(), pixel.color()), pixel.hitCount() + 1);
                        }
                        theta2 += Math.PI * 2 / symmetry;
                    }
                }
            }
        }

        ImageProcessor processor = new GammaCorrection();
        processor.process(canvas);

        return canvas;
    }

    public FractalImage render(FractalImage canvas, Rect world, List<Transformation> variations,
                    List<AffineTransformation> affineTransforms, int symmetry, int samples, int iterPerSample) {
        Random random = new Random();
        return render(canvas, world, variations, affineTransforms, symmetry,
                      samples, iterPerSample, random.nextLong());
    }

    public static Point rotate(Point p, double theta) {
        return new Point(p.x() * Math.cos(theta) - p.y() * Math.sin(theta),
                         p.x() * Math.sin(theta) + p.y() * Math.cos(theta));
    }

    public static Point map_range(Rect world, Point p, FractalImage canvas) {
        return new Point((int) ((p.x() - world.x()) * canvas.width() / world.width()),
                         (int) ((p.y() - world.y()) * canvas.height() / world.height()));
    }
}
