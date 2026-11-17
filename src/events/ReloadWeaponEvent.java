package events;

import characters.Player;
import util.GameLog;

/** Reloading costs the player's whole turn. */
public class ReloadWeaponEvent implements GameEvent {

    private final Player player;

    public ReloadWeaponEvent(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (!player.isAlive() || !player.hasWeapon()) return;
        if (skipIfFrozen(player)) return;

        player.getWeapon().refill();
        GameLog.logf("%s reloads the %s.", player.getName(), player.getWeapon().getName());
        player.updateStatus();
    }
}
