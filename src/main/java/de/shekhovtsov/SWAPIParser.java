package de.shekhovtsov;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SWAPIParser {
    Map<String, String> uniqueCharacters = new HashMap<>();
    private final List<SWMovie> swMovies = new ArrayList<>();

    public void parseSWAPI(String path) throws URISyntaxException, IOException {
        JsonNode root = parsePath(path);
        JsonNode movies = root.get("results");

        for (int i = 0; i < 3; i++) {
            String title = movies.get(i).get("title").asText();
            JsonNode characters = movies.get(i).get("characters");
            SWCharacter[] swCharacters = new SWCharacter[characters.size()];
            for (int j = 0; j < characters.size(); j++) {
                JsonNode character = parsePath(characters.get(j).asText());
                String characterName = character.get("name").asText();
                String specName = "unknown";

                if(character.get("species").get(0) != null) {
                    String specificationPath = character.get("species").get(0).asText();
                    JsonNode spec = parsePath(specificationPath);
                    specName = spec.get("name").asText();
                }
                SWSpecification[] swSpecification = {new SWSpecification(specName)};
                swCharacters[j] = new SWCharacter(characterName, swSpecification);
                uniqueCharacters.put(characterName, specName);
            }
            swMovies.add(new SWMovie(title, swCharacters));
        }
    }

    private static JsonNode parsePath(String characterPath) throws URISyntaxException, IOException {
        URL url = (new URI(characterPath)).toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream content = (InputStream) urlConnection.getContent();

        String body = new String(content.readAllBytes());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(body);
    }

    public void printUniqueCharacters() {
        AtomicInteger counter = new AtomicInteger(1);
        uniqueCharacters.forEach((key, value) -> System.out.println(counter.incrementAndGet() + "\tCharacter: " + key
                + "\n\tSpecification: " + value));
    }

    public void printMovies() {
        for (SWMovie movie: swMovies) {
            System.out.println("\nMovie: " + movie.title());
            for (SWCharacter character: movie.swCharacters()) {
                System.out.println("\tCharacter: " + character.name());
                for (SWSpecification specification: character.swSpecifications()) {
                    System.out.println("\t\tSpecification: " + specification.name());
                }
            }
        }
    }

    public List<SWMovie> getSwMovies() {
        return swMovies;
    }
}
