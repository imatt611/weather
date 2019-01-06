package matt.project.weather.weather;

import lombok.extern.slf4j.Slf4j;
import matt.project.weather.GoogleApiInfo;
import matt.project.weather.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * OpenWeather implementation, paired with {@link OpenWeatherData}.
 *
 * @see <a href="https://openweathermap.org/current#zip">OpenWeatherMap Zip Code API</a>
 */
@Service
@Slf4j
class OpenWeatherServiceImpl implements WeatherService {

    static final String ROOT_URI = "https://api.openweathermap.org/data/2.5";
    static final String GET_WEATHER_ENDPOINT_TEMPLATE = "/weather?zip={zipCode}&appid={apiKey}"; // TODO Constant-ize keys/vars
    private static final String TEMPLATE_VAR_NAME__ZIP_CODE = "zipCode";
    private static final String TEMPLATE_VAR_NAME__API_KEY = GoogleApiInfo.TEMPLATE_VAR_NAME__API_KEY;
    private final RestTemplate restTemplate;

    private static final String PROP_REF__API_KEY_OPEN_WEATHER = "${api.key.openWeather}";
    @Value(PROP_REF__API_KEY_OPEN_WEATHER)
    private String apiKey;

    OpenWeatherServiceImpl()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
    }

    OpenWeatherServiceImpl(RestTemplate template)
    {
        restTemplate = template;
    }

    /**
     * @param zipCode the Zip Code to validate
     * @return the valid Zip Code
     * @throws IllegalArgumentException if the Zip Code is not exactly 5 Integer-parseable digits
     */
    private static String getValidatedZipCode(String zipCode)
    {
        // Parse integer to guarantee digits
        try {
            Integer.parseInt(zipCode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Zip Code must only contain digits.", e);
        }

        // Validate length
        // Note: Integer#parseInt allows for a `-` sign at the beginning. Currently, a negative number will fail here
        if (5 != zipCode.length())
            throw new IllegalArgumentException("Zip Code must be exactly 5 digits. Zip+4 is not supported.");

        return zipCode;
    }

    @Override
    public OpenWeatherData retrieveWeather(String zipCode)
    {
        String validZipCode = getValidatedZipCode(zipCode);

        Map<String, Object> variablesMap = new HashMap<>(2);
        variablesMap.put(TEMPLATE_VAR_NAME__ZIP_CODE, validZipCode);
        variablesMap.put(TEMPLATE_VAR_NAME__API_KEY, apiKey);

        log.trace(">>> GET Weather for zipCode: {}", validZipCode);
        return restTemplate.getForObject(GET_WEATHER_ENDPOINT_TEMPLATE, OpenWeatherData.class, variablesMap);
    }
}
