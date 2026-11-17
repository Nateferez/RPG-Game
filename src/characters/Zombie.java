package characters;

import combat.AttackBehavior;
import util.GameLog;

public class Zombie extends Enemy {

    public Zombie(AttackBehavior attackBehavior) {
        super("Zombie", 190, 20, attackBehavior, 2);
    }

    @Override
    protected void onAttack(GameCharacter target) {
        GameLog.log("The Zombie shambles toward " + target.getName());
    }

    @Override
    protected double abilityChance() { return 0.25; }
}
