package characters;

import combat.AttackBehavior;
import util.GameLog;

public class Ogre extends Enemy {

    public Ogre(AttackBehavior attackBehavior) {
        super("Ogre", 180, 20, attackBehavior, 2);
    }

    @Override
    protected void onAttack(GameCharacter target) {
        GameLog.log("The Ogre charges at " + target.getName());
    }

    @Override
    protected double abilityChance() { return 0.3; }
}
