package matt.project.weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("resource")
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherAppTest_Spring {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams()
    {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams()
    {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void generatesWeatherDescription()
    {
        // Must trigger #main from test to ensure it feeds System#out into the stream under test
        WeatherApp.main(new String[]{});
        Pattern weatherDescriptionPattern = Pattern.compile(
                "At the location .*, the temperature is .*, the timezone is .*, and the elevation is .*\\.");
        assertThat(weatherDescriptionPattern.matcher(outContent.toString()).find(), is(true));
    }
}
