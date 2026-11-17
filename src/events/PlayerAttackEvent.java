package events;

import characters.Enemy;
import characters.Player;

/** Melee attack using the player's attack behaviour. */
public class PlayerAttackEvent implements GameEvent {

    private final Player player;
    private final Enemy enemy;

    public PlayerAttackEvent(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public void execute() {
        if (!player.isAlive() || !enemy.isAlive()) return;
        if (skipIfFrozen(player)) return;
        player.attack(enemy);
        player.updateStatus();
    }
}
