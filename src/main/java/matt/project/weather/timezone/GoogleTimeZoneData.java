package matt.project.weather.timezone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class GoogleTimeZoneData implements TimeZoneData {

    private String timeZoneName;

}
