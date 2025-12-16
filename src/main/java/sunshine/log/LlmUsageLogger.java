package sunshine.log;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sunshine.cost.LlmCostCalculator;

@Service
public class LlmUsageLogger {

    private static final Logger log =
            (Logger) LoggerFactory.getLogger(LlmUsageLogger.class);

    private final LlmCostCalculator costCalculator;

    public LlmUsageLogger(LlmCostCalculator costCalculator) {
        this.costCalculator = costCalculator;
    }

    public void log(
            String requestId,
            String model,
            int inputTokens,
            int outputTokens
    ) {
        int totalTokens = inputTokens + outputTokens;
        double costUsd = costCalculator.calculate(
                model, inputTokens, outputTokens
        );

        log.info(
                "[LLM_USAGE] requestId={}, model={}, inputTokens={}, outputTokens={}, totalTokens={}, estimatedCostUsd={}",
                requestId,
                model,
                inputTokens,
                outputTokens,
                totalTokens,
                costUsd
        );
    }
}


