package matt.project.weather.google;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import matt.project.weather.elevation.ElevationData;
import matt.project.weather.elevation.ElevationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static matt.project.weather.google.GoogleApiConstants.ENDPOINT_TEMPLATE__GET_ELEVATION;
import static matt.project.weather.google.GoogleApiConstants.PROP_REF__API_KEY_GOOGLE;
import static matt.project.weather.google.GoogleApiConstants.ROOT_URI;
import static matt.project.weather.google.GoogleApiConstants.TEMPLATE_VAR_NAME__LATITUDE;
import static matt.project.weather.google.GoogleApiConstants.TEMPLATE_VAR_NAME__LONGITUDE;
import static matt.project.weather.google.LatitudeLongitude.getValidatedLatitude;
import static matt.project.weather.google.LatitudeLongitude.getValidatedLongitude;
import static matt.project.weather.util.ApiConstants.TEMPLATE_VAR_NAME__API_KEY;

@Service
@Slf4j
public class GoogleElevationService implements ElevationService {

    private static final BigDecimal FORMULA_PART__METER_FEET__FACTOR = BigDecimal.valueOf(3.28084);

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_GOOGLE)
    private String apiKey;

    public GoogleElevationService()
    {
        restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
    }

    public GoogleElevationService(RestTemplate template)
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

    @Override
    public Double getElevationInFeet(ElevationData elevationData)
    {
        BigDecimal elevationMeters = BigDecimal.valueOf(elevationData.getElevation());
        return elevationMeters.multiply(FORMULA_PART__METER_FEET__FACTOR)
                              .setScale(2, RoundingMode.HALF_UP)
                              .doubleValue();
    }
}
