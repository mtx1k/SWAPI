package de.shekhovtsov;

import java.io.IOException;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String path = "https://swapi.dev/api/films";

        SWAPIParser swapiParser = new SWAPIParser();
        swapiParser.parseSWAPI(path);
        swapiParser.printUniqueCharacters();

        CsvWriter csvWriter = new CsvWriter();
        csvWriter.writeCsv("SWCustomers.csv", swapiParser.getCharacterList());

    }
}