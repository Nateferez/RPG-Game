package items;

import characters.GameCharacter;

public abstract class Weapon {

    public enum WeaponType {
        HAND_GUN("Hand Gun"), SHOT_GUN("Shot Gun"), MACHINE_GUN("Machine Gun");

        private final String label;
        WeaponType(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    /** Attack power at which weapon damage values apply unscaled (the player's starting power). */
    protected static final double BASE_POWER = 25.0;

    private final String name;
    private final int maxBullets;
    private int bullets;

    protected Weapon(String name, int maxBullets) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty");
        if (maxBullets <= 0 || maxBullets > 100) throw new IllegalArgumentException("Invalid magazine size");
        this.name = name;
        this.maxBullets = maxBullets;
        this.bullets = maxBullets;
    }

    public abstract void fire(GameCharacter owner, GameCharacter target);

    public void refill() {
        bullets = maxBullets;
    }

    /** Weapon damage grows with the owner's attack power (levels, Strength Potion). */
    protected double powerScale(GameCharacter owner) {
        return owner.getAttackPower() / BASE_POWER;
    }

    protected void consume(int amount) {
        bullets = Math.max(0, bullets - amount);
    }

    public boolean shootable(GameCharacter target) {
        return target.isAlive() && bullets > 0;
    }

    public boolean isEmpty() { return bullets <= 0; }
    public String getName() { return name; }
    public int getBullets() { return bullets; }
    public int getMaxBullets() { return maxBullets; }
}
