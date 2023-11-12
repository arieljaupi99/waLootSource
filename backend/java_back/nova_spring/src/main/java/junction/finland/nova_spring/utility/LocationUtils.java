package junction.finland.nova_spring.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LocationUtils {
    private static final Random random = new Random();

    private LocationUtils() {
    }

    public static Map<String, Map<String, Double>> calculateNewCoordinates(double lat, double lon, double distanceMeters) {
        Map<String, Map<String, Double>> result = new HashMap<>();
        double bearing = randomFromArray();
        // North, East, South, West

        // Convert latitude and longitude from degrees to radians
        double latRad = Math.toRadians(lat);
        double lonRad = Math.toRadians(lon);
        // Earth's radius in meters
        double earthRadius = 6371000;
        // Convert bearing from degrees to radians
        double bearingRad = Math.toRadians(bearing);

        // Calculate the new latitude and longitude
        double newLatRad = Math.asin(Math.sin(latRad) * Math.cos(distanceMeters / earthRadius) +
                Math.cos(latRad) * Math.sin(distanceMeters / earthRadius) * Math.cos(bearingRad));

        double newLonRad = lonRad + Math.atan2(Math.sin(bearingRad) * Math.sin(distanceMeters / earthRadius) * Math.cos(latRad),
                Math.cos(distanceMeters / earthRadius) - Math.sin(latRad) * Math.sin(newLatRad));

        // Convert the new latitude and longitude from radians to degrees
        double newLat = Math.toDegrees(newLatRad);
        double newLon = Math.toDegrees(newLonRad);

        Map<String, Double> newCoordinates = new HashMap<>();
        newCoordinates.put("latitude", newLat);
        newCoordinates.put("longitude", newLon);

        result.put(formatBearing(bearing), newCoordinates);


        return result;
    }

    private static String formatBearing(double bearing) {
        if (bearing == 0.0) return "North";
        if (bearing == 90.0) return "East";
        if (bearing == 180.0) return "South";
        if (bearing == 270.0) return "West";
        return "Unknown";
    }

    private static double randomFromArray() {
        double[] bearings = {0.0, 90.0, 180.0, 270.0};
        // Create a Random object


        // Generate a random index to access the array
        int randomIndex = random.nextInt(bearings.length);

        // Get the random value from the array
        return bearings[randomIndex];
    }

}
