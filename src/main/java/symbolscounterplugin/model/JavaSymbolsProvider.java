package symbolscounterplugin.model;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JavaSymbolsProvider {
    private List<String> qualifiedSymbolNames;

    public List<String> getStoredQualifiedSymbolNames() {
        return qualifiedSymbolNames;
    }

    public void computeQualifiedSymbolNames(Project project) {
        List<String> newQualifiedSymbolNames = new ArrayList<>();

        Collection<VirtualFile> javaFiles = FileTypeIndex.getFiles(
                JavaFileType.INSTANCE,
                GlobalSearchScope.projectScope(project)
        );
        javaFiles.forEach((virtualFile) -> {
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

            if (psiFile instanceof PsiJavaFile psiJavaFile) {
                psiJavaFile.accept(new PsiRecursiveElementVisitor() {
                    @Override
                    public void visitElement(@NotNull PsiElement element) {
                        super.visitElement(element);

                        if (element instanceof PsiClass classElement) {
                            newQualifiedSymbolNames.add(classElement.getQualifiedName());
                        }
                    }
                });
            }
        });

        newQualifiedSymbolNames.sort(String::compareTo);
        qualifiedSymbolNames = newQualifiedSymbolNames;
    }
}
