package symbolscounterplugin.ui.tree;

import com.intellij.icons.AllIcons;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import symbolscounterplugin.ui.tree.nodes.ClassSymbolNode;
import symbolscounterplugin.ui.tree.nodes.FileSymbolNode;
import symbolscounterplugin.ui.tree.nodes.MethodSymbolNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SymbolsTreeCellRenderer extends JPanel implements TreeCellRenderer {
    private final JLabel titleLabel = new JLabel();
    private final JLabel classesCountLabel = new JLabel();
    private final JLabel methodsCountLabel = new JLabel();
    private final int spacing = 5;

    public SymbolsTreeCellRenderer() {
        setLayout(new GridBagLayout());

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.anchor = GridBagConstraints.WEST;
        leftGbc.gridx = 0;
        leftGbc.weightx = 1.0;
        add(titleLabel, leftGbc);

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.anchor = GridBagConstraints.EAST;
        rightGbc.gridx = 1;
        add(classesCountLabel, rightGbc);
        rightGbc.gridx = 2;
        add(methodsCountLabel, rightGbc);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        classesCountLabel.setVisible(true);
        methodsCountLabel.setVisible(true);

        if (value instanceof FileSymbolNode fileNode) {
            var data = (FileSymbolNode.FileNodeData) fileNode.getUserObject();
            titleLabel.setIcon(AllIcons.FileTypes.Java);
            titleLabel.setText(data.getFileName());

            classesCountLabel.setIcon(data.getClassIcon());
            classesCountLabel.setText(String.valueOf(data.getClassCount()));
            methodsCountLabel.setIcon(data.getMethodIcon());
            methodsCountLabel.setText(String.valueOf(data.getMethodCount()));

            titleLabel.setBorder(JBUI.Borders.emptyRight(spacing));
            classesCountLabel.setBorder(JBUI.Borders.emptyRight(spacing));
            methodsCountLabel.setBorder(JBUI.Borders.emptyRight(spacing));
        } else if (value instanceof ClassSymbolNode classNode) {
            var data = (ClassSymbolNode.ClassNodeData) classNode.getUserObject();
            titleLabel.setIcon(data.getClassIcon());
            titleLabel.setText(data.getClassName());

            classesCountLabel.setVisible(false);
            if (data.getMethodCount() == 0) {
                methodsCountLabel.setVisible(false);
            }
            else {
                methodsCountLabel.setIcon(data.getMethodIcon());
                methodsCountLabel.setText(String.valueOf(data.getMethodCount()));
            }

            titleLabel.setBorder(JBUI.Borders.emptyRight(spacing));
            methodsCountLabel.setBorder(JBUI.Borders.emptyRight(spacing));
        } else if (value instanceof MethodSymbolNode methodNode) {
            var data = (MethodSymbolNode.MethodNodeData) methodNode.getUserObject();
            titleLabel.setIcon(data.getMethodIcon());
            titleLabel.setText(data.getMethodName());

            classesCountLabel.setVisible(false);
            methodsCountLabel.setVisible(false);
        }

        setEnabled(tree.isEnabled());
        setFont(tree.getFont());
        setOpaque(true);

        return this;
    }
}
