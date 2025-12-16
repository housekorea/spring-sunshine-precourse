package sunshine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "llm.pricing")
public class LlmPricingProperties {

    private Map<String, Price> models = new HashMap<>();

    // ⭐ 기본 생성자에서 기본 pricing 세팅
    public LlmPricingProperties() {
        // Gemini 2.5 Flash Lite 기본 가격
        Price gemini25FlashLite = new Price();
        gemini25FlashLite.setInput(0.00010);
        gemini25FlashLite.setOutput(0.00040);

        models.put("gemini-2.5-flash-lite", gemini25FlashLite);
    }

    public Map<String, Price> getModels() {
        return models;
    }

    public void setModels(Map<String, Price> models) {
        // properties로 값이 들어오면 기본값을 덮어씀
        if (models != null && !models.isEmpty()) {
            this.models = models;
        }
    }

    public static class Price {
        private double input;
        private double output;

        public double getInput() {
            return input;
        }

        public void setInput(double input) {
            this.input = input;
        }

        public double getOutput() {
            return output;
        }

        public void setOutput(double output) {
            this.output = output;
        }
    }
}
