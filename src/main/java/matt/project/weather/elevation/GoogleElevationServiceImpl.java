package matt.project.weather.elevation;

import lombok.extern.slf4j.Slf4j;
import matt.project.weather.ElevationData;
import matt.project.weather.ElevationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static matt.project.weather.util.ApiConstants.TEMPLATE_VAR_NAME__API_KEY;
import static matt.project.weather.util.GoogleApiConstants.ENDPOINT_TEMPLATE__GET_ELEVATION;
import static matt.project.weather.util.GoogleApiConstants.PROP_REF__API_KEY_GOOGLE;
import static matt.project.weather.util.GoogleApiConstants.ROOT_URI;
import static matt.project.weather.util.GoogleApiConstants.TEMPLATE_VAR_NAME__LATITUDE;
import static matt.project.weather.util.GoogleApiConstants.TEMPLATE_VAR_NAME__LONGITUDE;
import static matt.project.weather.util.LatitudeLongitude.getValidatedLatitude;
import static matt.project.weather.util.LatitudeLongitude.getValidatedLongitude;

@Service
@Slf4j
public class GoogleElevationServiceImpl implements ElevationService {

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
    public ElevationData retrieveElevation(double latitude, double longitude)
    {
        double validLatitude = getValidatedLatitude(latitude);
        double validLongitude = getValidatedLongitude(longitude);

        Map<String, Object> variablesMap = new HashMap<>(2);
        variablesMap.put(TEMPLATE_VAR_NAME__LATITUDE, validLatitude);
        variablesMap.put(TEMPLATE_VAR_NAME__LONGITUDE, validLongitude);
        variablesMap.put(TEMPLATE_VAR_NAME__API_KEY, apiKey);

        log.trace(">>> GET Elevation for latitude/longitude: {}/{}", validLatitude, validLongitude);
        return restTemplate.getForObject(ENDPOINT_TEMPLATE__GET_ELEVATION, GoogleElevationData.class, variablesMap);
    }
}
