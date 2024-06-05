package de.shekhovtsov;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class SWParser {
    private final URL url;

    public SWParser(String path) throws URISyntaxException, MalformedURLException {
        url = (new URI("https://swapi.dev/api/films")).toURL();
    }

    public String parseObject() throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream content = (InputStream) urlConnection.getContent();

        String body = new String(content.readAllBytes());

        ObjectMapper mapper = new ObjectMapper();
        SWObject swObject = mapper.readValue(body, SWObject.class);
        SWMovie[] movies = swObject.results();


        for (int i = 0; i < movies.length; i++) {
            movies[i].characters();
        }
        return null;
    }

    //private SWMovie parseMovie()
}
