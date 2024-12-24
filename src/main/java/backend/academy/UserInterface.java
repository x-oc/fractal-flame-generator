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
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserInterface {

    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);
    private static final PrintStream PRINT_STREAM = System.out;
    private static final int AFFINE_DEFAULT = 10;
    private static final int WIDTH_DEFAULT = 1920;
    private static final int HEIGHT_DEFAULT = 1080;
    private static final double WORLD_MIN_X = -1.777;
    private static final double WORLD_MIN_Y = -1;
    private static final Rect WORLD_DEFAULT = new Rect(WORLD_MIN_X, WORLD_MIN_Y,
                                                       Math.abs(WORLD_MIN_X * 2), Math.abs(WORLD_MIN_Y * 2));
    private static final int SYMMETRY_DEFAULT = 1;
    private static final int SAMPLES_DEFAULT = 10000;
    private static final int ITER_PER_SAMPLE_DEFAULT = 1000;
    private static final ImageFormat IMAGE_FORMAT_DEFAULT = ImageFormat.PNG;

    public FlameRenderer getFlameRenderer() {

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
    public Path getOutputFileName() {
        PRINT_STREAM.println("Введите имя выходного файла: ");
        return Paths.get(SCANNER.nextLine());
    }

    public ImageFormat getOutputFileFormat() {
        PRINT_STREAM.println("Введите формат выходного файла (" + Arrays.toString(ImageFormat.values()) + "): ");
        ImageFormat format = IMAGE_FORMAT_DEFAULT;
        try {
            format = ImageFormat.valueOf(SCANNER.nextLine());
        } catch (IllegalArgumentException e) {
            PRINT_STREAM.println("Будет использован формат по умолчанию: " + IMAGE_FORMAT_DEFAULT);
        }
        return format;
    }

    public FlameRenderParams getFlameParams() {
        FlameRenderParams.FlameRenderParamsBuilder params = FlameRenderParams.builder();

        PRINT_STREAM.println("Ввод параметров генерации пламени.");

        params.variations(getTransformations());
        params.affineTransforms(getAffineTransformations());
        params.canvas(getCanvas());
        params.world(getWorld());
        params.symmetry(getSymmetry());
        params.samples(getSamples());
        params.iterPerSample(getIterPerSample());
        params.random(getRandom());

        return params.build();
    }

    private List<Transformation> getTransformations() {
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

    private List<AffineTransformation> getAffineTransformations() {
        int count = getInt("количество аффинных преобразований", AFFINE_DEFAULT);

        List<AffineTransformation> affineTransformations = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            affineTransformations.add(AffineTransformation.random());
        }

        return affineTransformations;
    }

    private FractalImage getCanvas() {
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

    private Rect getWorld() {
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

    private int getSymmetry() {
        return getInt("количество осей симметрии", SYMMETRY_DEFAULT);
    }

    private int getSamples() {
        return getInt("количество начальных пикселей", SAMPLES_DEFAULT);
    }

    private int getIterPerSample() {
        return getInt("количество итераций на пиксель", ITER_PER_SAMPLE_DEFAULT);
    }

    private int getInt(String valueName, int defaultValue) {
        PRINT_STREAM.println("Введите " + valueName + ": ");
        int value = defaultValue;

        try {
            value = Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            PRINT_STREAM.println("Будет использовано " + valueName + " по умолчанию: " + defaultValue);
        }

        return value;
    }

    @SuppressWarnings("MagicNumber")
    private Random getRandom() {
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
