package ability;

import characters.GameCharacter;

public interface Ability {

    void use(GameCharacter user, GameCharacter target);

    String getName();

    /** False when the ability has run out of charges / is on cooldown. */
    default boolean isAvailable() { return true; }
}
