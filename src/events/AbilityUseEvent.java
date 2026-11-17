package events;

import ability.Ability;
import characters.GameCharacter;
import util.GameLog;

public class AbilityUseEvent implements GameEvent {

    private final GameCharacter user;
    private final GameCharacter target;
    private final Ability ability;

    public AbilityUseEvent(GameCharacter user, GameCharacter target, Ability ability) {
        this.user = user;
        this.target = target;
        this.ability = ability;
    }

    @Override
    public void execute() {
        if (!user.isAlive() || !target.isAlive()) return;
        if (skipIfFrozen(user)) return;

        if (ability.isAvailable()) {
            ability.use(user, target);
        } else {
            GameLog.logf("%s cannot use %s right now.", user.getName(), ability.getName());
        }
        user.updateStatus();
    }
}
