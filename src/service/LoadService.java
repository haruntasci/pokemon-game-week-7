package service;

import model.*;
import model.Character;
import service.interfaces.ILoadService;

import java.util.ArrayList;
import java.util.List;

public class LoadService implements ILoadService {

    @Override
    public List<Character> loadCharacters() {
        SpecialPower strategy1 = new Strategy("Strategy I", 4, 1);
        SpecialPower strategy2 = new Strategy("Strategy II", 3, 1);
        SpecialPower strategy3 = new Strategy("Strategy III", 5, 1);
        SpecialPower strategy4 = new Strategy("Strategy IV", 2, 1);

        Character ash = new Ash("Ash", strategy1, null);
        Character brock = new Brock("Brock", strategy2, null);
        Character james = new James("James", strategy3, null);
        Character serena = new Serena("Serena", strategy4, null);

        List<Character> characterList = new ArrayList<>();
        characterList.add(ash);
        characterList.add(brock);
        characterList.add(james);
        characterList.add(serena);
        return characterList;
    }

    @Override
    public List<Pokemon> loadPokemons() {
        SpecialPower electricity = new Electricity("Electricty", 3, 1);
        SpecialPower water = new Water("Water", 1, 1);
        SpecialPower fire = new Fire("Fire", 5, 1);
        SpecialPower earth = new Earth("Earth", 4, 1);

        Pokemon pokemon1 = new Pikachu("Pikachu", 100, 10, TypeEnum.ELECTRICITY, electricity,
                Weather.RAINY);
        Pokemon pokemon2 = new Sqiurtle("Squirtle", 100, 8, TypeEnum.WATER, water,
                Weather.TORNADO);
        Pokemon pokemon3 = new Charmander("Charmender", 100, 12, TypeEnum.FIRE, fire,
                Weather.SNOWY);
        Pokemon pokemon4 = new Balbausar("Balbausar", 100, 7, TypeEnum.EARTH, earth,
                Weather.SUNNY);

        List<Pokemon> pokemonList = new ArrayList<>();
        pokemonList.add(pokemon1);
        pokemonList.add(pokemon2);
        pokemonList.add(pokemon3);
        pokemonList.add(pokemon4);

        return pokemonList;
    }
}
