package items;

import characters.GameCharacter;
import util.GameLog;

public class ShotGun extends Weapon {

    private static final double HIT_CHANCE = 0.70;

    public ShotGun() {
        super("Shot Gun", 4);
    }

    @Override
    public void fire(GameCharacter user, GameCharacter target) {
        if (!shootable(target)) return;
        consume(1);

        if (Math.random() < HIT_CHANCE) {
            double damage = (55 + Math.random() * 30) * powerScale(user);
            target.takeDamage(damage);
            GameLog.logf("%s blasts %s with the %s for %.1f damage (%d shells left)",
                    user.getName(), target.getName(), getName(), damage, getBullets());
        } else {
            GameLog.logf("%s fires the %s at %s but misses! (%d shells left)",
                    user.getName(), getName(), target.getName(), getBullets());
        }
    }
}
