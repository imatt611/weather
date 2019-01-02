package matt.project.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
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
    public GoogleElevationData retrieveElevation(double latitude, double longitude)
    {
        double validLatitude = LatitudeLongitude.getValidatedLatitude(latitude);
        double validLongitude = LatitudeLongitude.getValidatedLongitude(longitude);

        Map<String, Object> variablesMap = new HashMap<>(2);
        variablesMap.put("latitude", validLatitude);
        variablesMap.put("longitude", validLongitude);
        variablesMap.put("apiKey", apiKey);

        log.trace(">>> GET Elevation for latitude/longitude: {}/{}", validLatitude, validLongitude);
        return restTemplate.getForObject(ENDPOINT_TEMPLATE__GET_ELEVATION, GoogleElevationData.class, variablesMap);
    }
}
