package matt.project.weather;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@NoArgsConstructor
class OpenWeatherService {

    private static final String ROOT_URI = "https://api.openweathermap.org/data/2.5";

    private final RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();

    @Value("${api.key.openWeather}")
    private String openWeatherApiKey;

    WeatherData getWeather(int zipCode)
    {
        log.trace(">>> GET for zipCode: {}", zipCode);
        return restTemplate.getForObject(
                "/weather?zip={zipCode}&appid={apiKey}",
                WeatherData.class,
                String.valueOf(zipCode),
                openWeatherApiKey);
    }
}
