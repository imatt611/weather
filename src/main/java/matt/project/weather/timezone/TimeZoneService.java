package matt.project.weather.timezone;

public interface TimeZoneService {

    /**
     * @param latitude  a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return time zone information at the location represented by the {@code (latitude, longitude)} tuple
     */
    TimeZoneData retrieveTimeZone(double latitude, double longitude);

    /**
     * @param timeZoneData the time zone data
     * @return the name of the time zone
     */
    String getTimeZoneName(TimeZoneData timeZoneData);
}
