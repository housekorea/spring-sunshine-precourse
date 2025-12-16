package sunshine.domain;

public class WeatherInfo {
    private float currentTemp;
    private float apparentTemp;
    private int weatherCode;
    private float humidity;
    private String weather_kr;

    public WeatherInfo(float currentTemp,  float apparentTemp, int weatherCode, float humidity) {
        this.currentTemp = currentTemp;
        this.apparentTemp = apparentTemp;
        this.weatherCode = weatherCode;
        this.humidity = humidity;
        this.weather_kr = WeatherCodeMapper.toKorean(weatherCode);
    }

    public float getCurrentTemp() {
        return currentTemp;
    }

    public float getApparentTemp() {
        return apparentTemp;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public float getHumidity() {
        return humidity;
    }

    public String getWeather_kr() {
        return weather_kr;
    }
    public class WeatherCodeMapper {

        public static String toKorean(int code) {
            if (code == 0) return "맑음";

            if (code == 1 || code == 2 || code == 3)
                return "대체로 흐림";

            if (code == 45 || code == 48)
                return "안개";

            if (code == 51 || code == 53 || code == 55)
                return "이슬비";

            if (code == 56 || code == 57)
                return "어는 비";

            if (code == 61 || code == 63 || code == 65)
                return "비";

            if (code == 66 || code == 67)
                return "어는 비";

            if (code == 71 || code == 73 || code == 75)
                return "눈";

            if (code == 77)
                return "싸락눈";

            if (code == 80 || code == 81 || code == 82)
                return "소나기";

            if (code == 85 || code == 86)
                return "눈 소나기";

            if (code == 95)
                return "천둥번개";

            if (code == 96 || code == 99)
                return "우박을 동반한 천둥";

            return "알 수 없음";
        }
    }


}

