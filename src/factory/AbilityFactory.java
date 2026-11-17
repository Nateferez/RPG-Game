package factory;

import ability.Ability;
import ability.FireAttack;
import ability.HealingAbility;
import ability.MultiHitAttack;

public class AbilityFactory {

    public static final int HEAL_CHARGES = 2;

    public static Ability create(String name) {
        return switch (name.toUpperCase()) {
            case "HEALING" -> new HealingAbility(HEAL_CHARGES);
            case "FIRE" -> new FireAttack();
            case "MULTI_HIT" -> new MultiHitAttack(2);
            default -> throw new IllegalArgumentException("Unknown ability: " + name);
        };
    }
}
