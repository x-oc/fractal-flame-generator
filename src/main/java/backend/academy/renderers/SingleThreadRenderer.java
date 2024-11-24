package backend.academy.renderers;

import backend.academy.model.FlameRenderParams;

public class SingleThreadRenderer extends FlameRenderer {

    @Override
    void processPoints(FlameRenderParams params) {
        for (int num = 0; num < params.samples(); ++num) {
            processRandomPoint(params);
        }
    }
}
