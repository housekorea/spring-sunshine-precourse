package sunshine.repository;

import org.springframework.stereotype.Repository;
import sunshine.domain.Region;
import sunshine.domain.RegionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RegionRepository {

    private final Map<String, Region> regions = new HashMap<>();

    public RegionRepository() {

        // 단일 도시 등록(도시 이름 그대로)
        regions.put("Seoul", new Region("Seoul", RegionType.CITY, List.of("Seoul")));

        // 수도권 예시
        regions.put("수도권", new Region("수도권", RegionType.METRO_AREA,
                List.of("Seoul", "Incheon")));

        // 동/구 예시
        regions.put("강남구", new Region("강남구", RegionType.DISTRICT,
                List.of("Gangnam1", "Gangnam2")));
    }

    public Region findByName(String name) {
        return regions.get(name);
    }

    public boolean exists(String name) {
        return regions.containsKey(name);
    }
}
