package matt.project.weather;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@ToString
@NoArgsConstructor
public class GoogleElevationServiceImpl implements GoogleElevationService {

    private static final String ROOT_URI = "https://maps.googleapis.com/maps/api/elevation";

    private final RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();

    @Value(PROP_REF__API_KEY_GOOGLE_ELEVATION)
    private String apiKey;

    @Override
    public GoogleElevationData getElevation(double latitude, double longitude)
    {
        // TODO Input validation

        log.trace(">>> GET Elevation for latitude/longitude: {}/{}", latitude, longitude);
        return restTemplate.getForObject(
                "/json?locations={latitude},{longitude}&key={apiKey}",
                GoogleElevationData.class,
                latitude, // TODO Consider encoding here
                longitude,
                apiKey);
    }
}
