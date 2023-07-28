
import service.GameService;
import service.interfaces.IGameService;

public class Main {
    public static void main(String[] args) {
        IGameService gameService = new GameService();
        gameService.run();
    }
}