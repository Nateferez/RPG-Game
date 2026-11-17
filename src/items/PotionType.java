package items;

public enum PotionType {
    FREEZE("Freeze Potion"),
    UNFREEZE("Unfreeze Potion"),
    STRENGTH("Strength Potion"),
    DEATH("Death Potion");

    private final String label;

    PotionType(String label) { this.label = label; }

    @Override
    public String toString() { return label; }
}
