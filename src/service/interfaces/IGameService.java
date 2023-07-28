package service.interfaces;

import model.Character;
import model.Player;
import model.Pokemon;
import model.Weather;

import java.util.ArrayList;
import java.util.Scanner;

public interface IGameService {

    public void run();

    public void rollTheDice();

    public void startTheFight();

    public void startRoundsLoop();

    public Pokemon getWeakestPokemon();


    public void pokemonTransfer();

    public Weather getRandomWeather();

    public void switchPlayers();

    public void playerSelectionProcess();

    public void attack(Player attacker, Player defender, boolean isPokeSpecialAttack, boolean isCharSpecialAttack);

    public int calculateDamage(Pokemon attackingPokemon, Player attacker,
                               boolean isPokeSpecialAttack, boolean isCharSpecialAttack, boolean specialAttack);

    public boolean healthCheck(Player player);
}
