package de.shekhovtsov;

public record SWCharacter(
        String name,
        String height,
        String mass,
        String hair_color,
        String skin_color,
        String eye_color,
        String birth_year,
        String gender,
        SWSpecification[] species,
        String[] films
) {

}