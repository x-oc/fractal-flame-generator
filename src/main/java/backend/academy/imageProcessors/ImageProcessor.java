package backend.academy.imageProcessors;

import backend.academy.model.FractalImage;

// пост-обработка in-place, например, гамма-коррекция
@FunctionalInterface
public
interface ImageProcessor {
    void process(FractalImage image);
}
