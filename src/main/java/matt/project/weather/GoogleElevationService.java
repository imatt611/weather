package matt.project.weather;

public interface GoogleElevationService {

    /**
     * @param latitude  a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return Elevation information at the location represented by the {@code (latitude, longitude)} tuple
     */
    ElevationData retrieveElevation(double latitude, double longitude);
}
