package matt.project.weather.weather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class OpenWeatherConfig {

    public static final String CITY_NAME = "Boulder";
    public static final double TEMP = 75.2;

    @Bean
    @Primary
    static WeatherService openWeatherService()
    {
        WeatherData weatherData = new OpenWeatherData();
        weatherData.setName(CITY_NAME);
        weatherData.setTemperature(TEMP);
        weatherData.setLatitude(1.0);
        weatherData.setLongitude(1.0);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(weatherData);

        return new OpenWeatherServiceImpl(mockRestTemplate);
    }
}
