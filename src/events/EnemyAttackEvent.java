package events;

import characters.Enemy;
import characters.Player;

public class EnemyAttackEvent implements GameEvent {

    private final Enemy enemy;
    private final Player player;

    public EnemyAttackEvent(Enemy enemy, Player player) {
        this.enemy = enemy;
        this.player = player;
    }

    @Override
    public void execute() {
        if (!enemy.isAlive() || !player.isAlive()) return;
        if (skipIfFrozen(enemy)) return;

        enemy.performAttack(player);
        enemy.updateStatus();
    }
}
