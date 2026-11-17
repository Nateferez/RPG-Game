package config;

import items.PotionType;
import items.Weapon.WeaponType;

public class GameConfig {

    private final String playerName;
    private final int startingTier;
    private final boolean hasWeapon;
    private final WeaponType weapon;
    private final boolean hasPotion;
    private final PotionType potion;
    private final boolean autoPlay;

    public GameConfig(String playerName, int startingTier,
                      boolean hasWeapon, WeaponType weapon,
                      boolean hasPotion, PotionType potion,
                      boolean autoPlay) {

        if (playerName == null || playerName.isBlank()) throw new IllegalArgumentException("Name required");
        if (startingTier < 1 || startingTier > 3) throw new IllegalArgumentException("Tier must be 1-3");
        if (hasWeapon && weapon == null) throw new IllegalArgumentException("Weapon type required");
        if (hasPotion && potion == null) throw new IllegalArgumentException("Potion type required");

        this.playerName = playerName.trim();
        this.startingTier = startingTier;
        this.hasWeapon = hasWeapon;
        this.weapon = weapon;
        this.hasPotion = hasPotion;
        this.potion = potion;
        this.autoPlay = autoPlay;
    }

    public String getPlayerName() { return playerName; }
    public int getStartingTier() { return startingTier; }
    public boolean hasWeapon() { return hasWeapon; }
    public WeaponType getWeapon() { return weapon; }
    public boolean hasPotion() { return hasPotion; }
    public PotionType getPotion() { return potion; }
    public boolean isAutoPlay() { return autoPlay; }
}
