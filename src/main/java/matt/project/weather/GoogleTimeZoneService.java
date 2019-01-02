package matt.project.weather;

public interface GoogleTimeZoneService {

    /**
     * @param latitude  a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return Time Zone information at the location represented by the {@code (latitude, longitude)} tuple
     */
    GoogleTimeZoneData retrieveTimeZone(double latitude, double longitude);
}
