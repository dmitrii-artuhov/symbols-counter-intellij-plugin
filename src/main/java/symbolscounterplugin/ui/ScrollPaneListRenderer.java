package symbolscounterplugin.ui;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneListRenderer extends JPanel implements ListCellRenderer<ScrollPaneItem> {
    private final JLabel classCountLabel = new JLabel();
    private final JLabel methodCountLabel = new JLabel();
    private final JLabel textLabel = new JLabel();

    public ScrollPaneListRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        classCountLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        methodCountLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        classCountLabel.setBorder(null);
        methodCountLabel.setBorder(null);

        add(textLabel);
        add(classCountLabel);
        add(methodCountLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ScrollPaneItem> list, ScrollPaneItem value, int index, boolean isSelected, boolean cellHasFocus) {
//        classCountLabel.setText(value.getClassCountLabelText());
//        classCountLabel.setIcon(value.getClassCountLabelIcon());
//
//        methodCountLabel.setText(value.getMethodCountLabelText());
//        methodCountLabel.setIcon(value.getMethodCountLabelIcon());

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
