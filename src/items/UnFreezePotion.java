package items;

import characters.GameCharacter;
import util.GameLog;

public class UnFreezePotion implements Potion {

    @Override public String getName() { return "Unfreeze Potion"; }
    @Override public boolean needsEnemyTarget() { return false; }
    @Override public boolean worthUsing(GameCharacter user, GameCharacter enemy) { return user.isFrozen(); }

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        if (target.isFrozen()) {
            target.setFreezeTurns(0);
            GameLog.logf("%s drinks an Unfreeze Potion. The ice melts!", target.getName());
        } else {
            GameLog.log("Not frozen. The potion was wasted!");
        }
    }
}
