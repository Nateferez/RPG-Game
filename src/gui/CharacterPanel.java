package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import characters.GameCharacter;
import characters.Player;

public class CharacterPanel extends JPanel {

    private String currentImagePath;

    private final JLabel imageLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JProgressBar hpBar = new JProgressBar();
    private final JProgressBar ammoBar = new JProgressBar();
    private final JLabel statusLabel = new JLabel(" ");

    public CharacterPanel() {
        super(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        Dimension imageSize = new Dimension(100, 100);
        imageLabel.setPreferredSize(imageSize);
        imageLabel.setMinimumSize(imageSize);
        imageLabel.setMaximumSize(imageSize);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        hpBar.setStringPainted(true);
        ammoBar.setStringPainted(true);
        ammoBar.setForeground(new Color(60, 120, 210));
        statusLabel.setForeground(new Color(180, 50, 50));
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 11f));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        for (JLabel l : new JLabel[]{nameLabel, statusLabel}) l.setAlignmentX(Component.LEFT_ALIGNMENT);
        hpBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        ammoBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(nameLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(hpBar);
        info.add(Box.createVerticalStrut(4));
        info.add(ammoBar);
        info.add(Box.createVerticalStrut(4));
        info.add(statusLabel);

        add(imageLabel, BorderLayout.WEST);
        add(info, BorderLayout.CENTER);
    }

    /** Loads and scales the image only when the path actually changes. */
    public void setImagePath(String imagePath) {
        if (imagePath != null && imagePath.equals(currentImagePath)) return;
        currentImagePath = imagePath;

        URL url = imagePath == null ? null : getClass().getResource(imagePath);
        if (url == null) {
            imageLabel.setIcon(null);
            imageLabel.setText("No image");
            return;
        }
        Image scaled = new ImageIcon(url).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imageLabel.setText("");
        imageLabel.setIcon(new ImageIcon(scaled));
    }

    public void display(GameCharacter c, String extra) {
        if (c == null) return;

        nameLabel.setText(c.getName());

        int max = Math.max(1, (int) Math.round(c.getMaxHealth()));
        int hp = Math.max(0, (int) Math.round(c.getHealth()));
        double pct = (double) hp / max;

        hpBar.setMaximum(max);
        hpBar.setValue(hp);
        hpBar.setString(hp + " / " + max + " HP");
        hpBar.setForeground(pct > 0.60 ? new Color(40, 167, 69)
                : pct > 0.30 ? new Color(255, 153, 0)
                : new Color(220, 53, 69));

        if (c instanceof Player p && p.hasWeapon()) {
            var w = p.getWeapon();
            ammoBar.setVisible(true);
            ammoBar.setMaximum(w.getMaxBullets());
            ammoBar.setValue(w.getBullets());
            ammoBar.setString(w.getName() + ": " + w.getBullets() + " / " + w.getMaxBullets());
        } else {
            ammoBar.setVisible(false);
        }

        String freeze = c.isFrozen() ? "FROZEN (" + c.getFreezeTurns() + ")" : "";
        String text = ((freeze + "  " + (extra == null ? "" : extra)).trim());
        statusLabel.setText(text.isEmpty() ? " " : text);

        revalidate();
        repaint();
    }
}
