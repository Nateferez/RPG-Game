package characters;

import combat.AttackBehavior;
import items.Potion;
import items.Weapon;
import util.GameLog;

public class Player extends GameCharacter {

    public static final int MAX_LEVEL = 3;
    private static final double ATTACK_PER_LEVEL = 5;
    private static final double HEALTH_PER_LEVEL = 30;

    private int level = 1;
    private Potion potion;
    private Weapon weapon;

    public Player(String name, double health, double attackPower, AttackBehavior attackBehavior) {
        super(name, health, attackPower, attackBehavior);
    }

    // ---------- progression ----------

    public void levelUp() {
        if (level >= MAX_LEVEL) return;
        level++;
        increaseAttackPower(ATTACK_PER_LEVEL);
        increaseMaxHealth(HEALTH_PER_LEVEL);
        GameLog.logf("*** LEVEL UP! %s is now level %d (attack %.0f, max HP %.0f) ***",
                getName(), level, getAttackPower(), getMaxHealth());
    }

    public int getLevel() { return level; }

    // ---------- weapon ----------

    public void equipWeapon(Weapon weapon) { this.weapon = weapon; }
    public Weapon getWeapon() { return weapon; }

    @Override
    public boolean hasWeapon() { return weapon != null; }

    @Override
    public void useWeapon(GameCharacter target) {
        if (weapon == null) throw new IllegalStateException("No weapon equipped");
        weapon.fire(this, target);
    }

    public boolean hasAmmo() { return weapon != null && !weapon.isEmpty(); }
    public boolean canReload() { return weapon != null && weapon.getBullets() < weapon.getMaxBullets(); }
    public boolean shouldReload() { return weapon != null && weapon.isEmpty(); }

    // ---------- potion ----------

    public void equipPotion(Potion potion) { this.potion = potion; }
    public Potion getPotion() { return potion; }
    public boolean hasPotion() { return potion != null; }

    /** The potion decides whether it affects the enemy or the player. */
    public void usePotion(GameCharacter enemy) {
        if (potion == null) throw new IllegalStateException("No potion available");
        Potion used = potion;
        potion = null;
        used.use(this, used.needsEnemyTarget() ? enemy : this);
    }
}
