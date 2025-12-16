package sunshine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OpenMeteoResponse {

    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;

    private Hourly hourly;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public Hourly getHourly() {
        return hourly;
    }

    @Override
    public String toString() {
        return "OpenMeteoResponse{" +
                "currentWeather=" + currentWeather +
                ", hourly=" + hourly +
                '}';
    }

    // ===== 내부 클래스: CurrentWeather =====
    public static class CurrentWeather {

        private double temperature;

        @JsonProperty("apparent_temperature")
        private double apparentTemperature;

        @JsonProperty("weathercode")
        private int weatherCode;

        public double getTemperature() {
            return temperature;
        }

        public double getApparentTemperature() {
            return apparentTemperature;
        }

        public int getWeatherCode() {
            return weatherCode;
        }

        @Override
        public String toString() {
            return "CurrentWeather{" +
                    "temperature=" + temperature +
                    ", apparentTemperature=" + apparentTemperature +
                    ", weatherCode=" + weatherCode +
                    '}';
        }
    }

    // ===== 내부 클래스: Hourly 데이터 =====
    public static class Hourly {

        @JsonProperty("relativehumidity_2m")
        private List<Double> relativeHumidity2m;

        public List<Double> getRelativeHumidity2m() {
            return relativeHumidity2m;
        }

        @Override
        public String toString() {
            return "Hourly{" +
                    "relativeHumidity2m=" + relativeHumidity2m +
                    '}';
        }
    }
}
