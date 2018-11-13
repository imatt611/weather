package matt.project.weather;

import org.junit.BeforeClass;
import org.junit.Test;

public class OpenWeatherTest_Unit {

    private static OpenWeatherService openWeather;

    @BeforeClass
    public static void setup()
    {
        openWeather = new OpenWeatherService();
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNegativeZipCode() throws IllegalArgumentException
    {
        openWeather.getWeather("-97209");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooFewDigits() throws IllegalArgumentException
    {
        openWeather.getWeather("972");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesTooManyDigits() throws IllegalArgumentException
    {
        openWeather.getWeather("972103009");
    }

    @Test(expected = IllegalArgumentException.class)
    public void refusesNonDigits() throws IllegalArgumentException
    {
        openWeather.getWeather("972g8");
    }
}
