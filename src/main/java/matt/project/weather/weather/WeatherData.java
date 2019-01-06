package matt.project.weather.weather;

public interface WeatherData {
    Double getLatitude();

    void setLatitude(Double latitude);

    Double getLongitude();

    void setLongitude(Double longitude);

    Double getTemperature();

    void setTemperature(Double temperature);

    String getName();

    void setName(String name);
}
