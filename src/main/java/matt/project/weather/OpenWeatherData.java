package matt.project.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class OpenWeatherData {

    static final String KEY_MAIN_TEMP = "temp";
    static final String KEY_COORD_LAT = "lat";
    static final String KEY_COORD_LON = "lon";

    @JsonProperty("coord")
    private Map<String, Double> coordinates;

    private Map<String, Double> main;

    private String name;

    Double getLatitude()
    {
        return coordinates.get(KEY_COORD_LAT);
    }

    Double getLongitude()
    {
        return coordinates.get(KEY_COORD_LON);
    }

    Double getTemperature()
    {
        return main.get(KEY_MAIN_TEMP);
    }
}
