package matt.project.weather.openweather;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import matt.project.weather.weather.WeatherData;

/**
 * OpenWeather implementation.
 *
 * @see <a href="https://samples.openweathermap.org/data/2.5/weather?zip=94040,us&appid=b6907d289e10d714a6e88b30761fae22">Example JSON data from OpenWeather</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class OpenWeatherData implements WeatherData {

    private static final String KEY_MAIN_TEMP = "temp";
    private static final String KEY_COORD_LAT = "lat";
    private static final String KEY_COORD_LON = "lon";

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
     * "temp": float (Kelvin)
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
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (!(obj instanceof WeatherData)) return false;
        WeatherData otherWeatherData = (WeatherData) obj;
        if (!otherWeatherData.getName().equals(name)) return false;
        if (!otherWeatherData.getTemperature().equals(getTemperature())) return false;
        if (!otherWeatherData.getLatitude().equals(getLatitude())) return false;
        return otherWeatherData.getLongitude().equals(getLongitude());
    }
}
