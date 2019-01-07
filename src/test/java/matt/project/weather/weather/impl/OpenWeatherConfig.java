package matt.project.weather.weather.impl;

import matt.project.weather.weather.WeatherData;
import matt.project.weather.weather.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static matt.project.weather.WeatherAppTest_Spring.TEST_CITY_NAME;
import static matt.project.weather.WeatherAppTest_Spring.TEST_TEMPERATURE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class OpenWeatherConfig {

    @Bean
    static WeatherService weatherService()
    {
        WeatherData weatherData = new OpenWeatherData();
        weatherData.setName(TEST_CITY_NAME);
        weatherData.setTemperature(TEST_TEMPERATURE);
        weatherData.setLatitude(1.0);
        weatherData.setLongitude(1.0);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(weatherData);

        return new OpenWeatherService(mockRestTemplate);
    }
}
