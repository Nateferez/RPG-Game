package events;

import characters.Enemy;
import characters.Player;
import util.GameLog;

/** Fires the equipped weapon; falls back to a melee attack if there is no ammo. */
public class WeaponUseEvent implements GameEvent {

    private final Player player;
    private final Enemy enemy;

    public WeaponUseEvent(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public void execute() {
        if (!player.isAlive() || !enemy.isAlive()) return;
        if (skipIfFrozen(player)) return;

        if (player.hasAmmo()) {
            player.useWeapon(enemy);
        } else {
            GameLog.log(player.getName() + "'s weapon is empty - attacking in melee instead.");
            player.attack(enemy);
        }
        player.updateStatus();
    }
}
