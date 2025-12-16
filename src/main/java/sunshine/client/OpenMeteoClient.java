package sunshine.client;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import sunshine.domain.OpenMeteoResponse;
import sunshine.domain.WeatherInfo;

@Component
public class OpenMeteoClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public OpenMeteoResponse getWeather(float lat, float lon) {

        // 1. API URL 만들기
        String url = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&current_weather=true"
                + "&hourly=relativehumidity_2m";

        // 2. API 호출해서 DTO로 받기
        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

        // 3. 응답이 null이면 예외
        if (response == null || response.getCurrentWeather() == null) {
            throw new RuntimeException("날씨 정보를 가져올 수 없습니다.");
        }

        return response;
    }

//    private WeatherInfo convertToWeatherInfo(OpenMeteoResponse response) {
//        float temperature = response.current_weather.temperature;
//        float apparentTemperature = response.current_weather.apparent_temperature;
//        int weatherCode = response.current_weather.weathercode;
//        float humidity = response.hourly.relativehumidity_2m[0]; // 첫 번째 값 사용
//
//        return new WeatherInfo(
//                temperature,
//                apparentTemperature,
//                weatherCode,
//                humidity
//        );
//    }


}