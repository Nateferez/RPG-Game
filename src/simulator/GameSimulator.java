package simulator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import ability.Ability;
import ability.HealingAbility;
import characters.Enemy;
import characters.Player;
import events.AbilityUseEvent;
import events.EnemyAttackEvent;
import events.GameEvent;
import events.PlayerAttackEvent;
import events.PlayerUsePotionEvent;
import events.ReloadWeaponEvent;
import events.WeaponUseEvent;

/** One fight: an event queue plus the policies for choosing the enemy's and the auto-player's actions. */
public class GameSimulator {

    private final Queue<GameEvent> eventQueue = new LinkedList<>();
    private final Random random;

    private final Player player;
    private final Enemy enemy;

    public GameSimulator(Player player, Enemy enemy) {
        this(player, enemy, new Random());
    }

    /** Inject a seeded Random to make fights reproducible (unit tests, balance runs). */
    public GameSimulator(Player player, Enemy enemy, Random random) {
        this.player = player;
        this.enemy = enemy;
        this.random = random;
    }

    public void schedule(GameEvent event) {
        eventQueue.add(event);
    }

    /** Runs exactly one queued event. Returns false if nothing was queued. */
    public boolean step() {
        if (isOver()) return false;
        GameEvent event = eventQueue.poll();
        if (event == null) return false;
        event.execute();
        return true;
    }

    /** Smart-ish auto player: reload, heal when low, use sensible potions, prefer the weapon. */
    public void scheduleAutoPlayerTurn() {
        if (!player.isAlive()) return;

        if (player.shouldReload()) {
            schedule(new ReloadWeaponEvent(player));
            return;
        }

        var heal = player.findAbility(HealingAbility.class);
        if (heal.isPresent() && heal.get().isAvailable()
                && player.getHealth() < 0.4 * player.getMaxHealth()) {
            schedule(new AbilityUseEvent(player, player, heal.get()));
            return;
        }

        if (player.hasPotion() && player.getPotion().worthUsing(player, enemy) && random.nextDouble() < 0.5) {
            schedule(new PlayerUsePotionEvent(player, enemy));
            return;
        }

        if (player.hasAmmo()) {
            schedule(new WeaponUseEvent(player, enemy));
        } else {
            schedule(new PlayerAttackEvent(player, enemy));
        }
    }

    /** Chooses between an ability and a normal attack (the single place this is decided). */
    public void nextEnemyTurn() {
        if (!enemy.isAlive()) return;

        if (enemy.shouldUseAbility()) {
            Ability ability = enemy.selectAbility();
            schedule(new AbilityUseEvent(enemy, player, ability));
        } else {
            schedule(new EnemyAttackEvent(enemy, player));
        }
    }

    public boolean isOver() {
        return !player.isAlive() || !enemy.isAlive();
    }

    public Player getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }
}
