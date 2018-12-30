package matt.project.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class OpenWeatherData {

    @JsonProperty("coord")
    private Map<String, Double> coordinates;

    private Map<String, Double> main;

    private String name;

    Double getLatitude()
    {
        return coordinates.get("lat");
    }

    Double getLongitude()
    {
        return coordinates.get("lon");
    }

    Double getTemperature()
    {
        return main.get("temp");
    }
}
