package sunshine.repository;

import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Repository;
import sunshine.domain.CityCoordinate;



// 1. repository 패키지 안에 CityCoordinateRepository 클래스를 만든다.

// 2. 도시 이름(String)을 key,
//    CityCoordinate 객체를 value 로 가지는 Map<String, CityCoordinate> 를 필드로 선언한다.

// 3. 생성자에서 최소 5개 도시의 좌표를 Map에 미리 채워 넣는다.
//    예: "Seoul" → new CityCoordinate(lat, lon)
//        "Tokyo" → new CityCoordinate(lat, lon)

// 4. findByCity(String city) 메서드를 만든다.

// 5. city 이름을 소문자/대문자 구분 없이 조회할 수 있도록
//    city.toLowerCase() 등을 사용해 Map에서 찾도록 한다.

// 6. 만약 Map 에 해당 도시가 없다면
//    - null 반환
//    또는
//    - IllegalArgumentException 같은 예외 던지기
//    중 하나를 선택한다.

// 7. 존재한다면 CityCoordinate 객체를 그대로 반환한다.

@Repository
public class CityCoordinateRepository {
    private Map<String, CityCoordinate> cityMap;

    public CityCoordinateRepository() {
        cityMap = new HashMap<>();
        cityMap.put("seoul",     new CityCoordinate(37.5665f, 126.9780f)); // 서울
        cityMap.put("busan",     new CityCoordinate(35.1796f, 129.0756f)); // 부산
        cityMap.put("incheon",   new CityCoordinate(37.4563f, 126.7052f)); // 인천
        cityMap.put("daegu",     new CityCoordinate(35.8714f, 128.6014f)); // 대구
        cityMap.put("daejeon",   new CityCoordinate(36.3504f, 127.3845f)); // 대전
        cityMap.put("gwangju",   new CityCoordinate(35.1595f, 126.8526f)); // 광주
        cityMap.put("ulsan",     new CityCoordinate(35.5384f, 129.3114f)); // 울산
        cityMap.put("sejong",    new CityCoordinate(36.4800f, 127.2890f)); // 세종

        cityMap.put("suwon",     new CityCoordinate(37.2636f, 127.0286f)); // 수원
        cityMap.put("yongin",    new CityCoordinate(37.2411f, 127.1776f)); // 용인
        cityMap.put("seongnam",  new CityCoordinate(37.4200f, 127.1265f)); // 성남
        cityMap.put("goyang",    new CityCoordinate(37.6584f, 126.8320f)); // 고양

        cityMap.put("chuncheon", new CityCoordinate(37.8813f, 127.7298f)); // 춘천
        cityMap.put("cheongju",  new CityCoordinate(36.6424f, 127.4890f)); // 청주
        cityMap.put("jeonju",    new CityCoordinate(35.8242f, 127.1480f)); // 전주
        cityMap.put("mokpo",     new CityCoordinate(34.8118f, 126.3922f)); // 목포
        cityMap.put("yeosu",     new CityCoordinate(34.7604f, 127.6622f)); // 여수

        cityMap.put("pohang",    new CityCoordinate(36.0190f, 129.3435f)); // 포항
        cityMap.put("changwon",  new CityCoordinate(35.2286f, 128.6811f)); // 창원
        cityMap.put("jinju",     new CityCoordinate(35.1795f, 128.1076f)); // 진주

        cityMap.put("jeju",      new CityCoordinate(33.4996f, 126.5312f)); // 제주
    }

    public CityCoordinate findByCity(String city){
        city = city.toLowerCase();
        return cityMap.get(city);
    }

    public boolean exists(String city) {
        return cityMap.containsKey(city.toLowerCase());
    }

}