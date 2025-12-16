package sunshine.ai;

public record OutfitRecommendationRequest(
        float currentTemperature,
        float apparentTemperature,
        int weatherCode,
        float humidity
) {}
