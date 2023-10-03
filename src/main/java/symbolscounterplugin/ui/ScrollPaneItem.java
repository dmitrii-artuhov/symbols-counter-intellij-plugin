package symbolscounterplugin.ui;

import javax.swing.*;

public class ScrollPaneItem {
    // private final Badge badge;
    private final JLabel classCountLabel;
    private final JLabel methodCountLabel;
    private final String text;

    public ScrollPaneItem(Badge classCountLabel, Badge methodCountLabel, String text) {
        this.classCountLabel = classCountLabel;
        this.methodCountLabel = methodCountLabel;
        this.text = text;
    }

    public JLabel getClassCountLabel() {
        return classCountLabel;
    }

    public JLabel getMethodCountLabel() {
        return methodCountLabel;
    }

    public String getClassCountLabelText() {
        return classCountLabel.getText();
    }

    public Icon getClassCountLabelIcon() {
        return classCountLabel.getIcon();
    }

    public String getMethodCountLabelText() {
        return methodCountLabel.getText();
    }

    public Icon getMethodCountLabelIcon() {
        return methodCountLabel.getIcon();
    }

    public String getText() {
        return text;
    }
}
