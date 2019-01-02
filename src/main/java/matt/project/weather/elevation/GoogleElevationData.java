package matt.project.weather.elevation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import matt.project.weather.ElevationData;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleElevationData implements ElevationData {

    @Getter
    @Setter
    private double elevation;

    @SuppressWarnings("unchecked")
    @JsonProperty("results")
    private void unpack(List<Object> results)
    {
        // Assumes only a single result due to a single (lat, long) tuple in API request
        Map<String, Object> result = (Map<String, Object>) results.get(0);
        elevation = (double) result.get("elevation");
    }

}
