package matt.project.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

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

    @Override
    public void run(ApplicationArguments args) throws ExecutionException, InterruptedException
    {
        // TODO Clearer args handling
        if (0 < args.getSourceArgs().length) {
            OpenWeatherData weatherData = weatherService.getWeather(args.getSourceArgs()[0]);
            Double latitude = weatherData.getLatitude();
            Double longitude = weatherData.getLongitude();

            AtomicReference<GoogleTimeZoneData> timeZoneData = new AtomicReference<>(new GoogleTimeZoneData());
            AtomicReference<GoogleElevationData> elevationData = new AtomicReference<>(new GoogleElevationData());

            CompletableFuture<Void> timeZoneFuture = CompletableFuture.runAsync(
                () -> timeZoneData.set(timeZoneService.getTimeZone(latitude, longitude)));
            CompletableFuture<Void> elevationFuture = CompletableFuture.runAsync(
                () -> elevationData.set(elevationService.getElevation(latitude, longitude)));

            CompletableFuture.allOf(timeZoneFuture, elevationFuture).get();

            String weatherDescription = String.format(
                "At the location %s, the temperature is %f, the timezone is %s, and the elevation is %f.",
                weatherData.getName(),
                weatherData.getTemperature(),
                timeZoneData.get().getTimeZoneName(),
                elevationData.get().getElevation());

            System.out.println(weatherDescription);
        }
    }
}
