package simulator;

import characters.Enemy;
import characters.Player;
import events.GameEvent;
import progression.EncounterManager;
import util.GameLog;

/**
 * All game rules for a run (fights, progression, win/lose) with no Swing in sight.
 * The GUI controller adds timers on top; BatchSimulator and unit tests use it directly.
 */
public class BattleSession {

    public enum Outcome { ONGOING, NEXT_ENEMY, VICTORY, DEFEAT }

    private static final double VICTORY_HEAL_RATIO = 0.5;

    private final Player player;
    private final EncounterManager encounters;
    private Enemy enemy;
    private GameSimulator sim;

    public BattleSession(Player player, int startingTier) {
        this.player = player;
        this.encounters = new EncounterManager(player, startingTier);
        this.enemy = factory.EnemyFactory.createEnemy(encounters.getCurrentTier());
        this.sim = new GameSimulator(player, enemy);
    }

    public void announce() {
        GameLog.logf("=== %s (level %d) faces a %s! ===", player.getName(), player.getLevel(), enemy.getName());
    }

    // ---------- turns ----------

    public Outcome playerAct(GameEvent event) {
        sim.schedule(event);
        sim.step();
        return resolve();
    }

    public Outcome playerAutoAct() {
        sim.scheduleAutoPlayerTurn();
        sim.step();
        return resolve();
    }

    public Outcome enemyAct() {
        sim.nextEnemyTurn();
        sim.step();
        return resolve();
    }

    // ---------- outcome handling ----------

    private Outcome resolve() {
        if (!player.isAlive()) {
            GameLog.logf("%s has fallen...", player.getName());
            return Outcome.DEFEAT;
        }
        if (!enemy.isAlive()) {
            GameLog.logf("The %s is defeated!", enemy.getName());
            if (encounters.isFinalTier()) {
                return Outcome.VICTORY;
            }
            advance();
            return Outcome.NEXT_ENEMY;
        }
        return Outcome.ONGOING;
    }

    private void advance() {
        enemy = encounters.createNextEnemy();

        // Recover a little between fights
        double before = player.getHealth();
        player.performHealing(player.getMaxHealth() * VICTORY_HEAL_RATIO);
        if (player.hasWeapon()) player.getWeapon().refill();
        player.getAttackBehavior().reset();
        double gained = player.getHealth() - before;
        if (gained >= 1) {
            GameLog.logf("You catch your breath: +%.0f HP (%.0f/%.0f)", gained, player.getHealth(), player.getMaxHealth());
        }
        if (player.hasWeapon()) {
            GameLog.logf("You reload the %s.", player.getWeapon().getName());
        }

        sim = new GameSimulator(player, enemy);
        GameLog.logf("--- Tier %d: a %s appears! ---", encounters.getCurrentTier(), enemy.getName());
    }

    // ---------- accessors ----------

    public Player getPlayer() { return player; }
    public Enemy getEnemy() { return enemy; }
    public int getTier() { return encounters.getCurrentTier(); }
    public boolean isFinalTier() { return encounters.isFinalTier(); }
}
