package characters;

import combat.AttackBehavior;
import util.GameLog;

public class Goblin extends Enemy {

    public Goblin(AttackBehavior attackBehavior) {
        super("Goblin", 80, 10, attackBehavior, 1);
    }

    @Override
    protected void onAttack(GameCharacter target) {
        GameLog.log("The Goblin lunges at " + target.getName());
    }

    @Override
    protected double abilityChance() { return 0.25; }
}
