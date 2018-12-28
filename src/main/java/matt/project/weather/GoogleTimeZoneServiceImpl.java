package matt.project.weather;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Slf4j
@ToString
public class GoogleTimeZoneServiceImpl implements GoogleTimeZoneService {

    private static final String ROOT_URI = GoogleTimeZoneService.ROOT_URI;

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_GOOGLE_TIME_ZONE)
    private String apiKey;

    public GoogleTimeZoneServiceImpl()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
    }

    public GoogleTimeZoneServiceImpl(RestTemplate template)
    {
        restTemplate = template;
    }

    @Override
    public GoogleTimeZoneData getTimeZone(double latitude, double longitude)
    {
        // TODO Input validation

        log.trace(">>> GET Time Zone for latitude/longitude: {}/{}", latitude, longitude);
        return restTemplate.getForObject(
                GET_TIMEZONE_ENDPOINT_TEMPLATE,
                GoogleTimeZoneData.class,
                latitude, // TODO Consider encoding here
                longitude,
                Instant.now().getEpochSecond(),
                apiKey);
    }
}
