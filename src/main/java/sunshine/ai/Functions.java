package sunshine.ai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import java.time.LocalDate;

@Configuration
public class Functions {

    @Description("요청된 도시의 날씨 데이터를 요약 문장으로 생성합니다.")
    @Bean("makeWeatherSummary")
    public Function<WeatherFunctionRequest, WeatherFunctionResponse> makeWeatherSummary() {

        return request -> {

            String city = request.city();
            float currentTemperature = request.currentTemperature();
            float apparentTemperature = request.apparentTemperature();
            int weatherCode = request.weatherCode();
            float humidity = request.humidity();

            // 자연스러운 한 줄 요약 생성
            String summary = String.format(
                    "현재 %s의 기온은 %.1f°C이며, 체감온도는 %.1f°C입니다. 하늘 상태는 '%d'이며 습도는 %.0f%%입니다.",
                    city, currentTemperature, apparentTemperature, weatherCode, humidity
            );

            return new WeatherFunctionResponse(summary);
        };
    }

    private boolean isRain(int code) {
        return (code >= 51 && code <= 67) || (code >= 80 && code <= 82);
    }

    private boolean isSnow(int code) {
        return (code >= 71 && code <= 77) || code == 85 || code == 86;
    }

    private boolean isClear(int code) {
        return code == 0 || code == 1;
    }


    @Description("Open-Meteo 날씨 데이터를 기준으로 오늘 입기 좋은 복장을 추천한다.")
    @Bean("recommendOutfit")
    public Function<OutfitRecommendationRequest, OutfitRecommendationResponse> recommendOutfit() {

        return request -> {

            // 1. 기준 온도 선택
            float baseTemp = request.apparentTemperature() > 0
                    ? request.apparentTemperature()
                    : request.currentTemperature();

            String outer;
            String top;
            String bottom;
            String shoes = "운동화";
            List<String> extras = new ArrayList<>();

            // 2. 기온 기준 복장
            if (baseTemp <= -5) {
                outer = "두꺼운 패딩";
                top = "기모 상의";
                bottom = "기모 바지";
                extras.add("목도리");
                extras.add("장갑");
            }
            else if (baseTemp <= 4) {
                outer = "코트나 패딩";
                top = "니트";
                bottom = "긴바지";
            }
            else if (baseTemp <= 9) {
                outer = "자켓";
                top = "후드나 니트";
                bottom = "긴바지";
            }
            else if (baseTemp <= 16) {
                outer = "얇은 자켓";
                top = "맨투맨이나 셔츠";
                bottom = "슬랙스나 청바지";
            }
            else if (baseTemp <= 22) {
                outer = "가벼운 가디건";
                top = "반팔이나 얇은 긴팔";
                bottom = "면바지";
            }
            else if (baseTemp <= 27) {
                outer = "겉옷 없이";
                top = "반팔";
                bottom = "가벼운 바지";
            }
            else {
                outer = "겉옷 없이";
                top = "민소매나 반팔";
                bottom = "반바지";
                extras.add("선크림");
            }

            // 3. 강수 보정 (weatherCode)
            if (isRain(request.weatherCode())) {
                outer += " (방수)";
                shoes = "방수 운동화";
                extras.add("우산");
            }

            if (isSnow(request.weatherCode())) {
                shoes = "미끄럼 방지 신발";
                extras.add("장갑");
            }

            // 4. 습도 보정
            if (request.humidity() >= 75 && baseTemp >= 23) {
                extras.add("통풍 잘 되는 옷");
            }

            // 5. 문장 생성
            StringBuilder sb = new StringBuilder();
            sb.append("오늘은 ");

            if (!outer.contains("없이")) {
                sb.append(outer).append("에 ");
            }

            sb.append(top)
                    .append("을 입고 ")
                    .append(bottom)
                    .append("를 입으면 좋아요. ")
                    .append(shoes)
                    .append("를 추천해요.");

            if (!extras.isEmpty()) {
                sb.append(" ");
                sb.append(String.join(", ", extras))
                        .append("도 챙기세요.");
            }

            return new OutfitRecommendationResponse(sb.toString());
        };
    }


}

