package symbolscounterplugin.ui.tree.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class SymbolNode extends DefaultMutableTreeNode {
    public SymbolNode(Object userData) {
        super(userData);
    }

    public abstract String getName();
}
