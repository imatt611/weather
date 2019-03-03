package matt.project.weather.openweather;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenWeatherConfig {
    //
    //    @Bean
    //    static WeatherService weatherService()
    //    {
    //        WeatherData weatherData = new OpenWeatherData();
    //        weatherData.setName(TEST_CITY_NAME);
    //        weatherData.setTemperature(TEST_TEMPERATURE_KELVIN);
    //        weatherData.setLatitude(1.0);
    //        weatherData.setLongitude(1.0);
    //
    //        RestTemplate mockRestTemplate = mock(RestTemplate.class);
    //        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(weatherData);
    //
    //        return new OpenWeatherService(mockRestTemplate);
    //    }
}
