# Given Assignment
Create a utility using the language/tools of your choice that takes a ZIP-code, then outputs the city name,
current temperature, time zone, and general elevation at the location with a user-friendly message. For
example, “At the location $CITY_NAME, the temperature is $TEMPERATURE, the timezone is $TIMEZONE,
and the elevation is $ELEVATION”.
1. Use the Open WeatherMap current weather API to retrieve the current temperature and city name. You
will be required to sign up for a free API key.
1. Use the Google Time Zone API to get the current timezone for a location. You will again need to
register a “project” and sign up for a free API key with Google.
1. Use the Google Elevation API to retrieve elevation data for a location. You can use the same Google
API key as the Time Zone API.

# Execution
1. Apply provided (by email) or your own API keys for the given APIs in `src/main/resources/keys.properties`.
1. Build and execute, passing a `String` ZIP-code argument

# Implementation Characteristics
- I strictly used Test Driven Development.
- I decided to keep the scope of testing, and thus implementation, as reasonably narrow as I could to fulfill the given requirements and utilize the given APIs (and demonstrate in tests that they, specifically, are in use).
  - As a result, I only followed some conventions, such as providing `toString` and `hashCode` implementations, explicit no-arg component constructors, and the like, where the desired behaviors could be fulfilled by them (for an example, see the `equals` implementation on `OpenWeatherData.java`).
