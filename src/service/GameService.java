package service;

import model.Character;
import model.Player;
import model.Pokemon;
import model.Weather;
import service.interfaces.IGameService;

import java.util.*;

public class GameService implements IGameService {
    private Player[] players;
    private LoadService loadService;
    private List<Character> characterList;
    private List<Pokemon> pokemonList;
    private Scanner input;
    private Random random;
    private int roundCounter;

    public GameService() {
        this.players = new Player[2];
        this.loadService = new LoadService();
        this.characterList = loadService.loadCharacters();
        this.pokemonList = loadService.loadPokemons();
        this.input = new Scanner(System.in);
        this.random = new Random();
        this.roundCounter = 0;
    }

    @Override
    public void run() {
        playerSelectionProcess();
        startRoundsLoop();
    }

    @Override
    public void startRoundsLoop() {
        for (roundCounter = 0; roundCounter < 2; roundCounter++) {
            startTheFight();
        }
    }

    @Override
    public Pokemon getWeakestPokemon() {
        int pokemonDamage = 99;
        Pokemon weakestPokemon = new Pokemon();
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getDamage() < pokemonDamage) {
                pokemonDamage = pokemon.getDamage();
                weakestPokemon = pokemon;
            }
        }
        return weakestPokemon;
    }


    @Override
    public void startTheFight() {
        rollTheDice();
        boolean flag = true;
        System.out.println((roundCounter + 1) + ". Round başladı!");
        while (flag) {
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println(players[0].getName() + " saldırı sırası");
            System.out.println("1-Normal saldırı");
            System.out.println("2-Karakter ve Pokemon özel güçlü saldırı");
            System.out.println("3-Karakter özel güçlü saldırı");
            System.out.println("4-Pokemon özel güçlü saldırı");
            System.out.println("-----------------------------------------------------------------------------");
            int attackType = input.nextInt();
            if (attackType == 1) {
                attack(players[0], players[1], false, false);
            } else if (attackType == 2) {
                attack(players[0], players[1], true, true);
            } else if (attackType == 3) {
                attack(players[0], players[1], false, true);
            } else if (attackType == 4) {
                attack(players[0], players[1], true, false);
            } else {
                System.out.println("Yanlış tuşlama yaptınız! Sıra rakibinize geçti!");
            }

            if (!healthCheck(players[1])) {
                if (players[1].getCharacter().getPokemonList().size() > 1 &&
                        players[1].getCharacter().getPokemonList().get(1).getHealth() > 0) {
                    swapPokemonAtIndexes(players[1].getCharacter().getPokemonList(), 0, 1);
                } else {
                    flag = false;
                }
            }
            switchPlayers();
        }
        if (roundCounter == 0)
            pokemonTransfer();

    }

    private void swapPokemonAtIndexes(List<Pokemon> list, int index1, int index2) {
        Pokemon temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }

    //Yenilen oyuncunun pokemonunu yenen oyuncuya veren ve yenilen oyuncuya en güçsüz pokemonu veren fonksiyon
    @Override
    public void pokemonTransfer() {
        Pokemon earnedPokemon = players[0].getCharacter().getPokemonList().get(0);
        earnedPokemon.setHealth(100);
        List<Pokemon> winnerPlayerPokemons = new ArrayList<>(players[1].getCharacter().getPokemonList());
        winnerPlayerPokemons.add(earnedPokemon);
        players[1].getCharacter().setPokemonList(winnerPlayerPokemons);
        Pokemon weakestPokemon = getWeakestPokemon();
        players[0].getCharacter().setPokemonList(List.of(weakestPokemon));

        System.out.println(players[0].getName() + " " + earnedPokemon.getName()
                + " pokemonunu kaybetti ve en güçsüz pokemon olan " + weakestPokemon.getName() + " verildi");
        System.out.println(players[1].getName() + " " + earnedPokemon.getName() + " pokemonunu ele geçirdi!");
    }


    @Override
    public Weather getRandomWeather() {
        List<Weather> weathers = Arrays.asList(Weather.class.getEnumConstants());
        int randomInt = random.nextInt(weathers.size());
        return weathers.get(randomInt);
    }

    @Override
    public void rollTheDice() {
        int randomIndex = random.nextInt(2);
        if (randomIndex == 1) {
            switchPlayers();
        }
        System.out.println("İlk saldırıyı " + players[0].getName() + " yapacak!");
    }


    @Override
    public void switchPlayers() {
        Player temp = players[0];
        players[0] = players[1];
        players[1] = temp;
    }


    @Override
    public void playerSelectionProcess() {
        for (int i = 0; i < 2; i++) {

            System.out.println("Player " + (i + 1) + " için isim giriniz: ");
            String player = input.nextLine();
            players[i] = new Player(player);
            System.out.println(player + " oyuncusu için karakter seçiniz: ");
            for (int j = 0; j < characterList.size(); j++) {
                System.out.println((j + 1) + "-" + characterList.get(j).getName());
            }
            int indexOfSelectedCharacter = input.nextInt();
            Character selectedCharacter = characterList.get(indexOfSelectedCharacter - 1);
            //Seçilen karakter listeden kaldırılıyor
            characterList.remove(selectedCharacter);
            System.out.println(selectedCharacter.getName() + " karakteri için pokemon seçiniz:");
            for (int k = 0; k < pokemonList.size(); k++) {
                System.out.println((k + 1) + "-" + pokemonList.get(k).getName());
            }
            int indexOfSelectedPokemon = input.nextInt();
            Pokemon selectedPokemon = pokemonList.get(indexOfSelectedPokemon - 1);
            selectedCharacter.setPokemonList(List.of(selectedPokemon));
            players[i].setCharacter(selectedCharacter);
            //Seçilen pokemon listeden kaldırılıyor
            pokemonList.remove(selectedPokemon);
            input.nextLine();
        }

    }

    @Override
    public void attack(Player attacker, Player defender, boolean isPokeSpecialAttack, boolean isCharSpecialAttack) {

        Weather weather = getRandomWeather();

        Pokemon attackingPokemon = attacker.getCharacter().getPokemonList().get(0);
        Pokemon defendingPokemon = defender.getCharacter().getPokemonList().get(0);

        int attackerPokemonRemainingRights = attackingPokemon.getSpecialPower().getRemainingRights();
        int attackerCharacterRemainingRights = attacker.getCharacter().getSpecialPower().getRemainingRights();

        boolean specialAttack = isPokeSpecialAttack && isCharSpecialAttack ?
                (attackerPokemonRemainingRights > 0 && attackerCharacterRemainingRights > 0) :
                (isPokeSpecialAttack ? attackerPokemonRemainingRights > 0 : attackerCharacterRemainingRights > 0);

        int damage = calculateDamage(attackingPokemon, attacker, isPokeSpecialAttack, isCharSpecialAttack, specialAttack);

        if (defendingPokemon.getWeatherWeakness().equals(weather)) {
            damage += weather.getDamageValue();
        }
        defendingPokemon.setHealth(defendingPokemon.getHealth() - damage);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println(weather);
        System.out.println(attacker.getName() + "--> " + attacker.getCharacter().getName() + " karakteri "
                + attackingPokemon.getName()
                + " pokemonu ile " + damage + " şiddetinde vurdu!");
        System.out.println(defendingPokemon.getName() + " canı: " + defendingPokemon.getHealth());
        System.out.println("-----------------------------------------------------------------------------");
    }

    public int calculateDamage(Pokemon attackingPokemon, Player attacker,
                               boolean isPokeSpecialAttack, boolean isCharSpecialAttack, boolean specialAttack) {
        int damage = 0;
        if (specialAttack) {
            if (isPokeSpecialAttack && isCharSpecialAttack) {
                damage += attackingPokemon.specialAttack();
                damage += attacker.getCharacter().getSpecialPower().getExtraDamage();
                attacker.getCharacter().getSpecialPower().setRemainingRights(
                        attacker.getCharacter().getSpecialPower().getRemainingRights() - 1);
            } else if (isPokeSpecialAttack) {
                damage += attackingPokemon.specialAttack();
            } else {
                damage += attackingPokemon.getDamage();
                damage += attacker.getCharacter().getSpecialPower().getExtraDamage();
                attacker.getCharacter().getSpecialPower().setRemainingRights(
                        attacker.getCharacter().getSpecialPower().getRemainingRights() - 1);
            }
        } else {
            if (!(isPokeSpecialAttack || isCharSpecialAttack)) {
                damage += attackingPokemon.getDamage();
            }
        }
        return damage;
    }

    @Override
    public boolean healthCheck(Player player) {
        Pokemon activePokemon = player.getCharacter().getPokemonList().get(0);
        if (activePokemon.getHealth() > 0) {
            System.out.println("Oyun devam ediyor.");
            return true;
        } else {
            if (player.getCharacter().getPokemonList().size() > 1
                    && player.getCharacter().getPokemonList().get(1).getHealth() > 0) {
                System.out.println(player.getName() + " " + activePokemon.getName()
                        + " pokemonunu kaybetti");
                System.out.println(player.getName() + " " + player.getCharacter().getPokemonList().get(1).getName()
                        + " pokemonunuyla savaşa devam edecek!");
            } else if (roundCounter == 0) {
                System.out.println(player.getName() + " " + (roundCounter + 1) + ". Roundu kaybetti");
            } else {
                System.out.println(player.getName() + " " + (roundCounter + 1) + ". Roundu kaybetti");
                System.out.println(player.getName() + " oyunu kaybetti");
            }
            return false;
        }
    }
}