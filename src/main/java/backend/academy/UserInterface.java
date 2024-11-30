package backend.academy;

import backend.academy.model.FlameRenderParams;
import backend.academy.model.FractalImage;
import backend.academy.model.ImageFormat;
import backend.academy.model.Rect;
import backend.academy.renderers.FlameRenderer;
import backend.academy.renderers.MultiThreadRenderer;
import backend.academy.renderers.SingleThreadRenderer;
import backend.academy.transformations.AffineTransformation;
import backend.academy.transformations.Linear;
import backend.academy.transformations.Transformation;
import backend.academy.transformations.TransformationFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UserInterface {

    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final PrintStream PRINT_STREAM = System.out;
    private static final int AFFINE_DEFAULT = 10;
    private static final int WIDTH_DEFAULT = 1920;
    private static final int HEIGHT_DEFAULT = 1080;
    private static final Rect WORLD_DEFAULT = new Rect(-1.777, -1, 3.555, 2);
    private static final int SYMMETRY_DEFAULT = 1;
    private static final int SAMPLES_DEFAULT = 10000;
    private static final int ITER_PER_SAMPLE_DEFAULT = 1000;

    private UserInterface() {

    }

    public static FlameRenderer getFlameRenderer() {

        PRINT_STREAM.println("Ввод режима генерации пламени.");
        PRINT_STREAM.println("Однопоточный (1) / Многопоточный (2): ");

        String input = SCANNER.nextLine();
        if ("1".equals(input)) {
            return new SingleThreadRenderer();
        } else if ("2".equals(input)) {
            return new MultiThreadRenderer();
        }
        PRINT_STREAM.println("Будет использован режим по умолчанию (многопоточный)");
        return new MultiThreadRenderer();
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public static Path getOutputFileName() {
        PRINT_STREAM.println("Введите имя выходного файла: ");
        return Paths.get(SCANNER.nextLine());
    }

    public static ImageFormat getOutputFileFormat() {
        PRINT_STREAM.println("Введите формат выходного файла (" + Arrays.toString(ImageFormat.values()) + "): ");
        return ImageFormat.valueOf(SCANNER.nextLine());
    }

    public static FlameRenderParams getParams() {
        FlameRenderParams.FlameRenderParamsBuilder params = FlameRenderParams.builder();

        PRINT_STREAM.println("Ввод параметров генерации пламени.");

        params.variations(getTransformations());
        params.affineTransforms(getAffine());
        params.canvas(getCanvas());
        params.world(getWorld());
        params.symmetry(getSymmetry());
        params.samples(getSamples());
        params.iterPerSample(getIterPerSample());
        params.random(getRandom());

        return params.build();
    }

    private static List<Transformation> getTransformations() {
        List<Transformation> transformations = new ArrayList<>();

        PRINT_STREAM.println("Введите нелинейные преобразования "
            + "(" + TransformationFactory.TRANSFORM_NAMES + "), для окончания ввода введите Enter: ");

        String input = SCANNER.nextLine();

        while (!input.isEmpty()) {
            try {
                transformations.add(TransformationFactory.getTransformation(input));
            } catch (IllegalStateException e) {
                PRINT_STREAM.println("Такого преобразования нет!");
            }
            input = SCANNER.nextLine();
        }

        if (transformations.isEmpty()) {
            PRINT_STREAM.println("Будет использовано преобразование по умолчанию: Linear");
            transformations.add(new Linear());
        }

        return transformations;
    }

    private static List<AffineTransformation> getAffine() {
        PRINT_STREAM.println("Введите количество аффинных преобразований: ");

        int count = AFFINE_DEFAULT;
        try {
            count = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использовано количество аффинных преобразований по умолчанию: "
                + AFFINE_DEFAULT);
        }

        List<AffineTransformation> affineTransformations = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            affineTransformations.add(AffineTransformation.random());
        }

        return affineTransformations;
    }

    private static FractalImage getCanvas() {
        PRINT_STREAM.println("Введите размер итогового изображения ");
        int width = WIDTH_DEFAULT;
        int height = HEIGHT_DEFAULT;

        PRINT_STREAM.println("Введите ширину: ");
        try {
            width = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использована ширина по умолчанию: " + WIDTH_DEFAULT);
        }

        PRINT_STREAM.println("Введите высоту: ");
        try {
            height = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использована высота по умолчанию: " + HEIGHT_DEFAULT);
        }

        return FractalImage.create(width, height);
    }

    private static Rect getWorld() {
        PRINT_STREAM.println("Введите размеры области (x, y, width, height)");
        Rect world = WORLD_DEFAULT;

        try {
            double x = Double.parseDouble(SCANNER.nextLine());
            double y = Double.parseDouble(SCANNER.nextLine());
            double width = Double.parseDouble(SCANNER.nextLine());
            double height = Double.parseDouble(SCANNER.nextLine());
            world = new Rect(x, y, width, height);
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будут использованы значения по умолчанию");
        }

        return world;
    }

    private static int getSymmetry() {
        PRINT_STREAM.println("Введите количество осей симметрии: ");
        int symmetry = SYMMETRY_DEFAULT;

        try {
            symmetry = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использовано количество осей по умолчанию: " + SYMMETRY_DEFAULT);
        }

        return symmetry;
    }

    private static int getSamples() {
        PRINT_STREAM.println("Введите количество начальных пикселей: ");
        int samples = SAMPLES_DEFAULT;

        try {
            samples = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использовано количество пикселей по умолчанию: " + SAMPLES_DEFAULT);
        }

        return samples;
    }

    private static int getIterPerSample() {
        PRINT_STREAM.println("Введите количество итераций на пиксель: ");
        int iterPerSample = ITER_PER_SAMPLE_DEFAULT;

        try {
            iterPerSample = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использовано количество итераций по умолчанию: " + ITER_PER_SAMPLE_DEFAULT);
        }

        return iterPerSample;
    }

    @SuppressWarnings("MagicNumber")
    private static Random getRandom() {
        PRINT_STREAM.println("Введите сид рандома: ");
        Random random = new SecureRandom();

        try {
            random = new SecureRandom(ByteBuffer.allocate(4).putInt(
                Integer.parseInt(SCANNER.nextLine())).array());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использовано случайный сид рандома");
        }

        return random;
    }
}
