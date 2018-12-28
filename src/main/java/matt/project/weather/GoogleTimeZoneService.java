package matt.project.weather;

public interface GoogleTimeZoneService {

    String ROOT_URI__TIMEZONE = "https://maps.googleapis.com/maps/api/timezone";
    String ENDPOINT_TEMPLATE__GET_TIMEZONE = "/json?location={latitude},{longitude}&timestamp={timestamp}&key={apiKey}";
    String PROP_REF__API_KEY_GOOGLE_TIMEZONE = "${api.key.google}";

    /**
     * TODO
     *
     * @param latitude
     * @param longitude
     * @return
     */
    GoogleTimeZoneData getTimeZone(double latitude, double longitude);
}
