package combat;

import characters.GameCharacter;
import util.GameLog;

/** Hits harder than normal but the attacker takes recoil damage. */
public class AggressiveAttack implements AttackBehavior {

    private static final double DAMAGE_MULTIPLIER = 1.3;
    private static final double RECOIL_RATIO = 0.10;

    @Override
    public void attack(GameCharacter attacker, GameCharacter target) {
        double damage = attacker.getAttackPower() * DAMAGE_MULTIPLIER;
        double recoil = damage * RECOIL_RATIO;
        GameLog.logf("%s attacks %s ferociously for %.1f damage (recoil %.1f)",
                attacker.getName(), target.getName(), damage, recoil);
        target.takeDamage(damage);
        attacker.takeDamage(recoil);
    }
}
