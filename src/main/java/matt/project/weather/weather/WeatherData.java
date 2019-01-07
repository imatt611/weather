package matt.project.weather.weather;

public interface WeatherData {

    /**
     * @return the latitude
     */
    Double getLatitude();

    /**
     * @param latitude the latitude
     */
    void setLatitude(Double latitude);

    /**
     * @return the longitude
     */
    Double getLongitude();

    /**
     * @param longitude the longitude
     */
    void setLongitude(Double longitude);

    /**
     * @return the temperature in Kelvin
     */
    Double getTemperature();

    /**
     * @param temperature the temperature
     */
    void setTemperature(Double temperature);

    /**
     * @return the city name
     */
    String getName();

    /**
     * @param name the city name
     */
    void setName(String name);
}
