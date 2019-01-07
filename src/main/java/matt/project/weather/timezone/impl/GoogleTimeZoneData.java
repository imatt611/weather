package matt.project.weather.timezone.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import matt.project.weather.timezone.TimeZoneData;

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleTimeZoneData implements TimeZoneData {

    @Getter
    @Setter
    private String timeZoneName;

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (!(obj instanceof TimeZoneData)) return false;
        TimeZoneData otherTimeZoneData = (TimeZoneData) obj;
        return otherTimeZoneData.getTimeZoneName().equals(timeZoneName);
    }
}
