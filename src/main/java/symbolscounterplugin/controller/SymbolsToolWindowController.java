package symbolscounterplugin.controller;

import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import symbolscounterplugin.model.JavaSymbolsProvider;
import symbolscounterplugin.model.SymbolsTable;
import symbolscounterplugin.model.tree.ExpandedTreePathsStorage;
import symbolscounterplugin.model.tree.SymbolsTreeModel;
import symbolscounterplugin.ui.tree.SymbolsTreeCellRenderer;
import symbolscounterplugin.ui.tree.nodes.ClassSymbolNode;
import symbolscounterplugin.ui.tree.nodes.FileSymbolNode;
import symbolscounterplugin.utils.SymbolsComputeServiceSingleton;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.List;

public class SymbolsToolWindowController {
    private final JScrollPane view;
    private final JavaSymbolsProvider symbolsProvider;
    private final ExpandedTreePathsStorage expandedTreePathsStorage;

    public SymbolsToolWindowController(JavaSymbolsProvider symbolsProvider, JScrollPane scrollPane) {
        this.view = scrollPane;
        this.symbolsProvider = symbolsProvider;
        expandedTreePathsStorage = new ExpandedTreePathsStorage();
    }

    /**
     * Posts the request for recomputing symbols and updating corresponding UI element.
     * <p>
     * Uses single-threaded pool for posting order preservation. The UI element is updated on the UI thread.
     * @param project project for which to compute symbols.
     */
    public void postViewportUpdate(Project project) {
        ReadAction
            .nonBlocking(() -> {
                symbolsProvider.computeSymbols(project);

                return buildToolWindowContent(
                        symbolsProvider.getStoredSymbols()
                );
            })
            .inSmartMode(project)
            .finishOnUiThread(ModalityState.defaultModalityState(), view::setViewportView)
            .submit(SymbolsComputeServiceSingleton.getInstance());
    }

    private JTree buildToolWindowContent(SymbolsTable symbols) {
        if (symbols.getFileNames().isEmpty()) {
            System.out.println("Empty files");
            expandedTreePathsStorage.reset();
            return null;
        }

        // prepare tree
        SymbolsTreeModel treeModel = new SymbolsTreeModel();
        JTree symbolsTree = new Tree();
        symbolsTree.setModel(treeModel);

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

        // expand saved paths that are still available
        for (var expandedPath : expandedTreePathsStorage.getExpandedPaths()) {
            // paths that don't exist in the new tree will just be ignored inside JTree::expandPath(...) method
            DefaultMutableTreeNode nodeToExpand = treeModel.findNode(expandedPath);
            // System.out.println("Expand node: " + nodeToExpand);
            if (nodeToExpand != null) {
                symbolsTree.expandPath(new TreePath(nodeToExpand.getPath()));
            }
        }

        // reset expanded paths storage (use symbolsTree.getExpandedDescendants(...) on the newly created tree)
        expandedTreePathsStorage.reset(symbolsTree.getExpandedDescendants(new TreePath(treeModel.getRoot())));

        // watch for the new expand/collapse events
        symbolsTree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                if (event.getPath() != null) {
                    expandedTreePathsStorage.addExpandedPath(event.getPath());
                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                if (event.getPath() != null) {
                    expandedTreePathsStorage.removeExpandedPath(event.getPath());
                }
            }
        });


        // set renderer and data to the tree view
        symbolsTree.setCellRenderer(new SymbolsTreeCellRenderer());
        symbolsTree.setRootVisible(false);

        return symbolsTree;
    }
}
