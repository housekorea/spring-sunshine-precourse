package sunshine.controller;

import org.flywaydb.core.ProgressLogger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sunshine.ai.OutfitRecommendationRequest;
import sunshine.ai.OutfitRecommendationResponse;
import sunshine.domain.CityCoordinate;
import sunshine.domain.WeatherInfo;
import sunshine.repository.CityCoordinateRepository;
import sunshine.repository.RegionRepository;
import sunshine.service.WeatherService;
import sunshine.utils.TokenEstimator;
import sunshine.log.LlmUsageLogger;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final ChatClient client;

    private final CityCoordinateRepository cityCoordinateRepository;
    private final WeatherService weatherService;
    private RegionRepository regionRepository;
    private LlmUsageLogger llmUsageLogger;

    @Qualifier("recommendOutfit")
    private final Function<OutfitRecommendationRequest, OutfitRecommendationResponse> recommendOutfit;

    public WeatherController(CityCoordinateRepository cityCoordinateRepository, WeatherService weatherService,
                             ChatClient.Builder builder, RegionRepository regionRepository,  LlmUsageLogger llmUsageLogger, Function<OutfitRecommendationRequest,
                   OutfitRecommendationResponse> recommendOutfit
    ) {
        this.cityCoordinateRepository = cityCoordinateRepository;
        this.weatherService = weatherService;
        this.client = builder.build();
        this.regionRepository = regionRepository;

        this.recommendOutfit = recommendOutfit;
        this.llmUsageLogger = llmUsageLogger;

    }

//    @GetMapping
//    public String getWeather(@RequestParam String city) {
//        WeatherInfo weatherinfo = null;
//
//        // 1) 도시 이름으로 좌표 가져오기
//        CityCoordinate coordinate = cityCoordinateRepository.findByCity(city);
//        System.out.printf("lat : %f, lon %f", coordinate.getLat(), coordinate.getLon());
//        weatherinfo = weatherService.getWeather(coordinate.getLat(), coordinate.getLon());
////        System.out.printf("현재 %s의 기온은 %d도 이며, 체감 온도는 %d도, 날씨는 %s, 습도는 %.2f %% 입니다",
////                city, (int) weatherinfo.getCurrentTemp(), (int) weatherinfo.getApparentTemp(), weatherinfo.getWeather_kr(), weatherinfo.getHumidity());
//
//        var template = new PromptTemplate("OpenMeteo로 받아온 {city}에서의 현재 기온 {currentTemp}, 체감 기온 {ApparentTemp}, 날씨 코드 {weatherCode}, 습도 {humidity}를 바탕으로 날씨를 요약해줘. 날씨 코드는 OpenMeteo에서 표현하는 규격을 참고해줘");
//        var prompt = template.render(Map.of(
//                "city", city,
//                "currentTemp", (int) weatherinfo.getCurrentTemp(),
//                "ApparentTemp", (int) weatherinfo.getApparentTemp(),
//                "weatherCode", weatherinfo.getWeatherCode(),
//                "humidity", weatherinfo.getHumidity()
//        ));
//
//
//        return client.prompt(prompt).call().content();
//    }

    @GetMapping
    public String getWeather(@RequestParam String city) {
        WeatherInfo weatherinfo = null;
        String requestId = UUID.randomUUID().toString();

        if (cityCoordinateRepository.exists(city)) {
            // 단일 도시
            CityCoordinate coord = cityCoordinateRepository.findByCity(city);
            weatherinfo = weatherService.getWeather(coord.getLat(), coord.getLon());
        }
        else if (regionRepository.exists(city)) {
            // 수도권 같은 권역
            weatherinfo = weatherService.getRegionWeather(city);
        }
        else {
            throw new IllegalArgumentException("지원하지 않는 도시 또는 권역입니다.");
        }


        var template = new PromptTemplate("OpenMeteo로 받아온 {city}에서의 현재 기온 {currentTemp}, 체감 기온 {ApparentTemp}, 날씨 코드 {weatherCode}, 습도 {humidity}를 바탕으로 날씨를 요약해줘. 날씨 코드는 OpenMeteo에서 표현하는 규격을 참고해줘");
        var prompt = template.render(Map.of(
                "city", city,
                "currentTemp", (int) weatherinfo.getCurrentTemp(),
                "ApparentTemp", (int) weatherinfo.getApparentTemp(),
                "weatherCode", weatherinfo.getWeatherCode(),
                "humidity", weatherinfo.getHumidity()
        ));

        var llmResponse = client.prompt(prompt).call();
        String weatherSummary = llmResponse.content();

        // 버전 문제로 Gemini 응답에서 사용량 정보 추출 불가, estimator 사용
        // 4. 토큰 추정
        int inputTokens = TokenEstimator.estimate(prompt);
        int outputTokens = TokenEstimator.estimate(weatherSummary);

        // 5. 로그 남기기
        llmUsageLogger.log(
                requestId,
                "gemini-2.5-flash-lite",
                inputTokens,
                outputTokens
        );

        OutfitRecommendationRequest outfitRequest =
                new OutfitRecommendationRequest(
                        weatherinfo.getCurrentTemp(),
                        weatherinfo.getApparentTemp(),
                        weatherinfo.getWeatherCode(),
                        weatherinfo.getHumidity()
                );

        // 2. 복장 추천 (Function 호출)
        OutfitRecommendationResponse outfitResponse =
                recommendOutfit.apply(
                        new OutfitRecommendationRequest(
                                weatherinfo.getCurrentTemp(),
                                weatherinfo.getApparentTemp(),
                                weatherinfo.getWeatherCode(),
                                weatherinfo.getHumidity()
                        )
                );

        // 3. 결과 합치기
        return weatherSummary + "\n\n" + outfitResponse.recommendation();

    }


}
