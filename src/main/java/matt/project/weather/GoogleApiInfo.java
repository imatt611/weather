package matt.project.weather;

enum GoogleApiInfo {
    ;
    static final String PROP_REF__API_KEY_GOOGLE = "${api.key.google}";
    static final String ROOT_URI = "https://maps.googleapis.com/maps/api";
    static final String ENDPOINT_TEMPLATE__GET_ELEVATION = "/elevation/json?locations={latitude},{longitude}&key={apiKey}";
    static final String ENDPOINT_TEMPLATE__GET_TIMEZONE = "/timezone/json?location={latitude},{longitude}&timestamp={timestamp}&key={apiKey}";
    static final String TEMPLATE_VAR_NAME__LATITUDE = "latitude";
    static final String TEMPLATE_VAR_NAME__LONGITUDE = "longitude";
    static final String TEMPLATE_VAR_NAME__API_KEY = "apiKey";
    static final String TEMPLATE_VAR_NAME__TIMESTAMP = "timestamp";
}
