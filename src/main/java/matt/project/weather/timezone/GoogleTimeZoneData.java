package matt.project.weather.timezone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import matt.project.weather.TimeZoneData;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class GoogleTimeZoneData implements TimeZoneData {

    private String timeZoneName;

}
