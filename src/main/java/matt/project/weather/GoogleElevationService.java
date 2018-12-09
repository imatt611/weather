package matt.project.weather;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@NoArgsConstructor
public class GoogleElevationService {
    private static final String ROOT_URI = "https://maps.googleapis.com/maps/api/elevation";

    private final RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();

    @Value("${api.key.google}")
    private String googleTimeZoneApiKey;

    GoogleElevationData getElevation(double latitude, double longitude)
    {
        // TODO Input validation

        log.trace(">>> GET for latitude/longitude: {}/{}", latitude, longitude);
        return restTemplate.getForObject(
                "/json?locations={latitude},{longitude}&key={apiKey}",
                GoogleElevationData.class,
                latitude, // TODO Consider encoding here
                longitude,
                googleTimeZoneApiKey);
    }
}
