package backend.academy.transformations;

import backend.academy.Color;
import backend.academy.Point;
import java.util.function.Function;

// функция-преобразование
public interface Transformation extends Function<Point, Point> {
}
