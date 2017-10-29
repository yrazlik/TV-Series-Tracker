package com.yrazlik.tvseriestracker.data;

/**
 * Created by yrazlik on 29.10.2017.
 */

public class CastDto {

    private PersonDto person;
    private CharacterDto character;

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public CharacterDto getCharacter() {
        return character;
    }

    public void setCharacter(CharacterDto character) {
        this.character = character;
    }
}
