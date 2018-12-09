package matt.project.weather;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:keys.properties")
public class WeatherApp implements ApplicationRunner {

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static void main(String[] args)
    {
        SpringApplication.run(WeatherApp.class, args);
    }

    @Override
    public void run(ApplicationArguments args)
    {
        System.out.println(
            "At the location $CITY_NAME, the temperature is $TEMPERATURE, the timezone is $TIMEZONE, and the elevation is $ELEVATION.");
    }
}
