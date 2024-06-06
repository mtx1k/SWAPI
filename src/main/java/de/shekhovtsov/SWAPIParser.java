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
    private final List<SWCharacter> characterList = new ArrayList<>();

    public void parseSWAPI(String path) throws URISyntaxException, IOException {
        JsonNode root = parsePath(path);
        JsonNode movies = root.get("results");
        Set<String> characterPathSet = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            JsonNode characters = movies.get(i).get("characters");
            for (int j = 0; j < characters.size(); j++) {
                characterPathSet.add(characters.get(j).asText());
            }
        }

        for (String characterPath: characterPathSet) {
            JsonNode character = parsePath(characterPath);
            characterList.add(parseCustomer(character));
        }
    }

    private SWCharacter parseCustomer(JsonNode character) throws URISyntaxException, IOException {
        String name = character.get("name").asText();
        String height = character.get("height").asText();
        String mass = character.get("mass").asText();
        String hair_color = character.get("hair_color").asText();
        String skin_color = character.get("skin_color").asText();
        String eye_color = character.get("eye_color").asText();
        String birth_year = character.get("birth_year").asText();
        String gender = character.get("gender").asText();
        String[] films = getFilms(character.get("films"));
        SWSpecification[] species = parseSpecies(character.get("species"));
        return new SWCharacter(name, height, mass, hair_color, skin_color, eye_color, birth_year, gender, species, films);
    }

    private SWSpecification[] parseSpecies(JsonNode species) throws URISyntaxException, IOException {
        String name = "unknown";
        if (species.get(0) != null) {
            name = parsePath(species.get(0).asText()).get("name").asText();
        }
        return new SWSpecification[] { new SWSpecification(name) };
    }

    private String[] getFilms(JsonNode films) {
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < films.size(); i++) {
            String filmNumber = films.get(i).asText().replaceAll("\\D", "");
            switch (filmNumber) {
                case "1":
                    titles.add("A New Hope");
                    break;
                case "2":
                    titles.add("The Empire Strikes Back");
                    break;
                case "3":
                    titles.add("Return of the Jedi");
                    break;
            }
        }

        return titles.toArray(new String[0]);
    }

    private JsonNode parsePath(String path) throws URISyntaxException, IOException {
        URL url = (new URI(path)).toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        InputStream content = (InputStream) urlConnection.getContent();

        String body = new String(content.readAllBytes());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readTree(body);
    }

    public void printUniqueCharacters() {
        AtomicInteger counter = new AtomicInteger(0);
        characterList.forEach(character -> System.out.println(
                        counter.incrementAndGet() +
                        "\tCharacter: " + character.name() +
                        "\n\tSpecification: " + character.species()[0].name()));
    }

    public List<SWCharacter> getCharacterList() {
        return characterList;
    }

}
