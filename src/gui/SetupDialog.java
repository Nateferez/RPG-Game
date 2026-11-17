package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import config.GameConfig;
import controller.BattleController;
import items.PotionType;
import items.Weapon.WeaponType;

public class SetupDialog {

    /** Returns true if a battle was started. */
    public static boolean showSetup(Component parent, BattleController ctrl, Runnable beforeStart) {

        JTextField playerName = new JTextField("Hero", 14);
        JComboBox<Integer> tierCombo = new JComboBox<>(new Integer[]{1, 2, 3});

        JCheckBox weaponCheck = new JCheckBox("Equip Weapon", true);
        JComboBox<WeaponType> weaponCombo = new JComboBox<>(WeaponType.values());

        // The Death Potion would one-shot tiers 1 and 2, so it is not offered at the start
        PotionType[] startPotions = Arrays.stream(PotionType.values())
                .filter(t -> t != PotionType.DEATH).toArray(PotionType[]::new);
        JCheckBox potionCheck = new JCheckBox("Carry Potion", true);
        JComboBox<PotionType> potionCombo = new JComboBox<>(startPotions);

        JCheckBox autoCheck = new JCheckBox("Start in Auto-Play mode");

        weaponCheck.addActionListener(e -> weaponCombo.setEnabled(weaponCheck.isSelected()));
        potionCheck.addActionListener(e -> potionCombo.setEnabled(potionCheck.isSelected()));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(form, gbc, 0, new JLabel("Player Name:"), playerName);
        addRow(form, gbc, 1, new JLabel("Starting tier:"), tierCombo);
        addRow(form, gbc, 2, weaponCheck, weaponCombo);
        addRow(form, gbc, 3, potionCheck, potionCombo);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(autoCheck, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(parent, form, "Battle Setup",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return false;

            String name = playerName.getText().trim();
            if (!name.matches("^[\\p{L}\\p{N}][\\p{L}\\p{N}' \\-]{0,14}[\\p{L}\\p{N}]$")) {
                JOptionPane.showMessageDialog(parent,
                        "Use 2-16 characters (letters, digits, spaces, ' or -).",
                        "Invalid Name", JOptionPane.WARNING_MESSAGE);
                playerName.requestFocusInWindow();
                continue;
            }

            GameConfig config = new GameConfig(
                    name,
                    (Integer) tierCombo.getSelectedItem(),
                    weaponCheck.isSelected(),
                    weaponCheck.isSelected() ? (WeaponType) weaponCombo.getSelectedItem() : null,
                    potionCheck.isSelected(),
                    potionCheck.isSelected() ? (PotionType) potionCombo.getSelectedItem() : null,
                    autoCheck.isSelected());

            if (beforeStart != null) beforeStart.run();
            ctrl.newBattle(config);
            return true;
        }
    }

    private static void addRow(JPanel form, GridBagConstraints gbc, int row, Component left, Component right) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        gbc.gridx = 0;
        form.add(left, gbc);
        gbc.gridx = 1;
        form.add(right, gbc);
    }
}
