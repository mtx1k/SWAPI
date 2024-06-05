package de.shekhovtsov;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {
    public void writeCsv (String fileName, List<SWCharacter> characterList) {
        String[] header = {
                "name",
                "height",
                "mass",
                "hair_color",
                "skin_color",
                "eye_color",
                "birth_year",
                "gender",
                "species_name",
                "films"
        };
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(header);
            for (SWCharacter swCharacter : characterList) {
                String[] row = getString(swCharacter);
                writer.writeNext(row);
            }
            System.out.println("CSV file created successfully with OpenCSV: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing CSV file with OpenCSV: " + e.getMessage());
        }

    }
    private String[] getString(SWCharacter swCharacter) {
        String[] row = new String[11];
        row[0] = swCharacter.name();
        row[1] = swCharacter.height();
        row[2] = swCharacter.mass();
        row[3] = swCharacter.hair_color();
        row[4] = swCharacter.skin_color();
        row[5] = swCharacter.eye_color();
        row[6] = swCharacter.birth_year();
        row[7] = swCharacter.gender();
        row[8] = swCharacter.species()[0].name();
        StringBuilder result = new StringBuilder();
        for (String s: swCharacter.films()) {
            result.append(s).append(", ");
        }
        row[9] = result.substring(0, result.length() - 2);
        return row;
    }
}
