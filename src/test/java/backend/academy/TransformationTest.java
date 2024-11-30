package backend.academy;

import backend.academy.model.Point;
import backend.academy.transformations.Disc;
import backend.academy.transformations.Heart;
import backend.academy.transformations.Transformation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

public class TransformationTest {

    private static Stream<Arguments> providePointsForDisc() {
        return Stream.of(
            Arguments.of(new Point(0, 0), new Point(Double.NaN, Double.NaN)),
            Arguments.of(new Point(1, 1), new Point(-0.24097563321246931, -0.06656383551035391)),
            Arguments.of(new Point(-1, 0), new Point(-0.0, 0.0)),
            Arguments.of(new Point(0.5, -0.5), new Point(-0.19892330039187026, 0.15142496676970335)),
            Arguments.of(new Point(2, 3), new Point(-0.29579074248587306, 0.10184447156786326))
            );
    }

    @ParameterizedTest
    @MethodSource("providePointsForDisc")
    public void discTest(Point point, Point expected) {
        Transformation disc = new Disc();
        point = disc.apply(point);
        assertThat(point).isEqualTo(expected);
    }

    private static Stream<Arguments> providePointsForHeart() {
        return Stream.of(
            Arguments.of(new Point(0, 0), new Point(Double.NaN, Double.NaN)),
            Arguments.of(new Point(1, 1), new Point(1.2671621313307992, -0.6279332232978174)),
            Arguments.of(new Point(-1, 0), new Point(-0.0, -1.0)),
            Arguments.of(new Point(0.5, -0.5), new Point(-0.37282172672531666, -0.6008360509170146)),
            Arguments.of(new Point(2, 3), new Point(-1.4104430878355212, 3.318229994436341))
        );
    }

    @ParameterizedTest
    @MethodSource("providePointsForHeart")
    public void heartTest(Point point, Point expected) {
        Transformation heart = new Heart();
        point = heart.apply(point);
        assertThat(point).isEqualTo(expected);
    }

}
