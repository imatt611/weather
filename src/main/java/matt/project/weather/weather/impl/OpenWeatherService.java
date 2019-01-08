package matt.project.weather.weather.impl;

import lombok.extern.slf4j.Slf4j;
import matt.project.weather.weather.WeatherData;
import matt.project.weather.weather.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static matt.project.weather.util.ApiConstants.TEMPLATE_VAR_NAME__API_KEY;

/**
 * OpenWeather implementation, paired with {@link OpenWeatherData}.
 *
 * @see <a href="https://openweathermap.org/current#zip">OpenWeatherMap Zip Code API</a>
 */
@Service
@Slf4j
public class OpenWeatherService implements WeatherService {

    static final String ROOT_URI = "https://api.openweathermap.org/data/2.5";
    static final String GET_WEATHER_ENDPOINT_TEMPLATE = "/weather?zip={zipCode}&appid={apiKey}";
    static final String FAHRENHEIT = "FAHRENHEIT";

    private static final String TEMPLATE_VAR_NAME__ZIP_CODE = "zipCode";
    private static final String PROP_REF__API_KEY_OPEN_WEATHER = "${api.key.openWeather}";
    private static final BigDecimal FORMULA_PART__KELVIN_CELSIUS__DIFFERENCE = BigDecimal.valueOf(273.15);
    private static final BigDecimal FORMULA_PART__FAHRENHEIT_CELSIUS__DIFFERENCE = BigDecimal.valueOf(32.0);
    private static final BigDecimal FORMULA_PART__CELSIUS_FAHRENHEIT__FACTOR = BigDecimal.valueOf(9.0 / 5.0);

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_OPEN_WEATHER)
    private String apiKey;

    OpenWeatherService()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
    }

    public OpenWeatherService(RestTemplate template)
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

    @Override
    public Double getLatitude(WeatherData weatherData)
    {
        return weatherData.getLatitude();
    }

    @Override
    public Double getLongitude(WeatherData weatherData)
    {
        return weatherData.getLongitude();
    }

    @Override
    public String getCityName(WeatherData weatherData)
    {
        return weatherData.getName();
    }

    @Override
    public Double getTemperature(WeatherData weatherData)
    {
        return weatherData.getTemperature();
    }

    @Override
    public Double getTemperatureInFahrenheit(WeatherData weatherData)
    {
        BigDecimal kelvinTemperature = BigDecimal.valueOf(weatherData.getTemperature());
        // Formula: 9/5 (K - 273.15) + 32
        return (FORMULA_PART__CELSIUS_FAHRENHEIT__FACTOR
            .multiply(kelvinTemperature.subtract(FORMULA_PART__KELVIN_CELSIUS__DIFFERENCE))
            .add(FORMULA_PART__FAHRENHEIT_CELSIUS__DIFFERENCE))
            .doubleValue();

    }
}
