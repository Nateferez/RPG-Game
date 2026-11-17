package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import characters.Enemy;
import characters.Player;
import controller.BattleController;
import simulator.GameListener;
import util.GameLog;

public class GameWindow extends JFrame implements GameListener {

    private final BattleController ctrl = new BattleController();

    private final CharacterPanel playerPanel = new CharacterPanel();
    private final CharacterPanel enemyPanel = new CharacterPanel();
    private final LogPanel logPanel = new LogPanel();
    private final ActionPanel actions;

    public GameWindow() {
        super("Battle Simulator");

        actions = new ActionPanel(ctrl, this::startNewBattle);
        ctrl.setListener(this);
        GameLog.setSink(logPanel::append);

        JPanel top = new JPanel(new GridLayout(2, 1, 0, 4));
        top.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        top.add(playerPanel);
        top.add(enemyPanel);

        setLayout(new BorderLayout(0, 4));
        add(top, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(620, 640));
        setSize(660, 700);
        setLocationRelativeTo(null);
    }

    /** Call after setVisible(true). */
    public void startNewBattle() {
        ctrl.suspend();   // timers must not run behind the modal dialog

        boolean started = SetupDialog.showSetup(this, ctrl, logPanel::clear);

        if (!started) {
            if (ctrl.getPlayer() == null) {
                System.exit(0);   // cancelled on first launch
            }
            ctrl.resume();
        }
    }

    @Override
    public void onStateChanged() {
        SwingUtilities.invokeLater(() -> {
            Player p = ctrl.getPlayer();
            Enemy e = ctrl.getEnemy();
            if (p == null || e == null) return;

            playerPanel.setImagePath("/resources/images/player.png");
            enemyPanel.setImagePath(e.getImagePath());

            playerPanel.display(p, String.format("Level %d | Attack %.0f", p.getLevel(), p.getAttackPower()));
            enemyPanel.display(e, String.format("Tier %d | Attack %.0f", e.getTier(), e.getAttackPower()));

            actions.refresh(ctrl);
        });
    }

    @Override
    public void onGameOver(boolean playerWon, String message) {
        SwingUtilities.invokeLater(() -> {
            logPanel.append("\n==============================");
            logPanel.append("          GAME OVER");
            logPanel.append("==============================");

            actions.refresh(ctrl);

            int again = JOptionPane.showConfirmDialog(this,
                    message + "\n\nPlay again?",
                    playerWon ? "Victory!" : "Defeat",
                    JOptionPane.YES_NO_OPTION,
                    playerWon ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

            if (again == JOptionPane.YES_OPTION) {
                startNewBattle();
            }
        });
    }
}
