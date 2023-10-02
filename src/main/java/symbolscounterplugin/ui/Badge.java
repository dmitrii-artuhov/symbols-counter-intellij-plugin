package symbolscounterplugin.ui;

import com.intellij.ui.JBColor;
import com.vladsch.flexmark.util.html.ui.Color;

import javax.swing.*;
import java.awt.*;

public class Badge extends JLabel {
    private final Icon icon;
    private final String text;
    private final JBColor backgroundColor;

    private final int paddingVertical = 3;
    private final int paddingHorizontal = 3;
    private final int spacing = 5;


    public Badge(Icon icon, String text, JBColor backgroundColor) {
        this.icon = icon;
        this.text = text;
        this.backgroundColor = backgroundColor;

        setIcon(icon);
        setText(text);
        setBackground(backgroundColor);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        // Set the background color
//        g.setColor(backgroundColor);
//        g.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
//
//        // Draw the icon
//        if (icon != null) {
//            icon.paintIcon(this, g, paddingHorizontal, (getHeight() - icon.getIconHeight()) / 2);
//        }
//
//        // Draw the text
//        g.setColor(Color.WHITE); // customize the text color
//        g.drawString(
//            text,
//            paddingHorizontal + (icon != null ? icon.getIconWidth() + spacing : 0),
//            (getHeight() + g.getFontMetrics().getHeight()) / 2 - paddingVertical
//        );
//    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = getFontMetrics(getFont());

        int iconWidth = (icon != null ? icon.getIconWidth(): 0);
        int iconHeight = (icon != null ? icon.getIconHeight() : 0);

        int width = iconWidth + fontMetrics.stringWidth(text) + spacing + 2 * paddingHorizontal;
        int height = Math.max(iconHeight, fontMetrics.getHeight()) + 2 * paddingVertical;

        return new Dimension(width, height);
    }
}
