package items;

import characters.GameCharacter;
import util.GameLog;

/** Fires a burst of up to BURST_SIZE bullets per turn. */
public class MachineGun extends Weapon {

    private static final int BURST_SIZE = 10;

    public MachineGun() {
        super("Machine Gun", 30);
    }

    @Override
    public void fire(GameCharacter user, GameCharacter target) {
        if (!shootable(target)) return;

        int shots = Math.min(getBullets(), BURST_SIZE);
        double total = 0;
        for (int i = 0; i < shots; i++) {
            total += (2.5 + Math.random() * 2.5) * powerScale(user);
        }
        consume(shots);
        target.takeDamage(total);

        GameLog.logf("%s fires a %d-round burst from the %s at %s for %.1f total damage (%d bullets left)",
                user.getName(), shots, getName(), target.getName(), total, getBullets());
    }
}
