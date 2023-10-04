package symbolscounterplugin.model.tree;

import symbolscounterplugin.ui.tree.nodes.ClassSymbolNode;
import symbolscounterplugin.ui.tree.nodes.FileSymbolNode;
import symbolscounterplugin.ui.tree.nodes.SymbolNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.*;

public class ExpandedTreePathsStorage {
    private final Set<List<String>> expandedPaths = new HashSet<>();

    public ExpandedTreePathsStorage() {}

    public void addExpandedPath(TreePath path) {
        var pathList = getPathList(path);
        // System.out.println("Add expanded path: " + pathList);
        if (pathList != null) {
            expandedPaths.add(pathList);
        }
    }

    public void removeExpandedPath(TreePath path) {
        var pathList = getPathList(path);
        // System.out.println("Remove expanded path: " + pathList);
        if (pathList != null) {
            expandedPaths.removeIf(list -> new HashSet<>(list).containsAll(pathList));
        }
    }

    public Set<List<String>> getExpandedPaths() {
        // System.out.println("Expand paths that are still available: " + expandedPaths);
        return Collections.unmodifiableSet(expandedPaths);
    }

    public void reset() {
        // System.out.println("Clear all expanded paths");
        expandedPaths.clear();
    }

    public void reset(Enumeration<TreePath> newExpandedPaths) {
        if (newExpandedPaths == null) return;
        expandedPaths.clear();

        var pathLists = Collections.list(newExpandedPaths).stream().map(this::getPathList).filter(Objects::nonNull).toList();
        // System.out.println("Add path lists: " + pathLists);
        expandedPaths.addAll(pathLists);
        // System.out.println("Set expanded paths to: " + expandedPaths);
    }

    private List<String> getPathList(TreePath path) {
        List<String> pathList = new ArrayList<>();
        boolean invalidPath = false;

        for (Object node : path.getPath()) {
            String nodeName = getNodeName(node);
            if (nodeName == null) {
                invalidPath = true;
                break;
            }
            else {
                pathList.add(nodeName);
            }
        }

        if (invalidPath) {
            return null;
        }
        return pathList;
    }

    private String getNodeName(Object node) {
        if (node instanceof SymbolNode) {
            return ((SymbolNode) node).getName();
        }
        else if (node instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode)node).getUserObject().toString();
        }

        return null;
    }
}
