package com.mycompany.earthapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author silva
 */
public class EarthApi {

    private static final String BASE_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";

    public static void main(String[] args) {
        int year = 2019; // Cambia el año según tus necesidades
        int limit = 10;   // Cambia el límite de resultados según tus necesidades

        try {
            String response = getEarthquakeDataByYear(year, limit);
            displayEarthquakeData(response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getEarthquakeDataByYear(int year, int limit) throws IOException, InterruptedException {
        // Generar fechas de inicio y fin para el año dado
        String startTime = LocalDate.of(year, 1, 1).toString();
        String endTime = LocalDate.of(year, 12, 31).toString();
        
        // Construir URL con los parámetros
        String apiUrl = BASE_API_URL + "&starttime=" + startTime + "&endtime=" + endTime + "&limit=" + limit;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void displayEarthquakeData(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray features = jsonResponse.getJSONArray("features");

        System.out.println("Mostrando " + features.length() + " terremotos del año especificado:");
        
        for (int i = 0; i < features.length(); i++) {
            JSONObject earthquake = features.getJSONObject(i);
            JSONObject properties = earthquake.getJSONObject("properties");

            String place = properties.getString("place");
            double magnitude = properties.getDouble("mag");
            long time = properties.getLong("time");

            System.out.println("Lugar: " + place);
            System.out.println("Magnitud: " + magnitude);
            System.out.println("Fecha y hora (Unix): " + time);
            System.out.println("---------------------------------");
        }
    }
}