package symbolscounterplugin.model;

import java.util.*;

public class SymbolsTable {
    private final Map<String, Map<String, List<String>>> data;

    public SymbolsTable(Map<String, Map<String, List<String>>> data) {
        this.data = data;
    }

    public Set<String> getFileNames() {
        return data.keySet();
    }

    public Set<String> getClassNames(String fileName) {
        if (!data.containsKey(fileName)) {
            return Set.of();
        }
        return data.get(fileName).keySet();
    }

    public List<String> getMethodNames(String fileName, String className) {
        if (!data.containsKey(fileName)) {
            return List.of();
        }

        var classesData = data.get(fileName);
        if (!classesData.containsKey(className)) {
            return List.of();
        }

        return Collections.unmodifiableList(classesData.get(className));
    }

    public int getClassCountForFile(String fileName) {
        if (!data.containsKey(fileName)) return 0;

        return data.get(fileName).size();
    }

    public int getMethodCountForFile(String fileName) {
        if (!data.containsKey(fileName)) return 0;
        return data.get(fileName).values().stream().mapToInt(List::size).sum();
    }
}
