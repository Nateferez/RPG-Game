package characters;

import combat.AttackBehavior;
import util.GameLog;

public class Wolf extends Enemy {

    public Wolf(AttackBehavior attackBehavior) {
        super("Wolf", 100, 15, attackBehavior, 1);
    }

    @Override
    protected void onAttack(GameCharacter target) {
        GameLog.log("The Wolf pounces on " + target.getName());
    }

    @Override
    protected double abilityChance() { return 0.25; }
}
