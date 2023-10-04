package symbolscounterplugin.model.tree;


import symbolscounterplugin.ui.tree.nodes.ClassSymbolNode;
import symbolscounterplugin.ui.tree.nodes.FileSymbolNode;
import symbolscounterplugin.ui.tree.nodes.MethodSymbolNode;
import symbolscounterplugin.ui.tree.nodes.SymbolNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.List;

public class SymbolsTreeModel extends DefaultTreeModel {
    private final DefaultMutableTreeNode root;

    public SymbolsTreeModel() {
        super(new DefaultMutableTreeNode("Project"));
        root = (DefaultMutableTreeNode) getRoot();
    }

    public FileSymbolNode addFileNode(String filename, int classesCount, int methodsCount) {
        FileSymbolNode fileNode = new FileSymbolNode(filename, classesCount, methodsCount);
        root.add(fileNode);
        nodeStructureChanged(root);
        return fileNode;
    }

    public ClassSymbolNode addClassNodeToFileNode(FileSymbolNode fileNode, String className, int methodsCount) {
        if (fileNode != null) {
            ClassSymbolNode classNode = new ClassSymbolNode(className, methodsCount);
            fileNode.add(classNode);
            nodeStructureChanged(fileNode);
            return classNode;
        }
        return null;
    }

    public void addMethodNodeToClassNode(ClassSymbolNode classNode, String methodName) {
        if (classNode != null) {
            MethodSymbolNode methodNode = new MethodSymbolNode(methodName);
            classNode.add(methodNode);
            nodeStructureChanged(classNode);
        }
    }

    // Maybe will need to implement later (eg. for testing)
    public DefaultMutableTreeNode findNode(List<String> path) {
        DefaultMutableTreeNode currentNode = root;
        if (path.isEmpty() || !root.getUserObject().equals(path.get(0))) {
            // roots don't match
            return null;
        }

        for (int i = 1; i < path.size(); ++i) {
            String nodeName = path.get(i);
            boolean foundChild = false;
            var children = currentNode.children();

            while (children.hasMoreElements() && !foundChild) {
                if (children.nextElement() instanceof SymbolNode child) {
                    if (child.getName().equals(nodeName)) {
                        currentNode = (DefaultMutableTreeNode) child;
                        foundChild = true;
                    }
                }
            }

            if (!foundChild) return null;
        }

        return currentNode;
    }
}

