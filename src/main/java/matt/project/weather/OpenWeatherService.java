package matt.project.weather;

public interface OpenWeatherService {

    String ROOT_URI = "https://api.openweathermap.org/data/2.5";
    String GET_WEATHER_ENDPOINT_TEMPLATE = "/weather?zip={zipCode}&appid={apiKey}"; // TODO Constant-ize keys/vars
    String TEMPLATE_VAR_NAME__ZIP_CODE = "zipCode";
    String TEMPLATE_VAR_NAME__API_KEY = GoogleApiInfo.TEMPLATE_VAR_NAME__API_KEY;
    String PROP_REF__API_KEY_OPEN_WEATHER = "${api.key.openWeather}";

    /**
     * @param zipCode the Zip Code for which to get weather information
     * @return weather information for the Zip Code according to <a href="https://openweathermap.org">OpenWeatherMap</a>
     * @throws IllegalArgumentException if the Zip Code is not exactly 5 Integer-parseable digits
     * @see <a href="https://openweathermap.org/current#zip">OpenWeatherMap Zip Code API</a>
     */
    OpenWeatherData retrieveWeather(String zipCode);
}
