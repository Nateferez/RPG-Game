package combat;

import characters.GameCharacter;

public interface AttackBehavior {

    void attack(GameCharacter attacker, GameCharacter target);

    /** Called at the start of each new fight (only ScalingAttack has state to clear). */
    default void reset() { }
}
