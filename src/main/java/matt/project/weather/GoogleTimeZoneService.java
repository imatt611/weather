package matt.project.weather;

public interface GoogleTimeZoneService {

    /**
     * TODO
     *
     * @param latitude
     * @param longitude
     * @return
     */
    GoogleTimeZoneData getTimeZone(double latitude, double longitude);
}
