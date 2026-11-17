package events;

import characters.Enemy;
import characters.Player;
import util.GameLog;

/** Potions can be used even while frozen (an Unfreeze Potion would otherwise be useless). */
public class PlayerUsePotionEvent implements GameEvent {

    private final Player player;
    private final Enemy enemy;

    public PlayerUsePotionEvent(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public void execute() {
        if (!player.isAlive()) return;

        if (player.hasPotion()) {
            player.usePotion(enemy);
        } else {
            GameLog.log(player.getName() + " reached for a potion but the bag was empty!");
        }
        player.updateStatus();
    }
}
