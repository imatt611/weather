package matt.project.weather;

@FunctionalInterface
public interface OpenWeatherDataProvider<T, U, V, W> {
    W apply(T endpointTemplate, U zipCode, V apiKey);
}
