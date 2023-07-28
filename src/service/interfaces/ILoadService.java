package service.interfaces;

import model.Character;
import model.Pokemon;

import java.util.ArrayList;
import java.util.List;

public interface ILoadService {
    public List<Character> loadCharacters();

    public List<Pokemon> loadPokemons();
}
