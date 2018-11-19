package matt.project.weather;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Slf4j
@NoArgsConstructor
public class GoogleTimeZoneService {

    private static final String ROOT_URI = "https://maps.googleapis.com/maps/api/timezone";

    private final RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();

    @Value("${api.key.googleTimeZone}")
    private String googleTimeZoneApiKey;

    GoogleTimeZoneData getTimeZone(double latitude, double longitude)
    {
        // TODO Input validation

        log.trace(">>> GET for latitude/longitude: {}/{}", latitude, longitude);
        return restTemplate.getForObject(
                "/json?location={latitude},{longitude}&timestamp={timestamp}&key={apiKey}",
                GoogleTimeZoneData.class,
                latitude, // TODO Consider encoding here
                longitude,
                Instant.now().getEpochSecond(),
                googleTimeZoneApiKey);
    }
}
