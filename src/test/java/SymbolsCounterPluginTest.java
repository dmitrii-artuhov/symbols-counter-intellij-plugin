import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.junit.Assert;
import symbolscounterplugin.model.JavaSymbolsProvider;
import symbolscounterplugin.model.SymbolsTable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SymbolsCounterPluginTest extends BasePlatformTestCase {

    static private final Map<String, Map<String, List<String>>> allClassTypesProjectExpectedSymbols = new TreeMap<>(Map.of(
            "InnerAndNestedClass.java", new TreeMap<>(Map.of(
                    "InnerAndNestedClass", List.of("method1(int)"),
                    "AnotherClass", List.of(),
                    "InnerAndNestedClass.Nested", List.of("method3()", "method2()"),
                    "InnerAndNestedClass.Inner1", List.of(),
                    "InnerAndNestedClass.Inner1.Inner11", List.of(),
                    "InnerAndNestedClass.Inner1.Nested12", List.of("nestedMethod(Inner1)"),
                    "InnerAndNestedClass.Inner1.Nested12.Inner121", List.of(),
                    "InnerAndNestedClass.Inner1.Nested12.Inner121.Nested1211", List.of(),
                    "InnerAndNestedClass.Inner2", List.of(),
                    "InnerAndNestedClass.Inner3", List.of()
            )),
            "EnumClass.java", new TreeMap<>(Map.of(
                    "Size", List.of("getSize()", "main(String[])", "values()", "valueOf(String)"),
                    "SizeNoCustomMethods", List.of("values()", "valueOf(String)")
            )),
            "GenericClass.java", new TreeMap<>(Map.of(
                    "Lst", List.of("method(S, T)", "size()")
            )),
            "AnonymousAndLocalClass.java", new TreeMap<>(Map.of(
                    "AnonymousAndLocalClass", List.of("anonymous()", "local()")
            )),
            "Interface.java", new TreeMap<>(Map.of(
                    "Interface", List.of("method1()", "method2()"),
                    "InterfaceImpl", List.of("method2()")
            )),
            "Annotation.java", new TreeMap<>(Map.of(
                    "Init", List.of(),
                    "JsonSerializable", List.of(),
                    "Person", List.of("initNames()")
            ))
    ));

    static private final Map<String, Map<String, List<String>>> multipleFoldersProjectExpectedSymbols = new TreeMap<>(Map.of(
            "A.java", new TreeMap<>(Map.of(
                    "A", List.of("run(B)")
            )),
            "inner.B.java", new TreeMap<>(Map.of(
                    "inner.B", List.of()
            )),
            "inner.test.Test.java", new TreeMap<>(Map.of(
                    "inner.test.Test", List.of("test()")
            )),
            "OutOfPackage.java", new TreeMap<>(Map.of(
                    "C", List.of()
            ))
    ));

    static private final Map<String, Map<String, List<String>>> withNonJavaFiles = new TreeMap<>(Map.of(
            "Java.java", new TreeMap<>(Map.of(
                    "JJJava", List.of()
            ))
    ));

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }

    public void testPluginSymbolsComputation() {
        String projectName = "allClassTypesProject";
        Project project = getProject(projectName);

        var symbolsProvider = new JavaSymbolsProvider();
        symbolsProvider.computeSymbols(project);
        var symbols = symbolsProvider.getStoredSymbols();

        assertSymbolTableEquals(allClassTypesProjectExpectedSymbols, symbols);
    }

    public void testMultipleFoldersProject() {
        String projectName = "multipleFoldersProject";
        Project project = getProject(projectName);

        var symbolsProvider = new JavaSymbolsProvider();
        symbolsProvider.computeSymbols(project);
        var symbols = symbolsProvider.getStoredSymbols();

        assertSymbolTableEquals(multipleFoldersProjectExpectedSymbols, symbols);
    }

    public void testWithNonJavaFilesProject() {
        String projectName = "withNonJavaFiles";
        Project project = getProject(projectName);

        var symbolsProvider = new JavaSymbolsProvider();
        symbolsProvider.computeSymbols(project);
        var symbols = symbolsProvider.getStoredSymbols();

        assertSymbolTableEquals(withNonJavaFiles, symbols);
    }

    private Project getProject(String projectName) {
        String projectPath = getTestDataPath() + "/" + projectName;

        myFixture.copyDirectoryToProject(projectName, "");
        VirtualFile projectDir = LocalFileSystem.getInstance().refreshAndFindFileByPath(projectPath);
        assertNotNull("Project directory not found", projectDir);

        Project project = myFixture.getProject();
        return project;
    }

    private void assertSymbolTableEquals(Map<String, Map<String, List<String>>> expected, SymbolsTable got) {
        var expectedFileList = expected.keySet();
        var gotFileList = got.getFileNames();

        Assert.assertEquals("Same file names found", expectedFileList, gotFileList);

        for (String fileName : gotFileList) {
            var expectedClassListByFile = expected.get(fileName).keySet();
            var gotClassListByFile = got.getClassNames(fileName);

            Assert.assertEquals("Same class names found for file '" + fileName + "'", expectedClassListByFile, gotClassListByFile);

            for (String className : gotClassListByFile) {
                var expectedMethodListByClassName = expected.get(fileName).get(className);
                var gotMethodListByClassName = got.getMethodNames(fileName, className);
                Assert.assertEquals("Same method names found for class '" + fileName + ":" + className + "'", expectedMethodListByClassName, gotMethodListByClassName);
            }
        }
    }
}
