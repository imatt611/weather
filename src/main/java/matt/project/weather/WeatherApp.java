package matt.project.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@PropertySource("classpath:keys.properties")
public class WeatherApp implements ApplicationRunner {

    // TODO Consider refactor to constructor injection
    @Autowired private OpenWeatherService weatherService;
    @Autowired private GoogleTimeZoneService timeZoneService;
    @Autowired private GoogleElevationService elevationService;

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    private static void outputWeatherDescription(String cityName, Double temperature, String timeZoneName, Double elevation)
    {
        String weatherDescription = String.format(
            "At the location %s, the temperature is %f, the timezone is %s, and the elevation is %f.",
            cityName, temperature, timeZoneName, elevation);

        System.out.println(weatherDescription);
    }

    @Override
    public void run(ApplicationArguments args)
    {
        // TODO Clearer args handling
        if (0 < args.getSourceArgs().length) {
            // Starter, if necessary
            OpenWeatherData weatherData = weatherService.getWeather(args.getSourceArgs()[0]);
            Double latitude = weatherData.getLatitude();
            Double longitude = weatherData.getLongitude();

            CompletableFuture<GoogleTimeZoneData> timeZoneFuture = CompletableFuture.supplyAsync(
                () -> timeZoneService.getTimeZone(latitude, longitude));
            CompletableFuture<GoogleElevationData> elevationFuture = CompletableFuture.supplyAsync(
                () -> elevationService.getElevation(latitude, longitude));

            timeZoneFuture.thenAcceptBoth(elevationFuture, (timeZoneData, elevationData) ->
                outputWeatherDescription(weatherData.getName(),
                                         weatherData.getTemperature(),
                                         timeZoneData.getTimeZoneName(),
                                         elevationData.getElevation()));


            //            CompletableFuture.supplyAsync(() -> weatherService.getWeather(args.getSourceArgs()[0]))
            //                .thenApplyAsync(weatherData -> {
            //                    Double latitude = weatherData.getLatitude();
            //                    Double longitude = weatherData.getLongitude();
            //
            //                    CompletableFuture.supplyAsync(() -> timeZoneService.getTimeZone(latitude, longitude));
            //                    CompletableFuture.supplyAsync(() -> elevationService.getElevation(latitude, longitude));
            //                });


            // Idea:
            //            String original = "Message";
            //            CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(
            //                s -> delayedUpperCase(s))
            //                .thenCompose(upper -> CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s))
            //                    .thenApply(s -> upper + s));
            //            assertEquals("MESSAGEmessage", cf.join());

            // Idea impl (uses starter):
            // Works only in debug mode? Something async ain't right
            //            CompletableFuture.supplyAsync(() -> timeZoneService.getTimeZone(latitude, longitude))
            //                .thenCombineAsync(
            //                    CompletableFuture.supplyAsync(() -> elevationService.getElevation(latitude, longitude)),
            //                    (timeZoneData, elevationData) -> {
            //                        outputWeatherDescription(weatherData.getName(), weatherData.getTemperature(),
            //                                                 timeZoneData.getTimeZoneName(), elevationData.getElevation());
            //                        return null;
            //                    });
            //                .thenComposeAsync(openWeatherData -> )
            //                .thenApplyAsync(openWeatherData -> timeZoneService.getTimeZone(openWeatherData.getLatitude(), openWeatherData.getLongitude()))
            //                .thenAcceptBothAsync() // Description

            // Idea:
            //            String original = "Message";
            //            CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(
            //                s -> delayedUpperCase(s))
            //                .thenCompose(upper -> CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s))
            //                    .thenApply(s -> upper + s));
            //            assertEquals("MESSAGEmessage", cf.join());

            // Idea impl (uses starter):
            // Works only in debug mode? Something async ain't right
            //            CompletableFuture.completedFuture(weatherData)
            //                .thenApplyAsync(OpenWeatherData::getLatitude)
            //                .thenComposeAsync(lat -> CompletableFuture.completedFuture(weatherData)
            //                    .thenApplyAsync(OpenWeatherData::getLongitude)
            //                    .thenApplyAsync(lon -> CompletableFuture.supplyAsync(() -> timeZoneService.getTimeZone(lat, lon))
            //                        .thenCombineAsync( CompletableFuture.supplyAsync(() -> elevationService.getElevation(lat, lon)),
            //                            (timeZoneData, elevationData) -> {
            //                                outputWeatherDescription(weatherData.getName(),
            //                                                     weatherData.getTemperature(),
            //                                                     timeZoneData.getTimeZoneName(),
            //                                                     elevationData.getElevation());
            //                            return null;
            //                        })
            //                    )
            //                );

            // Works, but is rudimentary (uses starter):
            //            AtomicReference<GoogleTimeZoneData> timeZoneData = new AtomicReference<>(new GoogleTimeZoneData());
            //            AtomicReference<GoogleElevationData> elevationData = new AtomicReference<>(new GoogleElevationData());
            //
            //            CompletableFuture<Void> timeZoneFuture = CompletableFuture.runAsync(
            //                () -> timeZoneData.set(timeZoneService.getTimeZone(latitude, longitude)));
            //            CompletableFuture<Void> elevationFuture = CompletableFuture.runAsync(
            //                () -> elevationData.set(elevationService.getElevation(latitude, longitude)));
            //
            //            CompletableFuture.allOf(timeZoneFuture, elevationFuture).get();

            //            outputWeatherDescription(weatherData.getName(), weatherData.getTemperature(),
            //                                     timeZoneData.get().getTimeZoneName(), elevationData.get().getElevation());
        }
    }
}
