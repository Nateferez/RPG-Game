package combat;

import characters.GameCharacter;
import util.GameLog;

public class DefaultAttack implements AttackBehavior {

    @Override
    public void attack(GameCharacter attacker, GameCharacter target) {
        double damage = attacker.getAttackPower() * (0.8 + Math.random() * 0.4);
        GameLog.logf("%s attacks %s for %.1f damage", attacker.getName(), target.getName(), damage);
        target.takeDamage(damage);
    }
}
