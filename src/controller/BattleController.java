package controller;

import javax.swing.Timer;

import ability.HealingAbility;
import characters.Enemy;
import characters.Player;
import config.GameConfig;
import events.AbilityUseEvent;
import events.GameEvent;
import events.PlayerAttackEvent;
import events.PlayerUsePotionEvent;
import events.ReloadWeaponEvent;
import events.WeaponUseEvent;
import factory.PlayerFactory;
import factory.PotionFactory;
import factory.WeaponFactory;
import simulator.BattleSession;
import simulator.BattleSession.Outcome;
import simulator.GameListener;
import util.GameLog;

/**
 * Swing-side glue: owns the timers and the turn phase, delegates every rule to BattleSession.
 * Everything here runs on the Event Dispatch Thread (button clicks and javax.swing.Timer).
 */
public class BattleController {

    public enum Phase { PLAYER_TURN, ENEMY_TURN, GAME_OVER }

    private static final int ENEMY_DELAY_MS = 700;
    private static final int DEFAULT_AUTO_DELAY_MS = 800;
    private static final int MIN_AUTO_DELAY_MS = 200;
    private static final int MAX_AUTO_DELAY_MS = 2000;

    private BattleSession session;
    private Phase phase = Phase.GAME_OVER;
    private GameListener listener;

    private Timer enemyTimer;
    private Timer autoTimer;

    private boolean autoPlay;
    private boolean autoPaused;
    private int autoDelayMs = DEFAULT_AUTO_DELAY_MS;

    // ---------- accessors ----------

    public void setListener(GameListener listener) { this.listener = listener; }
    public Phase getPhase() { return phase; }
    public Player getPlayer() { return session == null ? null : session.getPlayer(); }
    public Enemy getEnemy() { return session == null ? null : session.getEnemy(); }
    public int getTier() { return session == null ? 0 : session.getTier(); }
    public boolean isAutoPlay() { return autoPlay; }
    public boolean isAutoPaused() { return autoPaused; }
    public int getAutoPlayDelay() { return autoDelayMs; }

    // ---------- starting a run ----------

    
    public void newBattle(Player p, boolean auto, int tier) {
        stopTimers();

        autoPlay = auto;
        autoPaused = false;
        autoDelayMs = DEFAULT_AUTO_DELAY_MS;

        session = new BattleSession(p, tier);
        session.announce();

        phase = Phase.PLAYER_TURN;
        fireChanged();

        if (autoPlay) {
            autoTimer = new Timer(autoDelayMs, ev -> autoStep());
            autoTimer.start();
        }
    }

    public void newBattle(GameConfig config) {

        Player p = PlayerFactory.createPlayer(config.getPlayerName());

        if (config.hasWeapon()) {
            p.equipWeapon(WeaponFactory.createWeapon(config.getWeapon()));
        }

        if (config.hasPotion()) {
            p.equipPotion(PotionFactory.createPotion(config.getPotion()));
        }

        newBattle(p, config.isAutoPlay(), config.getStartingTier());
    }


    // ---------- manual mode: one method per button ----------

    public void playerAttack() {
        if (session != null) playerAct(new PlayerAttackEvent(getPlayer(), getEnemy()));
    }

    public void playerShoot() {
        if (session != null && getPlayer().hasAmmo()) playerAct(new WeaponUseEvent(getPlayer(), getEnemy()));
    }

    public void playerReload() {
        if (session != null && getPlayer().canReload()) playerAct(new ReloadWeaponEvent(getPlayer()));
    }

    public void playerPotion() {
        if (session != null && getPlayer().hasPotion()) {
            playerAct(new PlayerUsePotionEvent(getPlayer(), getEnemy()));
        }
    }

    public void playerUseAbility() {
        if (session == null) return;
        Player p = getPlayer();
        p.findAbility(HealingAbility.class)
                .filter(a -> a.isAvailable())
                .ifPresent(a -> playerAct(new AbilityUseEvent(p, p, a)));   // heal targets the player
    }

    private void playerAct(GameEvent event) {
        if (phase != Phase.PLAYER_TURN || autoPlay) return;

        phase = Phase.ENEMY_TURN;          // disables the buttons right away
        Outcome outcome = session.playerAct(event);

        if (handle(outcome)) return;

        fireChanged();
        enemyTimer = new Timer(ENEMY_DELAY_MS, ev -> {
            Outcome o = session.enemyAct();
            if (handle(o)) return;
            phase = Phase.PLAYER_TURN;
            fireChanged();
        });
        enemyTimer.setRepeats(false);
        enemyTimer.start();
    }

    // ---------- auto-play ----------

    private void autoStep() {
        if (phase == Phase.GAME_OVER) return;

        if (phase == Phase.PLAYER_TURN) {
            Outcome o = session.playerAutoAct();
            if (handle(o)) return;
            phase = Phase.ENEMY_TURN;
        } else {
            Outcome o = session.enemyAct();
            if (handle(o)) return;
            phase = Phase.PLAYER_TURN;
        }
        fireChanged();
    }

    public void setAutoPlaySpeed(int delay) {
        if (delay < MIN_AUTO_DELAY_MS || delay > MAX_AUTO_DELAY_MS) return;
        autoDelayMs = delay;
        if (autoTimer != null) {
            autoTimer.setDelay(delay);
            autoTimer.setInitialDelay(delay);
        }
    }

    public void pauseAuto() {
        if (autoPlay && autoTimer != null && autoTimer.isRunning() && phase != Phase.GAME_OVER) {
            autoTimer.stop();
            autoPaused = true;
            fireChanged();
        }
    }

    public void resumeAuto() {
        if (autoPlay && autoTimer != null && autoPaused && phase != Phase.GAME_OVER) {
            autoPaused = false;
            autoTimer.start();
            fireChanged();
        }
    }

    // ---------- freezing the game while a modal dialog is open ----------

    /** Stops timers without changing state (used while the setup dialog is showing). */
    public void suspend() {
        if (enemyTimer != null) enemyTimer.stop();
        if (autoTimer != null) autoTimer.stop();
    }

    /** Undoes suspend() if the player cancelled the dialog. */
    public void resume() {
        if (phase == Phase.GAME_OVER) return;
        if (enemyTimer != null && phase == Phase.ENEMY_TURN) enemyTimer.restart();
        if (autoTimer != null && !autoPaused) autoTimer.start();
    }

    // ---------- outcome handling ----------

    /** Returns true if the turn sequence must stop (game over). */
    private boolean handle(Outcome outcome) {
        switch (outcome) {
            case DEFEAT -> {
                endGame(false, "Defeat. You were defeated in battle.");
                return true;
            }
            case VICTORY -> {
                endGame(true, "Victory! You defeated the final enemy.");
                return true;
            }
            case NEXT_ENEMY -> {
                // A new enemy has already been created by the session; the player acts first.
                if (enemyTimer != null) enemyTimer.stop();
                phase = Phase.PLAYER_TURN;
                fireChanged();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void endGame(boolean won, String message) {
        phase = Phase.GAME_OVER;
        stopTimers();
        fireChanged();
        GameLog.log(message);
        if (listener != null) listener.onGameOver(won, message);
    }

    private void stopTimers() {
        if (enemyTimer != null) { enemyTimer.stop(); enemyTimer = null; }
        if (autoTimer != null) { autoTimer.stop(); autoTimer = null; }
    }

    private void fireChanged() {
        if (listener != null) listener.onStateChanged();
    }
}
