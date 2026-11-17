package items;

import characters.GameCharacter;
import util.GameLog;

public class FreezePotion implements Potion {

    private static final int DURATION = 2;

    @Override public String getName() { return "Freeze Potion"; }
    @Override public boolean needsEnemyTarget() { return true; }
    @Override public boolean worthUsing(GameCharacter user, GameCharacter enemy) { return !enemy.isFrozen(); }

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        target.setFreezeTurns(DURATION);
        GameLog.logf("%s froze %s for %d turns!", user.getName(), target.getName(), DURATION);
    }
}
