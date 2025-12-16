package sunshine.cost;

import org.springframework.stereotype.Component;
import sunshine.config.LlmPricingProperties;

@Component
public class LlmCostCalculator {

    private final LlmPricingProperties pricingProperties;

    public LlmCostCalculator(LlmPricingProperties pricingProperties) {
        this.pricingProperties = pricingProperties;
    }

    public double calculate(String model, int inputTokens, int outputTokens) {
        LlmPricingProperties.Price price =
                pricingProperties.getModels().get(model);

        if (price == null) {
            throw new IllegalArgumentException("Unknown LLM model: " + model);
        }

        return (inputTokens / 1000.0 * price.getInput())
                + (outputTokens / 1000.0 * price.getOutput());
    }
}
