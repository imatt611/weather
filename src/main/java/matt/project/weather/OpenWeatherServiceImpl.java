package matt.project.weather;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@ToString
@NoArgsConstructor
class OpenWeatherServiceImpl implements OpenWeatherService {

    private static final String ROOT_URI = "https://api.openweathermap.org/data/2.5";

    private final RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();

    @Value("${api.key.openWeather}")
    private String openWeatherApiKey;

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
    public WeatherData getWeather(String zipCode)
    {
        String validZipCode = getValidatedZipCode(zipCode);

        log.trace(">>> GET for zipCode: {}", validZipCode);
        return restTemplate.getForObject(
                "/weather?zip={zipCode}&appid={apiKey}",
                WeatherData.class,
                validZipCode, // TODO Consider encoding here
                openWeatherApiKey);
    }
}
