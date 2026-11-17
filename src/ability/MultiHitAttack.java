package ability;

import characters.GameCharacter;
import util.GameLog;

public class MultiHitAttack implements Ability {

    private static final double DAMAGE_RATIO = 0.6;   // each hit is weaker than a normal attack

    private final int hits;

    public MultiHitAttack(int hits) {
        if (hits <= 0) throw new IllegalArgumentException("Hits must be positive");
        this.hits = hits;
    }

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        double perHit = user.getAttackPower() * DAMAGE_RATIO;
        for (int i = 0; i < hits; i++) {
            target.takeDamage(perHit);
        }
        GameLog.logf("%s strikes %s %d times for %.1f total damage",
                user.getName(), target.getName(), hits, perHit * hits);
    }

    @Override
    public String getName() { return "Flurry"; }
}
