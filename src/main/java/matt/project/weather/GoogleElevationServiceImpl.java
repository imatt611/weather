package matt.project.weather;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@ToString
public class GoogleElevationServiceImpl implements GoogleElevationService {

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_GOOGLE_ELEVATION)
    private String apiKey;

    public GoogleElevationServiceImpl()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI__ELEVATION).build();
    }

    public GoogleElevationServiceImpl(RestTemplate template)
    {
        restTemplate = template;
    }

    @Override
    public GoogleElevationData getElevation(double latitude, double longitude)
    {
        // TODO Input validation

        log.trace(">>> GET Elevation for latitude/longitude: {}/{}", latitude, longitude);
        return restTemplate.getForObject(
                ENDPOINT_TEMPLATE__GET_ELEVATION,
                GoogleElevationData.class,
                latitude, // TODO Consider encoding here
                longitude,
                apiKey);
    }
}
