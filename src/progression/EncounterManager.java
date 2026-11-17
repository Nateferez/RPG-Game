package progression;

import characters.Enemy;
import characters.Player;
import factory.AbilityFactory;
import factory.AttackBehaviorFactory;
import factory.EnemyFactory;
import factory.PotionFactory;
import items.PotionType;
import util.GameLog;

/** Tracks which tier the player is on and hands out rewards when a new tier is reached. */
public class EncounterManager {

    public static final int MAX_TIER = 3;

    private final Player player;
    private int currentTier;

    public EncounterManager(Player player, int startingTier) {
        this.player = player;
        this.currentTier = 1;
        // Starting on a later tier still grants every earlier reward
        while (currentTier < startingTier) {
            currentTier++;
            applyTierRewards(currentTier);
        }
    }

    public int getCurrentTier() { return currentTier; }
    public int getMaxTier() { return MAX_TIER; }
    public boolean isFinalTier() { return currentTier >= MAX_TIER; }
    public boolean hasMoreEncounters() { return currentTier < MAX_TIER; }

    public Enemy createNextEnemy() {
        if (!hasMoreEncounters()) return null;
        currentTier++;
        applyTierRewards(currentTier);
        return EnemyFactory.createEnemy(currentTier);
    }

    private void applyTierRewards(int tier) {
        player.levelUp();

        switch (tier) {
            case 2 -> {
                player.setAttackBehavior(AttackBehaviorFactory.create("AGGRESSIVE"));
                GameLog.log("Reward: Aggressive attack style (hits 30% harder, but you take 10% recoil).");
            }
            case 3 -> {
                player.setAttackBehavior(AttackBehaviorFactory.create("SCALING"));
                player.addAbility(AbilityFactory.create("HEALING"));
                GameLog.log("Reward: Scaling attack style (each hit in a fight is stronger) and a Heal ability (2 uses).");
                if (!player.hasPotion()) {
                    player.equipPotion(PotionFactory.createPotion(PotionType.STRENGTH));
                    GameLog.log("Reward: a Strength Potion.");
                }
            }
            default -> { }
        }
    }
}
