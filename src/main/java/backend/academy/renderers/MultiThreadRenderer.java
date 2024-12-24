package backend.academy.renderers;

import backend.academy.model.FlameRenderParams;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MultiThreadRenderer extends FlameRenderer {

    @Override
    @SuppressFBWarnings("PREDICTABLE_RANDOM")
    void processPoints(FlameRenderParams params) {
        try (var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < params.samples(); i++) {
                params.random(ThreadLocalRandom.current());
                executorService.execute(
                    () -> processRandomPoint(params)
                );
            }
            executorService.close();
            try {
                executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
