package matt.project.weather.elevation;

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
public class GoogleElevationConfig {

    public static final Double ELEVATION = 1500.3784;

    @Bean
    @Primary
    static ElevationService googleElevationService()
    {
        ElevationData elevationData = new GoogleElevationData();
        elevationData.setElevation(ELEVATION);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(elevationData);

        return new GoogleElevationServiceImpl(mockRestTemplate);
    }
}
