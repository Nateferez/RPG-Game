package simulator;

public interface GameListener {

    void onStateChanged();

    void onGameOver(boolean playerWon, String message);
}
