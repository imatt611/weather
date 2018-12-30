package matt.project.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static matt.project.weather.LatitudeLongitude.getValidatedLatitude;
import static matt.project.weather.LatitudeLongitude.getValidatedLongitude;

@Service
@Slf4j
public class GoogleTimeZoneServiceImpl implements GoogleTimeZoneService {

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_GOOGLE_TIMEZONE)
    private String apiKey;

    public GoogleTimeZoneServiceImpl()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI__TIMEZONE).build();
    }

    public GoogleTimeZoneServiceImpl(RestTemplate template)
    {
        restTemplate = template;
    }

    @Override
    public GoogleTimeZoneData getTimeZone(double latitude, double longitude)
    {
        double validLatitude = getValidatedLatitude(latitude);
        double validLongitude = getValidatedLongitude(longitude);

        log.trace(">>> GET Time Zone for latitude/longitude: {}/{}", validLatitude, validLongitude);
        return restTemplate.getForObject(
            ENDPOINT_TEMPLATE__GET_TIMEZONE,
            GoogleTimeZoneData.class,
            latitude,
            longitude,
            Instant.now().getEpochSecond(),
            apiKey);
    }
}
