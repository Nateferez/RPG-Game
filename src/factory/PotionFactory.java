package factory;

import items.DeathPotion;
import items.FreezePotion;
import items.Potion;
import items.PotionType;
import items.StrengthPotion;
import items.UnFreezePotion;

public class PotionFactory {

    public static Potion createPotion(PotionType type) {
        return switch (type) {
            case FREEZE -> new FreezePotion();
            case UNFREEZE -> new UnFreezePotion();
            case STRENGTH -> new StrengthPotion(10);
            case DEATH -> new DeathPotion();
        };
    }
}
