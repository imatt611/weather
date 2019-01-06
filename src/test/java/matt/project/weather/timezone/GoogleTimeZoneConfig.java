package matt.project.weather.timezone;

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
public class GoogleTimeZoneConfig {

    public static final String TIMEZONE_NAME = "Mountain Daylight Time";


    @Bean
    @Primary
    static TimeZoneService googleTimeZoneService()
    {
        TimeZoneData timeZoneData = new GoogleTimeZoneData();
        timeZoneData.setTimeZoneName(TIMEZONE_NAME);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(timeZoneData);

        return new GoogleTimeZoneServiceImpl(mockRestTemplate);
    }
}
