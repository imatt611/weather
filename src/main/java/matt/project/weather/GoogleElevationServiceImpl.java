package matt.project.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static matt.project.weather.GoogleApiInfo.ENDPOINT_TEMPLATE__GET_ELEVATION;
import static matt.project.weather.GoogleApiInfo.PROP_REF__API_KEY_GOOGLE;
import static matt.project.weather.GoogleApiInfo.ROOT_URI;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__API_KEY;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__LATITUDE;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__LONGITUDE;

@Service
@Slf4j
public class GoogleElevationServiceImpl implements GoogleElevationService {

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_GOOGLE)
    private String apiKey;

    public GoogleElevationServiceImpl()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
    }

    public GoogleElevationServiceImpl(RestTemplate template)
    {
        restTemplate = template;
    }

    @Override
    public GoogleElevationData retrieveElevation(double latitude, double longitude)
    {
        double validLatitude = LatitudeLongitudeUtil.getValidatedLatitude(latitude);
        double validLongitude = LatitudeLongitudeUtil.getValidatedLongitude(longitude);

        Map<String, Object> variablesMap = new HashMap<>(2);
        variablesMap.put(TEMPLATE_VAR_NAME__LATITUDE, validLatitude);
        variablesMap.put(TEMPLATE_VAR_NAME__LONGITUDE, validLongitude);
        variablesMap.put(TEMPLATE_VAR_NAME__API_KEY, apiKey);

        log.trace(">>> GET Elevation for latitude/longitude: {}/{}", validLatitude, validLongitude);
        return restTemplate.getForObject(ENDPOINT_TEMPLATE__GET_ELEVATION, GoogleElevationData.class, variablesMap);
    }
}
