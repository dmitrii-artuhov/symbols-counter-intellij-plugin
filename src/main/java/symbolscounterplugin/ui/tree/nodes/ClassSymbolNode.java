package symbolscounterplugin.ui.tree.nodes;


import com.intellij.icons.AllIcons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ClassSymbolNode extends SymbolNode {
    public ClassSymbolNode(String className, int methodCount) {
        super(new ClassNodeData(className, methodCount));
    }

    @Override
    public String getName() {
        return ((ClassSymbolNode.ClassNodeData)getUserObject()).getClassName();
    }

    public static class ClassNodeData {
        private final String className;
        private final int methodsCount;
        private final Icon classIcon = AllIcons.Nodes.Class;
        private final Icon methodIcon = AllIcons.Nodes.Method;

        private ClassNodeData(String className, int methodCount) {
            this.className = className;
            this.methodsCount = methodCount;
        }

        public String getClassName() {
            return className;
        }

        public int getMethodCount() {
            return methodsCount;
        }

        public Icon getMethodIcon() {
            return methodIcon;
        }

        public Icon getClassIcon() {
            return classIcon;
        }
    }
}
