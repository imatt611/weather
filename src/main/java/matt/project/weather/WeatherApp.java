package matt.project.weather;

import lombok.RequiredArgsConstructor;
import matt.project.weather.elevation.ElevationData;
import matt.project.weather.elevation.ElevationService;
import matt.project.weather.timezone.TimeZoneData;
import matt.project.weather.timezone.TimeZoneService;
import matt.project.weather.weather.WeatherData;
import matt.project.weather.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;


@SpringBootApplication
@PropertySource("classpath:keys.properties")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WeatherApp implements ApplicationRunner {

    private final WeatherService weatherService;
    private final TimeZoneService timeZoneService;
    private final ElevationService elevationService;

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    private static String buildWeatherDescription(
        String cityName, Double fahrenheitTemperature, String timeZoneName, Double elevation)
    {
        return String.format(
            "At the location %s, the temperature is %f Fahrenheit, the timezone is %s, and the elevation is %f.",
            cityName, fahrenheitTemperature, timeZoneName, elevation);
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

            CompletableFuture<WeatherData> weatherDataFuture = CompletableFuture
                .completedFuture(zipCodeArg)
                .thenApplyAsync(weatherService::retrieveWeather);

            CompletableFuture<String> weatherDescriptionFuture = buildWeatherDescriptionFuture(
                weatherDataFuture,
                weatherDataFuture.thenApplyAsync(weatherService::getLatitude),
                weatherDataFuture.thenApplyAsync(weatherService::getLongitude));

            System.out.println(weatherDescriptionFuture.get());
        } else {
            System.out.println("Provide a 5-digit ZIP Code as an argument to receive information about the area.");
        }
    }

    private CompletableFuture<ElevationData> buildElevationFuture(
        CompletableFuture<Double> latitudeFuture, CompletionStage<Double> longitudeStage)
    {
        return latitudeFuture.thenCombineAsync(longitudeStage, elevationService::retrieveElevation);
    }

    private CompletableFuture<TimeZoneData> buildTimeZoneFuture(
        CompletableFuture<Double> latitudeFuture, CompletionStage<Double> longitudeStage)
    {
        return latitudeFuture.thenCombineAsync(longitudeStage, timeZoneService::retrieveTimeZone);
    }

    private CompletableFuture<String> buildWeatherDescriptionFuture(
        CompletableFuture<WeatherData> weatherDataFuture, CompletableFuture<Double> latitudeFuture,
        CompletionStage<Double> longitudeStage)
    {
        CompletableFuture<TimeZoneData> timeZoneFuture = buildTimeZoneFuture(latitudeFuture, longitudeStage);
        CompletableFuture<ElevationData> elevationFuture = buildElevationFuture(latitudeFuture, longitudeStage);

        return weatherDataFuture
            .thenComposeAsync(weatherData -> timeZoneFuture
                .thenCombineAsync(elevationFuture, (timeZoneData, elevationData) -> buildWeatherDescription(
                    weatherService.getCityName(weatherData), weatherService.getTemperatureInFahrenheit(weatherData),
                    timeZoneService.getTimeZoneName(timeZoneData), elevationService.getElevation(elevationData))));
    }
}
