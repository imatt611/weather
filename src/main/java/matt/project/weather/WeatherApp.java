package matt.project.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:keys.properties")
public class WeatherApp implements ApplicationRunner {

    // TODO Consider refactor to constructor injection
    @Autowired private OpenWeatherService weatherService;
    @Autowired private GoogleTimeZoneService timeZoneService;

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    @Override
    public void run(ApplicationArguments args)
    {
        // TODO Clearer args handling
        if (0 < args.getSourceArgs().length) {
            OpenWeatherData weatherData = weatherService.getWeather(args.getSourceArgs()[0]);
            Double latitude = weatherData.getCoordinates().get("lat"); // TODO Simplify
            Double longitude = weatherData.getCoordinates().get("lon"); // TODO Simplify

            GoogleTimeZoneData timeZoneData = timeZoneService.getTimeZone(latitude, longitude);

            String weatherDescription = String.format(
                "At the location %s, the temperature is %f, the timezone is %s, and the elevation is $ELEVATION.",
                weatherData.getName(),
                weatherData.getMain().get("temp"), // TODO Simplify
                timeZoneData.getTimeZoneName());

            System.out.println(weatherDescription);
        }
    }
}
