package matt.project.weather;

public interface TimeZoneService {

    /**
     * @param latitude  a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return Time Zone information at the location represented by the {@code (latitude, longitude)} tuple
     */
    TimeZoneData retrieveTimeZone(double latitude, double longitude);
}
