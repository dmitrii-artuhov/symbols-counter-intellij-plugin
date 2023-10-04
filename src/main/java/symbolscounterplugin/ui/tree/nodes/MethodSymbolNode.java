package symbolscounterplugin.ui.tree.nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class MethodSymbolNode extends SymbolNode {
    public MethodSymbolNode(String methodName) {
        super(new MethodNodeData(methodName));
    }

    @Override
    public String getName() {
        return ((MethodSymbolNode.MethodNodeData)getUserObject()).getMethodName();
    }

    public static class MethodNodeData {
        private final String methodName;
        private final Icon classIcon = AllIcons.Nodes.Class;
        private final Icon methodIcon = AllIcons.Nodes.Method;

        private MethodNodeData(String methodName) {
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public Icon getMethodIcon() {
            return methodIcon;
        }

        public Icon getClassIcon() {
            return classIcon;
        }
    }
}