package sunshine.ai;

public record WeatherFunctionRequest(
        String city,
        float currentTemperature,
        float apparentTemperature,
        int weatherCode,
        float humidity
) {}
