package characters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ability.Ability;
import combat.AttackBehavior;
import util.GameLog;

/** Base class for everything that fights. (Renamed from Character to avoid java.lang.Character.) */
public abstract class GameCharacter {

    private final String name;
    private double maxHealth;
    private double health;
    private double attackPower;
    private AttackBehavior attackBehavior;
    private final List<Ability> abilities = new ArrayList<>();
    private int freezeTurns = 0;

    protected GameCharacter(String name, double maxHealth, double attackPower, AttackBehavior attackBehavior) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        validateMaxHealth(maxHealth);
        validateAttackPower(attackPower);
        if (attackBehavior == null) {
            throw new IllegalArgumentException("AttackBehavior cannot be null");
        }
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
        this.attackBehavior = attackBehavior;
    }

    // ---------- combat ----------

    public void attack(GameCharacter target) {
        attackBehavior.attack(this, target);
    }

    public void takeDamage(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Damage cannot be negative");
        health = Math.max(0, health - amount);
    }

    public void performHealing(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Healing cannot be negative");
        health = Math.min(maxHealth, health + amount);
    }

    public boolean isAlive() {
        return health > 0;
    }

    // ---------- status effects ----------

    public void setFreezeTurns(int turns) {
        this.freezeTurns = Math.max(0, turns);
    }

    public boolean isFrozen() {
        return freezeTurns > 0;
    }

    public int getFreezeTurns() {
        return freezeTurns;
    }

    /** Called once at the end of each of this character's own turns. */
    public void updateStatus() {
        if (freezeTurns > 0) {
            freezeTurns--;
            if (freezeTurns == 0) {
                GameLog.log(name + " has thawed out and can move again!");
            }
        }
    }

    // ---------- abilities ----------

    public void addAbility(Ability ability) {
        if (ability == null) throw new IllegalArgumentException("Ability cannot be null");
        abilities.add(ability);
    }

    public boolean hasAbilities() {
        return !abilities.isEmpty();
    }

    public List<Ability> getAbilities() {
        return Collections.unmodifiableList(abilities);
    }

    public Optional<Ability> findAbility(Class<? extends Ability> type) {
        return abilities.stream().filter(type::isInstance).findFirst();
    }

    // ---------- weapon hooks (overridden by Player) ----------

    public boolean hasWeapon() {
        return false;
    }

    public void useWeapon(GameCharacter target) { }

    // ---------- getters / setters ----------

    public String getName() { return name; }
    public double getHealth() { return health; }
    public double getMaxHealth() { return maxHealth; }
    public double getAttackPower() { return attackPower; }
    public AttackBehavior getAttackBehavior() { return attackBehavior; }

    public void increaseAttackPower(double amount) {
        validateAttackPower(attackPower + amount);
        attackPower += amount;
    }

    /** Raises max health and heals by the same amount. */
    public void increaseMaxHealth(double amount) {
        maxHealth += amount;
        health += amount;
    }

    public void setAttackBehavior(AttackBehavior attackBehavior) {
        if (attackBehavior == null) throw new IllegalArgumentException("AttackBehavior cannot be null");
        this.attackBehavior = attackBehavior;
    }

    private static void validateMaxHealth(double value) {
        if (value <= 0 || value > 400) {
            throw new IllegalArgumentException("maxHealth must be between 1 and 400");
        }
    }

    private static void validateAttackPower(double value) {
        if (value < 0) throw new IllegalArgumentException("Attack power must be >= 0");
    }
}
