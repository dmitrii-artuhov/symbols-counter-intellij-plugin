package symbolscounterplugin.ui;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneItem {
    private final Badge badge;
    private final String text;

    public ScrollPaneItem(Badge badge, String text) {
        this.badge = badge;
        this.text = text;
    }

    public JLabel getBadges() {
//        JPanel panel = new JPanel();
//        panel.setBackground(null);
//        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // panel.setBorder(BorderFactory.createLineBorder(JBColor.GREEN, 1));
//        panel.setBorder(null);

//        panel.add(badge);
//        panel.add(badge);
        return badge;
    }

    public String getText() {
        return text;
    }
}
