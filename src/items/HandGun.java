package items;

import characters.GameCharacter;
import util.GameLog;

public class HandGun extends Weapon {

    private static final double HIT_CHANCE = 0.85;

    public HandGun() {
        super("Hand Gun", 6);
    }

    @Override
    public void fire(GameCharacter user, GameCharacter target) {
        if (!shootable(target)) return;
        consume(1);

        if (Math.random() < HIT_CHANCE) {
            double damage = (28 + Math.random() * 20) * powerScale(user);
            target.takeDamage(damage);
            GameLog.logf("%s fires the %s at %s for %.1f damage (%d bullets left)",
                    user.getName(), getName(), target.getName(), damage, getBullets());
        } else {
            GameLog.logf("%s fires the %s at %s but misses! (%d bullets left)",
                    user.getName(), getName(), target.getName(), getBullets());
        }
    }
}
