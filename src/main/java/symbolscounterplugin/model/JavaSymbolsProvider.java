package symbolscounterplugin.model;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JavaSymbolsProvider {
    private SymbolsTable symbolsTable;

    public SymbolsTable getStoredSymbols() {
        return symbolsTable;
    }

    public void computeSymbols(Project project) {
        // { fileName: { className: [ methodName1, methodName2, ... ] } }
        Map<String, Map<String, List<String>>> filesData = new TreeMap<>();

        Collection<VirtualFile> javaFiles = FileTypeIndex.getFiles(
            JavaFileType.INSTANCE,
            GlobalSearchScope.projectScope(project)
        );
        System.out.println("Found java files: " + javaFiles);

        javaFiles.forEach((virtualFile) -> {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

            if (psiFile instanceof PsiJavaFile psiJavaFile) {
                // System.out.println("file: " + getJavaFileNameWithPackage(psiJavaFile));

                Map<String, List<String>> innerMap = new TreeMap<>();
                filesData.put(getJavaFileNameWithPackage(psiJavaFile), innerMap);

                psiJavaFile.accept(new PsiRecursiveElementVisitor() {
                    @Override
                    public void visitElement(@NotNull PsiElement element) {
                        super.visitElement(element);

                        if (element instanceof PsiClass classElement) {
                            List<String> methodNames = new ArrayList<>();
                            for (PsiMethod method : classElement.getMethods()) {
                                StringBuilder methodNameBuilder = new StringBuilder();

                                methodNameBuilder.append(method.getName());
                                methodNameBuilder.append("(");


                                var parametersList = method.getParameterList().getParameters();
                                for (int i = 0; i < parametersList.length; ++i) {
                                    methodNameBuilder.append(parametersList[i].getType().getPresentableText());
                                    if (i != parametersList.length - 1) {
                                        methodNameBuilder.append(", ");
                                    }
                                }

                                methodNameBuilder.append(")");
                                methodNames.add(methodNameBuilder.toString());
                            }

                            innerMap.put(classElement.getQualifiedName(), methodNames);
                        }
                    }
                });
            }
        });

        // System.out.println("Full files data: " + filesData);
        symbolsTable = new SymbolsTable(filesData);
    }

    private String getJavaFileNameWithPackage(PsiJavaFile psiJavaFile) {
        StringBuilder filenameBuilder = new StringBuilder();

        if (psiJavaFile.getPackageName().length() != 0) {
            filenameBuilder.append(psiJavaFile.getPackageName()).append(".");
        }

        return filenameBuilder.append(psiJavaFile.getName()).toString();
    }
}
