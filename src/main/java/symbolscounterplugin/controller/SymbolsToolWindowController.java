package symbolscounterplugin.controller;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import symbolscounterplugin.model.JavaSymbolsProvider;
import symbolscounterplugin.ui.Badge;
import symbolscounterplugin.ui.ScrollPaneItem;
import symbolscounterplugin.ui.ScrollPaneListRenderer;
import symbolscounterplugin.utils.SymbolsComputeServiceSingleton;

import javax.swing.*;
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
                model.computeQualifiedSymbolNames(project);

                // return new JBList<>(model.getStoredQualifiedSymbolNames().toArray(new String[0]));
                return buildToolWindowContent(
                    model.getStoredQualifiedSymbolNames()
                );
            })
            .inSmartMode(project)
            .finishOnUiThread(ModalityState.defaultModalityState(), view::setViewportView)
            .submit(SymbolsComputeServiceSingleton.getInstance());
    }

    private JBList<ScrollPaneItem> buildToolWindowContent(final List<String> symbols) {
        JBList<ScrollPaneItem> symbolsList = new JBList<>();
        symbolsList.setCellRenderer(new ScrollPaneListRenderer());
        DefaultListModel<ScrollPaneItem> listModel = new DefaultListModel<>();

        int i = 100;
        for (var symbol : symbols) {
            listModel.addElement(new ScrollPaneItem(
                new Badge(AllIcons.Nodes.Class, String.valueOf(i++), JBColor.BLUE),
                symbol
            ));
        }

        symbolsList.setModel(listModel);
        return symbolsList;
    }
}
