package symbolscounterplugin.ui;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneListRenderer extends JPanel implements ListCellRenderer<ScrollPaneItem> {
    private final JLabel dataPanel = new JLabel();
    private final JLabel textLabel = new JLabel();

    public ScrollPaneListRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        dataPanel.setBackground(null);
        dataPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dataPanel.setBorder(null);
        add(dataPanel);
        add(textLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ScrollPaneItem> list, ScrollPaneItem value, int index, boolean isSelected, boolean cellHasFocus) {
        //dataPanel.removeAll();
        //dataPanel.add(value.getBadges());
        // dataPanel.setSize(value.getBadges().getSize());
        dataPanel.setIcon(value.getBadges().getIcon());
        dataPanel.setText(value.getBadges().getText());
        textLabel.setText(value.getText());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);

        return this;
    }
}
