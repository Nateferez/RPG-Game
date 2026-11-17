package factory;

import java.util.concurrent.ThreadLocalRandom;

import characters.Dragon;
import characters.Enemy;
import characters.Goblin;
import characters.Ogre;
import characters.Orc;
import characters.Wolf;
import characters.Zombie;
import combat.AggressiveAttack;
import combat.DefaultAttack;
import combat.ScalingAttack;

public class EnemyFactory {

    public static Enemy createEnemy(int tier) {
        return switch (tier) {
            case 1 -> randomTier1();
            case 2 -> randomTier2();
            case 3 -> randomTier3();
            default -> throw new IllegalArgumentException("Invalid tier: " + tier);
        };
    }

    private static Enemy randomTier1() {
        Enemy e = switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0 -> new Goblin(new DefaultAttack());
            case 1 -> new Orc(new DefaultAttack());
            default -> new Wolf(new DefaultAttack());
        };
        e.addAbility(AbilityFactory.create("MULTI_HIT"));
        return e;
    }

    private static Enemy randomTier2() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            Enemy ogre = new Ogre(new AggressiveAttack());
            ogre.addAbility(AbilityFactory.create("MULTI_HIT"));
            return ogre;
        }
        return new Zombie(new ScalingAttack());   // no abilities: its scaling attack is the threat
    }

    private static Enemy randomTier3() {
        Enemy dragon = new Dragon(new ScalingAttack());
        dragon.addAbility(AbilityFactory.create("FIRE"));
        return dragon;
    }
}
