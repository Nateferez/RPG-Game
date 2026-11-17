package combat;

import characters.GameCharacter;
import util.GameLog;

/** Each attack in a fight hits harder than the last. */
public class ScalingAttack implements AttackBehavior {

    private static final double GROWTH_RATE = 0.10;
    private static final double MAX_FACTOR = 2.5;

    private double scalingFactor = 1.0;

    @Override
    public void attack(GameCharacter attacker, GameCharacter target) {
        double damage = scalingFactor * attacker.getAttackPower();
        GameLog.logf("%s attacks %s for %.1f damage (x%.2f)",
                attacker.getName(), target.getName(), damage, scalingFactor);
        target.takeDamage(damage);
        scalingFactor = Math.min(MAX_FACTOR, scalingFactor + GROWTH_RATE);
    }

    @Override
    public void reset() {
        scalingFactor = 1.0;
    }
}
