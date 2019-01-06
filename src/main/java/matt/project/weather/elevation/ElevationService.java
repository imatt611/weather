package matt.project.weather.elevation;

public interface ElevationService {

    /**
     * @param latitude  a latitude {@code <=90} and {@code >=-90}
     * @param longitude a longitude {@code <=180} and {@code >=-180}
     * @return elevation information at the location represented by the {@code (latitude, longitude)} tuple
     */
    ElevationData retrieveElevation(double latitude, double longitude);

    /**
     * @param elevationData the elevation information
     * @return the elevation, in meters
     */
    Double getElevation(ElevationData elevationData);
}
