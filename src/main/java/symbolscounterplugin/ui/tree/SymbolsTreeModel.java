package symbolscounterplugin.ui.tree;


import symbolscounterplugin.ui.tree.nodes.ClassSymbolNode;
import symbolscounterplugin.ui.tree.nodes.FileSymbolNode;
import symbolscounterplugin.ui.tree.nodes.MethodSymbolNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class SymbolsTreeModel extends DefaultTreeModel {
    public SymbolsTreeModel(DefaultMutableTreeNode root) {
        super(root);
    }

    public FileSymbolNode addFileNode(String filename, int classesCount, int methodsCount) {
        FileSymbolNode fileNode = new FileSymbolNode(filename, classesCount, methodsCount);
        ((DefaultMutableTreeNode) getRoot()).add(fileNode);
        nodeStructureChanged((TreeNode) getRoot());
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
//    private FileNode findFileNode(String filename) {
//        Enumeration<?> enumeration = ((DefaultMutableTreeNode) getRoot()).children();
//        while (enumeration.hasMoreElements()) {
//            FileNode fileNode = (FileNode) enumeration.nextElement();
//            if (fileNode.toString().equals(filename)) {
//                return fileNode;
//            }
//        }
//        return null;
//    }
}

