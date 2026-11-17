package characters;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ability.Ability;
import combat.AttackBehavior;

public abstract class Enemy extends GameCharacter {

    private final int tier;

    protected Enemy(String name, double health, double attackPower, AttackBehavior attackBehavior, int tier) {
        super(name, health, attackPower, attackBehavior);
        if (tier < 1 || tier > 3) throw new IllegalArgumentException("Enemy tier must be between 1 and 3.");
        this.tier = tier;
    }

    /** Plain attack. The decision "ability or attack" is made once, in GameSimulator.nextEnemyTurn. */
    public void performAttack(GameCharacter target) {
        onAttack(target);
        attack(target);
    }

    protected void onAttack(GameCharacter target) { }

    /** Chance (0..1) of using an ability on a given turn. Subclasses override to feel different. */
    protected double abilityChance() {
        return 0.15 * tier;
    }

    public boolean shouldUseAbility() {
        if (!hasAbilities()) return false;
        double chance = abilityChance();
        if (getHealth() < 0.3 * getMaxHealth()) chance *= 2;   // desperate
        return ThreadLocalRandom.current().nextDouble() < chance;
    }

    public Ability selectAbility() {
        List<Ability> list = getAbilities();
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public boolean resistsInstantKill() { return false; }

    public void receiveInstantKill(GameCharacter attacker) {
        takeDamage(getHealth());
    }

    public int getTier() { return tier; }

    public String getImagePath() {
        return "/resources/images/" + getName().toLowerCase() + ".jpg";
    }
}
