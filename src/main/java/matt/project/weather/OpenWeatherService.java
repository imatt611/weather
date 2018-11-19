package matt.project.weather;

public interface OpenWeatherService {

    /**
     * @param zipCode the Zip Code for which to get weather information
     * @return weather information for the Zip Code according to <a href="https://openweathermap.org">OpenWeatherMap</a>
     * @throws IllegalArgumentException if the Zip Code is not exactly 5 Integer-parseable digits
     * @see <a href="https://openweathermap.org/current#zip">OpenWeatherMap Zip Code API</a>
     */
    WeatherData getWeather(String zipCode);
}
