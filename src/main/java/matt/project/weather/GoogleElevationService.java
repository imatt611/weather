package matt.project.weather;

import static matt.project.weather.GoogleTimeZoneService.PROP_REF__API_KEY_GOOGLE_TIMEZONE;

public interface GoogleElevationService {

    String ROOT_URI__ELEVATION = "https://maps.googleapis.com/maps/api/elevation";
    String ENDPOINT_TEMPLATE__GET_ELEVATION = "/json?locations={latitude},{longitude}&key={apiKey}";
    String PROP_REF__API_KEY_GOOGLE_ELEVATION = PROP_REF__API_KEY_GOOGLE_TIMEZONE;

    /**
     * @param latitude the latitude which, paired with {@code longitude}, represents a geographic location
     * @param longitude the longitude which, paired with {@code latitude}, represents a geographic location
     * @return Elevation information at the location represented by the {@code latitude, longitude} tuple
     */
    GoogleElevationData getElevation(double latitude, double longitude);
}
