package items;

import characters.Enemy;
import characters.GameCharacter;
import util.GameLog;

public class DeathPotion implements Potion {

    @Override public String getName() { return "Death Potion"; }
    @Override public boolean needsEnemyTarget() { return true; }

    @Override
    public boolean worthUsing(GameCharacter user, GameCharacter enemy) {
        return enemy instanceof Enemy e && !e.resistsInstantKill();
    }

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        if (!target.isAlive()) {
            GameLog.log("The potion has no effect. Target is already dead.");
            return;
        }
        GameLog.logf("%s throws a Death Potion at %s", user.getName(), target.getName());
        if (target instanceof Enemy enemy) {
            enemy.receiveInstantKill(user);
        } else {
            target.takeDamage(target.getHealth());
        }
    }
}
