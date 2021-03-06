package matt.project.weather.util;

public enum GoogleApiConstants {
    ;
    public static final String PROP_REF__API_KEY_GOOGLE = "${api.key.google}";
    public static final String ROOT_URI = "https://maps.googleapis.com/maps/api";
    public static final String ENDPOINT_TEMPLATE__GET_ELEVATION = "/elevation/json?locations={latitude},{longitude}&key={apiKey}";
    public static final String ENDPOINT_TEMPLATE__GET_TIMEZONE = "/timezone/json?location={latitude},{longitude}&timestamp={timestamp}&key={apiKey}";
    public static final String TEMPLATE_VAR_NAME__LATITUDE = "latitude";
    public static final String TEMPLATE_VAR_NAME__LONGITUDE = "longitude";
    public static final String TEMPLATE_VAR_NAME__TIMESTAMP = "timestamp";
}
