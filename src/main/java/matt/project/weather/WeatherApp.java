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

    @Autowired OpenWeatherService weatherService;

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    @Override
    public void run(ApplicationArguments args)
    {
        if (0 < args.getSourceArgs().length) {
            System.out.println(args.getSourceArgs()[0]);
            // TODO Clearer args handling
            OpenWeatherData weatherData = weatherService.getWeather(args.getSourceArgs()[0]);

            String weatherDescription = String.format(
                "At the location %s, the temperature is %f, the timezone is $TIMEZONE, and the elevation is $ELEVATION.",
                weatherData.getName(),
                weatherData.getMain().get("temp")); // TODO Simplify

            System.out.println(weatherDescription);
        }
    }
}
