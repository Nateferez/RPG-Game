package ability;

import characters.GameCharacter;
import util.GameLog;

public class FireAttack implements Ability {

    private static final double BONUS_DAMAGE = 5;

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        double damage = user.getAttackPower() + BONUS_DAMAGE;
        target.takeDamage(damage);
        GameLog.logf("%s scorches %s with fire for %.1f damage", user.getName(), target.getName(), damage);
    }

    @Override
    public String getName() { return "Fire Breath"; }
}
