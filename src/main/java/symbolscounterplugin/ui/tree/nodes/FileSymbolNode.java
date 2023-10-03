package symbolscounterplugin.ui.tree.nodes;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileSymbolNode extends DefaultMutableTreeNode {
    public FileSymbolNode(String filename, int classesCount, int methodsCount) {
        super(new FileNodeData(filename, classesCount, methodsCount));
    }

    public static class FileNodeData {
        private final String fileName;
        private final int classesCount;
        private final int methodsCount;
        private final Icon classIcon = AllIcons.Nodes.Class;
        private final Icon methodIcon = AllIcons.Nodes.Method;

        private FileNodeData(String fileName, int classCount, int methodCount) {
            this.fileName = fileName;
            this.classesCount = classCount;
            this.methodsCount = methodCount;
        }

        public String getFileName() {
            return fileName;
        }

        public int getClassCount() {
            return classesCount;
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
