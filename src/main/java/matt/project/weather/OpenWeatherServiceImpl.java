package matt.project.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
class OpenWeatherServiceImpl implements OpenWeatherService {

    private final OpenWeatherDataProvider<String, String, String, OpenWeatherData> openWeatherDataProvider;

    @Value(PROP_REF__API_KEY_OPEN_WEATHER)
    private String apiKey;

    OpenWeatherServiceImpl()
    {
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(WEATHER_ROOT_URI).build();
        openWeatherDataProvider = (endpointTemplate, zipCode, key) -> restTemplate
            .getForObject(endpointTemplate, OpenWeatherData.class, zipCode, key);
    }

    OpenWeatherServiceImpl(
        OpenWeatherDataProvider<String, String, String, OpenWeatherData> weatherDataProvider)
    {
        openWeatherDataProvider = weatherDataProvider;
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

        log.trace(">>> GET Weather for zipCode: {}", validZipCode);
        // TODO Remove 2nd arg from interface; add to Javadoc somewhere
        return openWeatherDataProvider.apply(GET_WEATHER_QUERY_TEMPLATE, validZipCode, apiKey);
    }
}
