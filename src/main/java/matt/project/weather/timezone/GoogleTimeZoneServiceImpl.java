package matt.project.weather.timezone;

import lombok.extern.slf4j.Slf4j;
import matt.project.weather.TimeZoneData;
import matt.project.weather.TimeZoneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static matt.project.weather.GoogleApiInfo.ENDPOINT_TEMPLATE__GET_TIMEZONE;
import static matt.project.weather.GoogleApiInfo.PROP_REF__API_KEY_GOOGLE;
import static matt.project.weather.GoogleApiInfo.ROOT_URI;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__API_KEY;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__LATITUDE;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__LONGITUDE;
import static matt.project.weather.GoogleApiInfo.TEMPLATE_VAR_NAME__TIMESTAMP;
import static matt.project.weather.LatitudeLongitudeUtil.getValidatedLatitude;
import static matt.project.weather.LatitudeLongitudeUtil.getValidatedLongitude;

@Service
@Slf4j
public class GoogleTimeZoneServiceImpl implements TimeZoneService {

    private final RestTemplate restTemplate;

    @Value(PROP_REF__API_KEY_GOOGLE)
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
    public TimeZoneData retrieveTimeZone(double latitude, double longitude)
    {
        double validLatitude = getValidatedLatitude(latitude);
        double validLongitude = getValidatedLongitude(longitude);

        Map<String, Object> variablesMap = new HashMap<>(4);
        variablesMap.put(TEMPLATE_VAR_NAME__LATITUDE, validLatitude);
        variablesMap.put(TEMPLATE_VAR_NAME__LONGITUDE, validLongitude);
        variablesMap.put(TEMPLATE_VAR_NAME__TIMESTAMP, Instant.now().getEpochSecond());
        variablesMap.put(TEMPLATE_VAR_NAME__API_KEY, apiKey);

        log.trace(">>> GET Time Zone for latitude/longitude: {}/{}", validLatitude, validLongitude);
        return restTemplate.getForObject(ENDPOINT_TEMPLATE__GET_TIMEZONE, GoogleTimeZoneData.class, variablesMap);
    }
}
