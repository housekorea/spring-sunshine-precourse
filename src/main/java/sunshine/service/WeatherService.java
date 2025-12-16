package sunshine.service;

// 1. @Service 붙여서 스프링 빈으로 등록한다.

// 2. OpenMeteoClient(아직 안 만들었으면 빈 자리만 마련) 를 주입받을 준비를 한다.

// 3. getWeather(float lat, float lon) 메서드를 만든다.
//    - 이 메서드는 API 호출을 담당한다.
//    - 지금은 진짜 호출하지 말고, "lat/lon 잘 들어왔는지"만 println으로 확인해도 됨.

// 4. 반환 타입은 나중에 WeatherInfo가 될 예정이므로, 지금은 임시로 String 또는 void로 만들어도 됨.

import org.springframework.stereotype.Service;
import sunshine.client.OpenMeteoClient;
import sunshine.domain.CityCoordinate;
import sunshine.domain.OpenMeteoResponse;
import sunshine.domain.Region;
import sunshine.domain.WeatherInfo;
import sunshine.repository.CityCoordinateRepository;
import sunshine.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
//    private WeatherInfo weatherInfo;
    private OpenMeteoClient openMeteoClient;
    private final RegionRepository regionRepository;
    private CityCoordinateRepository cityCoordinateRepository;

    public WeatherService(OpenMeteoClient openMeteoClient,
                          RegionRepository regionRepository,
                          CityCoordinateRepository cityCoordinateRepository) {
        this.openMeteoClient = openMeteoClient;
        this.regionRepository = regionRepository;
        this.cityCoordinateRepository = cityCoordinateRepository;
    }

    public WeatherInfo getWeather(float lat, float lon){
        WeatherInfo weatherInfo = null;

        try {
//            System.out.println("openMeteoClient is null? " + (openMeteoClient == null));

            OpenMeteoResponse openMeteoResponse = openMeteoClient.getWeather(lat, lon);
            weatherInfo = getWeatherInfo(openMeteoResponse);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return weatherInfo;

    }

    private WeatherInfo averageWeather(List<WeatherInfo> list) {

        float temp = 0, app = 0, hum = 0;

        for (WeatherInfo w : list) {
            temp += w.getCurrentTemp();
            app += w.getApparentTemp();
            hum += w.getHumidity();
        }

        int n = list.size();

        return new WeatherInfo(
                temp/n,
                app/n,
                list.get(0).getWeatherCode(),  // 대표값
                hum/n
        );
    }

    public WeatherInfo getRegionWeather(String regionName) {

        Region region = regionRepository.findByName(regionName);
        List<WeatherInfo> list = new ArrayList<>();

        for (String city : region.getAreas()) {
            System.out.println(city);
            CityCoordinate coord = cityCoordinateRepository.findByCity(city);
            list.add(getWeather(coord.getLat(), coord.getLon()));
        }

        return averageWeather(list);
    }


    private WeatherInfo getWeatherInfo(OpenMeteoResponse response){
        float currentTemp = (float) response.getCurrentWeather().getTemperature();
        float apparentTemp = (float) response.getCurrentWeather().getApparentTemperature();
        int weatherCode = response.getCurrentWeather().getWeatherCode();
        double humidity = 0.0F;
        if (response.getHourly() != null &&
                response.getHourly().getRelativeHumidity2m() != null &&
                !response.getHourly().getRelativeHumidity2m().isEmpty()) {

            humidity = response.getHourly().getRelativeHumidity2m().get(0);
        }

        return new WeatherInfo(currentTemp, apparentTemp, weatherCode, (float) humidity);
    }

}

