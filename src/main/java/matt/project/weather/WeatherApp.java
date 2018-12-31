package matt.project.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@PropertySource("classpath:keys.properties")
public class WeatherApp implements ApplicationRunner {

    // TODO Consider refactor to constructor injection (or, in order to avoid mocking known implementation in these Impls' tests, consider providers (functions) to be injected here (or into a [new] class that performs the work and isn't "the app" itself)
    @Autowired private OpenWeatherService weatherService;
    @Autowired private GoogleTimeZoneService timeZoneService;
    @Autowired private GoogleElevationService elevationService;

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    private static String buildWeatherDescription(String cityName, Double temperature, String timeZoneName, Double elevation)
    {
        return String.format(
            "At the location %s, the temperature is %f, the timezone is %s, and the elevation is %f.",
            cityName, temperature, timeZoneName, elevation);
    }

    @Override
    public void run(ApplicationArguments args) throws ExecutionException, InterruptedException
    {
        // TODO Clearer args handling
        if (0 < args.getSourceArgs().length) {
            String zipCodeArg = args.getSourceArgs()[0];
            OpenWeatherData weatherData = weatherService.getWeather(zipCodeArg);
            Double latitude = weatherData.getLatitude();
            Double longitude = weatherData.getLongitude();

            CompletableFuture<GoogleTimeZoneData> timeZoneFuture = CompletableFuture.supplyAsync(
                () -> timeZoneService.getTimeZone(latitude, longitude));
            CompletableFuture<GoogleElevationData> elevationFuture = CompletableFuture.supplyAsync(
                () -> elevationService.getElevation(latitude, longitude));

            CompletableFuture<String> weatherDescriptionFuture = timeZoneFuture
                .thenCombineAsync(elevationFuture, (timeZoneData, elevationData) ->
                    buildWeatherDescription(
                        weatherData.getName(),
                        weatherData.getTemperature(),
                        timeZoneData.getTimeZoneName(),
                        elevationData.getElevation()));

            System.out.println(weatherDescriptionFuture.get());
        }
    }
}
