package matt.project.weather;

public interface GoogleTimeZoneService {

    String ROOT_URI__TIMEZONE = "https://maps.googleapis.com/maps/api/timezone";
    String ENDPOINT_TEMPLATE__GET_TIMEZONE = "/json?location={latitude},{longitude}&timestamp={timestamp}&key={apiKey}";
    String PROP_REF__API_KEY_GOOGLE_TIMEZONE = "${api.key.google}";

    /**
     * @param latitude a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return Time Zone information at the location represented by the {@code (latitude, longitude)} tuple
     */
    GoogleTimeZoneData getTimeZone(double latitude, double longitude);
}
