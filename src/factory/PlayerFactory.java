package factory;

import characters.Player;
import combat.DefaultAttack;

public class PlayerFactory {

    public static Player createPlayer(String name) {
        return new Player(name, 100, 25, new DefaultAttack());
    }
}
