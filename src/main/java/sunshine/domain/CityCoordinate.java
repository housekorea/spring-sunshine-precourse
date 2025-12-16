package sunshine.domain;

public class CityCoordinate {

    private float lat;
    private float lon;


    public CityCoordinate(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public  float getLat() {
        return lat;
    }
    public float getLon() {
        return lon;
    }


}