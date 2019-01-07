package matt.project.weather.weather;

public interface WeatherService {

    /**
     * @param zipCode the Zip Code for which to get weather information
     * @return weather information for the Zip Code
     * @throws IllegalArgumentException if the Zip Code is not exactly 5 Integer-parseable digits
     */
    WeatherData retrieveWeather(String zipCode);

    /**
     * @param weatherData the weather data
     * @return the latitude the weather data refers to
     */
    Double getLatitude(WeatherData weatherData);

    /**
     * @param weatherData the weather data
     * @return the longitude the weather data refers to
     */
    Double getLongitude(WeatherData weatherData);

    /**
     * @param weatherData the weather data
     * @return the name of the city the weather data is about
     */
    String getCityName(WeatherData weatherData);

    /**
     * @param weatherData the weather data
     * @return the temperature within the weather data, in Fahrenheit, to two decimal places
     */
    Double getTemperatureInFahrenheit(WeatherData weatherData);
}
