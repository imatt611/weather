package matt.project.weather.google;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleTimeZoneConfig {
    //
    //    @Bean
    //    static TimeZoneService timeZoneService()
    //    {
    //        TimeZoneData timeZoneData = new GoogleTimeZoneData();
    //        timeZoneData.setTimeZoneName(TEST_TIMEZONE_NAME);
    //
    //        RestTemplate mockRestTemplate = mock(RestTemplate.class);
    //        when(mockRestTemplate.getForObject(anyString(), any(), anyMap())).thenReturn(timeZoneData);
    //
    //        return new GoogleTimeZoneService(mockRestTemplate);
    //    }
}
