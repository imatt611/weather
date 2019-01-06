package matt.project.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class OpenWeatherData implements WeatherData {

    static final String KEY_MAIN_TEMP = "temp";
    static final String KEY_COORD_LAT = "lat";
    static final String KEY_COORD_LON = "lon";

    /**
     * {
     * "lon": float,
     * "lat": float
     * }
     */
    @JsonProperty("coord")
    private final Map<String, Double> coordinates = new HashMap<>(2);

    /**
     * {
     * "temp": float
     * "humidity": int,
     * "pressure": float,
     * "temp_min": float,
     * "temp_max": float
     * }
     */
    @JsonProperty("main")
    private final Map<String, Double> main = new HashMap<>(5);

    @Getter
    @Setter
    private String name;

    @Override
    public Double getLatitude()
    {
        return coordinates.get(KEY_COORD_LAT);
    }

    @Override
    public void setLatitude(Double latitude)
    {
        coordinates.put(KEY_COORD_LAT, latitude);
    }

    @Override
    public Double getLongitude()
    {
        return coordinates.get(KEY_COORD_LON);
    }

    @Override
    public void setLongitude(Double longitude)
    {
        coordinates.put(KEY_COORD_LON, longitude);
    }

    @Override
    public Double getTemperature()
    {
        return main.get(KEY_MAIN_TEMP);
    }

    @Override
    public void setTemperature(Double temperature)
    {
        main.put(KEY_MAIN_TEMP, temperature);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof WeatherData)) return false;
        WeatherData otherWeatherData = (WeatherData) o;
        if (!otherWeatherData.getName().equals(name)) return false;
        if (!otherWeatherData.getTemperature().equals(getTemperature())) return false;
        if (!otherWeatherData.getLatitude().equals(getLatitude())) return false;
        if (!otherWeatherData.getLongitude().equals(getLongitude())) return false;
        return true;
    }
}
