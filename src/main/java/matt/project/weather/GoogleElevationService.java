package matt.project.weather;

public interface GoogleElevationService {

    /**
     * TODO
     *
     * @param latitude
     * @param longitude
     * @return
     */
    GoogleElevationData getElevation(double latitude, double longitude);
}
