package matt.project.weather.util;

public enum LatitudeLongitude {
    ;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private static double getValidatedCoordinate(double coord, double min, double max, String coordName)
    {
        boolean isValid = isWithinRange(coord, min, max);
        if (isValid) {
            return coord;
        }
        throw new IllegalArgumentException(
            String.format("%s %f is out of range (%f to %f).", coordName, coord, min, max));
    }

    private static boolean isWithinRange(double coord, double min, double max)
    {
        return min <= coord && max >= coord;
    }

    public static double getValidatedLongitude(double longitude)
    {
        return getValidatedCoordinate(longitude, MIN_LONGITUDE, MAX_LONGITUDE, "Longitude");
    }

    public static double getValidatedLatitude(double latitude)
    {
        return getValidatedCoordinate(latitude, MIN_LATITUDE, MAX_LATITUDE, "Latitude");
    }
}
