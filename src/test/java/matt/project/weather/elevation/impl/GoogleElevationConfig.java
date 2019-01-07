package matt.project.weather.elevation.impl;

import matt.project.weather.elevation.ElevationData;
import matt.project.weather.elevation.ElevationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static matt.project.weather.WeatherAppTest_Spring.TEST_ELEVATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class GoogleElevationConfig {

    @Bean
    static ElevationService elevationService()
    {
        ElevationData elevationData = new GoogleElevationData();
        elevationData.setElevation(TEST_ELEVATION);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(elevationData);

        return new GoogleElevationService(mockRestTemplate);
    }
}
