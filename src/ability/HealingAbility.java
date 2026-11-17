package ability;

import characters.GameCharacter;
import util.GameLog;

/** Limited-use self heal. Always heals the user, whatever target is passed in. */
public class HealingAbility implements Ability {

    private static final double HEAL_RATIO = 0.5;

    private int charges;

    public HealingAbility(int charges) {
        this.charges = charges;
    }

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        if (!isAvailable()) {
            GameLog.log(user.getName() + " tries to heal but has no charges left!");
            return;
        }
        charges--;
        double before = user.getHealth();
        user.performHealing(user.getMaxHealth() * HEAL_RATIO);
        GameLog.logf("%s heals %.0f HP (%d charge(s) left)", user.getName(), user.getHealth() - before, charges);
    }

    public int getCharges() { return charges; }

    @Override
    public boolean isAvailable() { return charges > 0; }

    @Override
    public String getName() { return "Heal"; }
}
