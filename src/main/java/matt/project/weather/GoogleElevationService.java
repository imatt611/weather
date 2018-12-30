package matt.project.weather;

import static matt.project.weather.GoogleTimeZoneService.PROP_REF__API_KEY_GOOGLE_TIMEZONE;

public interface GoogleElevationService {

    String ROOT_URI__ELEVATION = "https://maps.googleapis.com/maps/api/elevation";
    String ENDPOINT_TEMPLATE__GET_ELEVATION = "/json?locations={latitude},{longitude}&key={apiKey}";
    String PROP_REF__API_KEY_GOOGLE_ELEVATION = PROP_REF__API_KEY_GOOGLE_TIMEZONE;

    /**
     * @param latitude  a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return Elevation information at the location represented by the {@code (latitude, longitude)} tuple
     */
    GoogleElevationData getElevation(double latitude, double longitude);
}
