package matt.project.weather.google;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import matt.project.weather.timezone.TimeZoneData;
import matt.project.weather.timezone.TimeZoneService;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static matt.project.weather.google.GoogleApiConstants.ENDPOINT_TEMPLATE__GET_TIMEZONE;
import static matt.project.weather.google.GoogleApiConstants.ROOT_URI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class GoogleTimeZoneTest_Unit {
    private static final String TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON = "/testResponse_googleTimeZone.json";
    private static final String TEST_TIME_ZONE_NAME = "Nowhere Time";

    @Test
    public void evaluatesEquality_whenTrue()
    {
        //given
        TimeZoneData timeZoneData1 = new GoogleTimeZoneData();
        timeZoneData1.setTimeZoneName(TEST_TIME_ZONE_NAME);
        TimeZoneData timeZoneData2 = new GoogleTimeZoneData();
        timeZoneData2.setTimeZoneName(TEST_TIME_ZONE_NAME);

        //expect
        assertThat(timeZoneData1, equalTo(timeZoneData2));
    }

    @Test
    public void evaluatesEquality_whenFalse()
    {
        //given
        TimeZoneData timeZoneData1 = new GoogleTimeZoneData();
        timeZoneData1.setTimeZoneName(TEST_TIME_ZONE_NAME);
        TimeZoneData timeZoneData2 = new GoogleTimeZoneData();
        timeZoneData2.setTimeZoneName("Somewhere Time");

        //expect
        assertThat(timeZoneData2, not(equalTo(timeZoneData1)));
    }

    @Test
    public void deserializesResults() throws Exception
    {
        // expect
        TimeZoneData expectedTimeZoneData = new GoogleTimeZoneData();
        expectedTimeZoneData.setTimeZoneName("Eastern Daylight Time");

        // when
        ObjectMapper mapper = new ObjectMapper();
        URL src = getClass().getResource(TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON);
        TimeZoneData timeZoneData = mapper.readValue(src, GoogleTimeZoneData.class);

        assertThat(timeZoneData, equalTo(expectedTimeZoneData));
    }

    @Test
    public void usesKnownGoogleTimeZoneApiContractAndReturnsTimeZoneData() throws Exception
    {
        // given
        RestTemplate restTemplate = new RestTemplateBuilder().rootUri(ROOT_URI).build();
        TimeZoneService localTestTimeZoneService = new GoogleTimeZoneService(restTemplate);

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String testDataJsonString = String.join("", Files.readAllLines(
            Paths.get(getClass().getResource(TEST_RESPONSE_GOOGLE_TIME_ZONE_JSON).getPath())));

        Map<String, Double> testLatLongTuple = new HashMap<>(2);
        testLatLongTuple.put("lat", 28.59);
        testLatLongTuple.put("lon", 77.0);

        // expect
        mockServer
            .expect(requestTo(
                stringContainsInOrder(
                    Arrays.asList(ROOT_URI,
                                  ENDPOINT_TEMPLATE__GET_TIMEZONE
                                      .substring(0, ENDPOINT_TEMPLATE__GET_TIMEZONE.indexOf('{')),
                                  testLatLongTuple.get("lat").toString(),
                                  testLatLongTuple.get("lon").toString(),
                                  String.valueOf(Instant.now().getEpochSecond())
                                        .substring(0, 6))))) // Close enough for unit test
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(testDataJsonString, MediaType.APPLICATION_JSON));

        // when
        TimeZoneData mockData = localTestTimeZoneService
            .retrieveTimeZone(testLatLongTuple.get("lat"), testLatLongTuple.get("lon"));

        mockServer.verify();
        assertThat(mockData, instanceOf(TimeZoneData.class));
    }
}
