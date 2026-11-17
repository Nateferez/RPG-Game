package gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import ability.HealingAbility;
import characters.Player;
import controller.BattleController;
import controller.BattleController.Phase;

public class ActionPanel extends JPanel {

    private final JButton attackBtn = new JButton("Attack");
    private final JButton fireBtn = new JButton("Fire");
    private final JButton reloadBtn = new JButton("Reload");
    private final JButton potionBtn = new JButton("Potion");
    private final JButton healBtn = new JButton("Heal");
    private final JButton newBtn = new JButton("New Battle");

    private final JButton pauseBtn = new JButton("Pause");
    private final JSlider speedSlider = new JSlider(1, 10, 7);
    private final JLabel speedLabel = new JLabel("Speed: 7/10");

    private final JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
    private final JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));

    /** True while refresh() is moving the slider, so its listener doesn't echo back to the controller. */
    private boolean syncing = false;

    public ActionPanel(BattleController ctrl, Runnable onNewBattle) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (JButton b : new JButton[]{attackBtn, fireBtn, reloadBtn, potionBtn, healBtn}) actionRow.add(b);
        controlRow.add(pauseBtn);
        controlRow.add(speedLabel);
        controlRow.add(speedSlider);
        controlRow.add(newBtn);
        add(actionRow);
        add(controlRow);

        attackBtn.setMnemonic('A');
        fireBtn.setMnemonic('F');
        reloadBtn.setMnemonic('R');
        potionBtn.setMnemonic('P');
        healBtn.setMnemonic('H');
        newBtn.setMnemonic('N');
        attackBtn.setToolTipText("Melee attack (Alt+A)");
        fireBtn.setToolTipText("Fire your weapon (Alt+F)");
        reloadBtn.setToolTipText("Reload - costs your turn (Alt+R)");
        potionBtn.setToolTipText("Drink / throw your potion (Alt+P)");
        healBtn.setToolTipText("Restore 50% of your max HP, limited uses (Alt+H)");

        attackBtn.addActionListener(e -> ctrl.playerAttack());
        fireBtn.addActionListener(e -> ctrl.playerShoot());
        reloadBtn.addActionListener(e -> ctrl.playerReload());
        potionBtn.addActionListener(e -> ctrl.playerPotion());
        healBtn.addActionListener(e -> ctrl.playerUseAbility());
        newBtn.addActionListener(e -> onNewBattle.run());

        pauseBtn.addActionListener(e -> {
            if (ctrl.isAutoPaused()) ctrl.resumeAuto(); else ctrl.pauseAuto();
        });

        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setToolTipText("Auto-play speed");
        speedSlider.addChangeListener(e -> {
            if (syncing) return;
            int speed = speedSlider.getValue();
            ctrl.setAutoPlaySpeed(2000 - (speed - 1) * 200);
            speedLabel.setText("Speed: " + speed + "/10");
        });
    }

    public void refresh(BattleController ctrl) {
        Player p = ctrl.getPlayer();
        boolean auto = ctrl.isAutoPlay();
        boolean gameOver = ctrl.getPhase() == Phase.GAME_OVER;
        boolean myTurn = ctrl.getPhase() == Phase.PLAYER_TURN && !auto;

        actionRow.setVisible(!auto);
        pauseBtn.setVisible(auto);
        speedSlider.setVisible(auto);
        speedLabel.setVisible(auto);

        boolean has = p != null;
        attackBtn.setEnabled(myTurn && has);

        // Fire / Reload stay visible (no jumping layout), just disabled when useless
        fireBtn.setEnabled(myTurn && has && p.hasAmmo());
        reloadBtn.setEnabled(myTurn && has && p.canReload());

        potionBtn.setText(has && p.hasPotion() ? p.getPotion().getName() : "No Potion");
        potionBtn.setEnabled(myTurn && has && p.hasPotion());

        var heal = has ? p.findAbility(HealingAbility.class) : java.util.Optional.<ability.Ability>empty();
        healBtn.setVisible(heal.isPresent());
        if (heal.isPresent()) {
            HealingAbility h = (HealingAbility) heal.get();
            healBtn.setText("Heal (" + h.getCharges() + ")");
            healBtn.setEnabled(myTurn && h.isAvailable());
        }

        pauseBtn.setText(ctrl.isAutoPaused() ? "Resume" : "Pause");
        pauseBtn.setEnabled(auto && !gameOver);
        speedSlider.setEnabled(auto && !gameOver);

        int speed = Math.max(1, Math.min(10, (2000 - ctrl.getAutoPlayDelay()) / 200 + 1));
        if (speedSlider.getValue() != speed) {
            syncing = true;
            try {
                speedSlider.setValue(speed);
            } finally {
                syncing = false;
            }
        }
        speedLabel.setText("Speed: " + speed + "/10");

        revalidate();
        repaint();
    }
}
