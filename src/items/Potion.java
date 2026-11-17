package items;

import characters.GameCharacter;

public interface Potion {

    String getName();

    /** True if the potion should be used on the enemy, false if it affects the user. */
    boolean needsEnemyTarget();

    /** Hint for the auto-player: is it sensible to drink this right now? */
    boolean worthUsing(GameCharacter user, GameCharacter enemy);

    void use(GameCharacter user, GameCharacter target);
}
