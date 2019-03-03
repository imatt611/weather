package matt.project.weather.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import matt.project.weather.timezone.TimeZoneData;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleTimeZoneData implements TimeZoneData {

    private String timeZoneName;
}
