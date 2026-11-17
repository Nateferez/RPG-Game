package factory;

import items.HandGun;
import items.MachineGun;
import items.ShotGun;
import items.Weapon;
import items.Weapon.WeaponType;

public class WeaponFactory {

    public static Weapon createWeapon(WeaponType type) {
        return switch (type) {
            case HAND_GUN -> new HandGun();
            case SHOT_GUN -> new ShotGun();
            case MACHINE_GUN -> new MachineGun();
        };
    }
}
