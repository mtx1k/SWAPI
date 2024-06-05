package de.shekhovtsov;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        URL url = (new URI("https://swapi.dev/api/films")).toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream content = (InputStream) urlConnection.getContent();

        String body = new String(content.readAllBytes());

        ObjectMapper mapper = new ObjectMapper();
        SWObject swObject = mapper.readValue(body, SWObject.class);
        SWMovie[] movies = swObject.results();

        SWMovie[] originalMovies = Arrays.copyOf(movies, 3);
        System.out.println(Arrays.toString(originalMovies));


    }
}