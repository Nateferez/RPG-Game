package characters;

import combat.AttackBehavior;
import util.GameLog;

public class Orc extends Enemy {

    public Orc(AttackBehavior attackBehavior) {
        super("Orc", 90, 12, attackBehavior, 1);
    }

    @Override
    protected void onAttack(GameCharacter target) {
        GameLog.log("The Orc swings at " + target.getName());
    }

    @Override
    protected double abilityChance() { return 0.2; }
}
