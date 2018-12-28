package matt.project.weather;

import static matt.project.weather.GoogleTimeZoneService.PROP_REF__API_KEY_GOOGLE_TIMEZONE;

public interface GoogleElevationService {

    String ROOT_URI__ELEVATION = "https://maps.googleapis.com/maps/api/elevation";
    String ENDPOINT_TEMPLATE__GET_ELEVATION = "/json?locations={latitude},{longitude}&key={apiKey}";
    String PROP_REF__API_KEY_GOOGLE_ELEVATION = PROP_REF__API_KEY_GOOGLE_TIMEZONE;

    /**
     * TODO
     *
     * @param latitude
     * @param longitude
     * @return
     */
    GoogleElevationData getElevation(double latitude, double longitude);
}
