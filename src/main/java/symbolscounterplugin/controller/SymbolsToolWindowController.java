package symbolscounterplugin.controller;

import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import symbolscounterplugin.model.JavaSymbolsProvider;
import symbolscounterplugin.model.SymbolsTable;
import symbolscounterplugin.ui.tree.SymbolsTreeCellRenderer;
import symbolscounterplugin.ui.tree.SymbolsTreeModel;
import symbolscounterplugin.ui.tree.nodes.ClassSymbolNode;
import symbolscounterplugin.ui.tree.nodes.FileSymbolNode;
import symbolscounterplugin.utils.SymbolsComputeServiceSingleton;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class SymbolsToolWindowController {
    private final JScrollPane view;
    private final JavaSymbolsProvider model;

    public SymbolsToolWindowController(JavaSymbolsProvider symbolsProvider, JScrollPane scrollPane) {
        this.view = scrollPane;
        this.model = symbolsProvider;
    }

    /**
     * Posts the request for recomputing symbols and updating corresponding UI element.
     * <p>
     * Uses single-threaded pool for posting order preservation. The UI element is updated on the UI thread.
     * @param project for which to compute symbols.
     */
    public void postViewportUpdate(Project project) {
        ReadAction
            .nonBlocking(() -> {
                model.computeSymbols(project);

                return buildToolWindowContent(
                    model.getStoredSymbols()
                );
            })
            .inSmartMode(project)
            .finishOnUiThread(ModalityState.defaultModalityState(), view::setViewportView)
            .submit(SymbolsComputeServiceSingleton.getInstance());
    }

    private JTree buildToolWindowContent(SymbolsTable symbols) {
        // prepare tree
        JTree symbolsTree = new Tree();
        SymbolsTreeModel treeModel = new SymbolsTreeModel(/* empty root node */new DefaultMutableTreeNode());

        // build the tree
        for (var fileName : symbols.getFileNames()) {
            int totalClassCount = symbols.getClassCountForFile(fileName);
            int totalMethodCount = symbols.getMethodCountForFile(fileName);

            FileSymbolNode fileNode = treeModel.addFileNode(fileName, totalClassCount, totalMethodCount);

            for (var className : symbols.getClassNames(fileName)) {
                List<String> methodNames = symbols.getMethodNames(fileName, className);
                ClassSymbolNode classNode = treeModel.addClassNodeToFileNode(fileNode, className, methodNames.size());

                for (String methodName : methodNames) {
                    treeModel.addMethodNodeToClassNode(classNode, methodName);
                }
            }
        }

        // set renderer and data to the tree view
        symbolsTree.setModel(treeModel);
        symbolsTree.setCellRenderer(new SymbolsTreeCellRenderer());
        symbolsTree.setRootVisible(false);

        return symbolsTree;
    }

//    private JBList<ScrollPaneItem> buildToolWindowContent(SymbolsTable symbols) {
//        // prepare UI
//        JBList<ScrollPaneItem> symbolsList = new JBList<>();
//        symbolsList.setCellRenderer(new ScrollPaneListRenderer());
//        DefaultListModel<ScrollPaneItem> listModel = new DefaultListModel<>();
//
//        for (var symbol : symbols.getFileNames()) {
//            listModel.addElement(new ScrollPaneItem(
//                new Badge(AllIcons.Nodes.Class, String.valueOf(symbols.getClassCountForFile(symbol)), JBColor.BLUE),
//                new Badge(AllIcons.Nodes.Method, String.valueOf(symbols.getMethodCountForFile(symbol)), JBColor.PINK),
//                symbol
//            ));
//        }
//
//        symbolsList.setModel(listModel);
//        return symbolsList;
//    }
}
