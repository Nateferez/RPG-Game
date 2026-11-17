package factory;

import combat.AggressiveAttack;
import combat.AttackBehavior;
import combat.DefaultAttack;
import combat.ScalingAttack;

public class AttackBehaviorFactory {

    public static AttackBehavior create(String name) {
        return switch (name.toUpperCase()) {
            case "DEFAULT" -> new DefaultAttack();
            case "AGGRESSIVE" -> new AggressiveAttack();
            case "SCALING" -> new ScalingAttack();
            default -> throw new IllegalArgumentException("Unknown attack behavior: " + name);
        };
    }
}
