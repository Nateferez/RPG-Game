package characters;

import combat.AttackBehavior;
import util.GameLog;

public class Dragon extends Enemy {

    private static final double RESIST_DAMAGE = 50;

    public Dragon(AttackBehavior attackBehavior) {
        super("Dragon", 320, 25, attackBehavior, 3);
    }

    @Override
    protected void onAttack(GameCharacter target) {
        GameLog.log("The Dragon rears up over " + target.getName());
    }

    @Override
    protected double abilityChance() { return 0.4; }

    @Override
    public boolean resistsInstantKill() { return true; }

    @Override
    public void receiveInstantKill(GameCharacter attacker) {
        GameLog.log("The Dragon resists instant death! The Death Potion only weakens it.");
        takeDamage(RESIST_DAMAGE);
    }
}
