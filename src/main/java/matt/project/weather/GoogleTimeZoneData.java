package matt.project.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class GoogleTimeZoneData {

    private String timeZoneName;

}
