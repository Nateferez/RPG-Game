package items;

import characters.GameCharacter;
import util.GameLog;

public class StrengthPotion implements Potion {

    private final double amount;

    public StrengthPotion(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be greater than 0");
        this.amount = amount;
    }

    @Override public String getName() { return "Strength Potion"; }
    @Override public boolean needsEnemyTarget() { return false; }
    @Override public boolean worthUsing(GameCharacter user, GameCharacter enemy) { return true; }

    @Override
    public void use(GameCharacter user, GameCharacter target) {
        double old = target.getAttackPower();
        target.increaseAttackPower(amount);
        GameLog.logf("%s drinks a Strength Potion! Attack power: %.1f -> %.1f",
                target.getName(), old, target.getAttackPower());
    }

    public double getAmount() { return amount; }
}
