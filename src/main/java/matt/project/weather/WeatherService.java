package matt.project.weather;

public interface WeatherService {

    /**
     * @param zipCode the Zip Code for which to get weather information
     * @return weather information for the Zip Code
     * @throws IllegalArgumentException if the Zip Code is not exactly 5 Integer-parseable digits
     */
    WeatherData retrieveWeather(String zipCode);
}
