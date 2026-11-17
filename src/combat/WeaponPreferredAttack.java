package combat;

import characters.GameCharacter;

/** Uses the weapon when it has ammo, otherwise falls back to a normal attack. */
public class WeaponPreferredAttack implements AttackBehavior {

    private final AttackBehavior fallback = new DefaultAttack();

    @Override
    public void attack(GameCharacter attacker, GameCharacter target) {
        if (attacker.hasWeapon()) {
            // Player.useWeapon silently does nothing when empty, so check through the fallback
            if (attacker instanceof characters.Player p && !p.hasAmmo()) {
                fallback.attack(attacker, target);
                return;
            }
            attacker.useWeapon(target);
        } else {
            fallback.attack(attacker, target);
        }
    }
}
