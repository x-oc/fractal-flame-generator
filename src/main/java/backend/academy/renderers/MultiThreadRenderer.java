package backend.academy.renderers;

import backend.academy.model.FlameRenderParams;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadRenderer extends FlameRenderer {

    @Override
    void processPoints(FlameRenderParams params) {
        try (var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < params.samples(); i++) {
                executorService.execute(
                    () -> processRandomPoint(params)
                );
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
