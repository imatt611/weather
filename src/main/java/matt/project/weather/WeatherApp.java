package matt.project.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


// TODO Create *Data Interfaces, place them and service interfaces on top level with WeatherApp, then establish
//  feature-based packages below there. WeatherApp shouldn't have to see anything in the packages beneath, so fewer
//  things would need to be made public. *Data interfaces could drop the "Google" and "OpenWeather" knowledge.


@SpringBootApplication
@PropertySource("classpath:keys.properties")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WeatherApp implements ApplicationRunner {

    private final OpenWeatherService weatherService;
    private final TimeZoneService timeZoneService;
    private final ElevationService elevationService;

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    private static String buildWeatherDescription(
        String cityName, Double temperature, String timeZoneName, Double elevation)
    {
        return String.format(
            "At the location %s, the temperature is %f, the timezone is %s, and the elevation is %f.",
            cityName, temperature, timeZoneName, elevation);
    }

    @Override
    public void run(ApplicationArguments args) throws ExecutionException, InterruptedException
    {
        String[] sourceArgs = args.getSourceArgs();
        if (1 < sourceArgs.length) {
            System.out.println(
                String.format("Extra arguments provided. The first, %s, will be used as the ZIP Code argument.",
                              sourceArgs[0]));
        }

        if (0 < sourceArgs.length) {
            String zipCodeArg = sourceArgs[0];
            OpenWeatherData weatherData = weatherService.retrieveWeather(zipCodeArg);
            Double latitude = weatherData.getLatitude();
            Double longitude = weatherData.getLongitude();

            CompletableFuture<TimeZoneData> timeZoneFuture = CompletableFuture.supplyAsync(
                () -> timeZoneService.retrieveTimeZone(latitude, longitude));
            CompletableFuture<ElevationData> elevationFuture = CompletableFuture.supplyAsync(
                () -> elevationService.retrieveElevation(latitude, longitude));

            CompletableFuture<String> weatherDescriptionFuture = timeZoneFuture
                .thenCombineAsync(elevationFuture, (timeZoneData, elevationData) ->
                    buildWeatherDescription(
                        weatherData.getName(),
                        weatherData.getTemperature(),
                        timeZoneData.getTimeZoneName(),
                        elevationData.getElevation()));

            System.out.println(weatherDescriptionFuture.get());
        } else {
            System.out.println("Provide a 5-digit ZIP Code as an argument to receive information about the area.");
        }
    }
}
