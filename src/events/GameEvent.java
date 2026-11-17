package events;

import characters.GameCharacter;
import util.GameLog;

/**
 * An action that happens on someone's turn. Events only perform their own action; they never
 * schedule the next turn (BattleSession / BattleController own the turn order).
 */
public interface GameEvent {

    void execute();

    /** Freeze check shared by all events. Returns true if the actor loses this turn. */
    default boolean skipIfFrozen(GameCharacter actor) {
        if (!actor.isFrozen()) return false;
        GameLog.logf("%s is frozen (%d turn(s) left) and cannot act!", actor.getName(), actor.getFreezeTurns());
        actor.updateStatus();
        return true;
    }
}
