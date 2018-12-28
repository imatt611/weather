package matt.project.weather;

public interface GoogleTimeZoneService {

    String ROOT_URI = "https://maps.googleapis.com/maps/api/timezone";
    String GET_TIMEZONE_ENDPOINT_TEMPLATE = "/json?location={latitude},{longitude}&timestamp={timestamp}&key={apiKey}";
    String PROP_REF__API_KEY_GOOGLE_TIME_ZONE = "${api.key.google}";

    /**
     * TODO
     *
     * @param latitude
     * @param longitude
     * @return
     */
    GoogleTimeZoneData getTimeZone(double latitude, double longitude);
}
