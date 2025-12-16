package sunshine.domain;

import java.util.List;

public class Region {
    private String name;
    private RegionType type;
    private List<String> areas;  // 포함된 도시목록

    public Region(String name, RegionType type, List<String> areas) {
        this.name = name;
        this.type = type;
        this.areas = areas;
    }

    public String getName() { return name; }
    public RegionType getType() { return type; }
    public List<String> getAreas() { return areas; }
}
