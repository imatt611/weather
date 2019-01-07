package matt.project.weather.timezone.impl;

import matt.project.weather.timezone.TimeZoneData;
import matt.project.weather.timezone.TimeZoneService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static matt.project.weather.WeatherAppTest_Spring.TEST_TIMEZONE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class GoogleTimeZoneConfig {

    @Bean
    static TimeZoneService timeZoneService()
    {
        TimeZoneData timeZoneData = new GoogleTimeZoneData();
        timeZoneData.setTimeZoneName(TEST_TIMEZONE_NAME);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(timeZoneData);

        return new GoogleTimeZoneService(mockRestTemplate);
    }
}
