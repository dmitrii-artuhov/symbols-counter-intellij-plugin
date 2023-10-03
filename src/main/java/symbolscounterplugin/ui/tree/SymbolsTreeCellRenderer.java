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

public class SymbolsTreeCellRenderer extends JPanel implements TreeCellRenderer {
    private final JLabel titleLabel = new JLabel();
    private final JLabel classesCountLabel = new JLabel();
    private final JLabel methodsCountLabel = new JLabel();
    private final int spacing = 5;

    public SymbolsTreeCellRenderer() {
//        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 1));
//        titleLabel.setBorder(BorderFactory.createLineBorder(JBColor.BLACK, 1));
//        setBorder(BorderFactory.createLineBorder(JBColor.RED, 1));

//        titleLabel.setBorder(null);
//        badgeLabel.setBorder(null);
//        badgeLabel2.setBorder(null);
//        setBorder(null);
//
//        badgeLabel.setIcon(AllIcons.Nodes.Class);
//        badgeLabel.setText("12");
//
//        badgeLabel2.setIcon(AllIcons.Nodes.Method);
//        badgeLabel2.setText("1");
//
//        add(titleLabel);
//        add(badgeLabel);
//        add(badgeLabel2);

        setLayout(new GridBagLayout());

//        badgeLabel.setIcon(AllIcons.Nodes.Class);
//        badgeLabel.setText("12");
//
//        badgeLabel2.setIcon(AllIcons.Nodes.Method);
//        badgeLabel2.setText("1");

//        titleLabel.setBorder(BorderFactory.createLineBorder(JBColor.GREEN, 1));
//        badgeLabel.setBorder(BorderFactory.createLineBorder(JBColor.RED, 1));
//        badgeLabel2.setBorder(BorderFactory.createLineBorder(JBColor.YELLOW, 1));
//        setBorder(BorderFactory.createLineBorder(JBColor.YELLOW, 1));



        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.anchor = GridBagConstraints.WEST;
        leftGbc.gridx = 0;
        leftGbc.weightx = 1.0;

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.anchor = GridBagConstraints.EAST;
        rightGbc.gridx = 1;

        add(titleLabel, leftGbc);
        add(classesCountLabel, rightGbc);
        rightGbc.gridx = 2;
        add(methodsCountLabel, rightGbc);


        //GridBagConstraints gbc = new GridBagConstraints();
        // gbc.fill = GridBagConstraints.VERTICAL;

//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 3;
//        gbc.gridheight = 1;
//        gbc.fill = GridBagConstraints.VERTICAL;
//        gbc.weighty = 1.0;
//        gbc.weightx = 1.0;
//        gbc.anchor = GridBagConstraints.LINE_START;
//
//        add(titleLabel, gbc);
//
//        gbc.gridx = 1;
//        gbc.anchor = GridBagConstraints.LINE_END;
//        add(badgeLabel, gbc);

//        gbc.gridx = 2;
//        gbc.anchor = GridBagConstraints.;
//        add(badgeLabel2, gbc);
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
