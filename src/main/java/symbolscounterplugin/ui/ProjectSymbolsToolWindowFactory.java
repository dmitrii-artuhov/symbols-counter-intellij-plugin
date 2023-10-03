package symbolscounterplugin.ui;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import symbolscounterplugin.utils.Constants;
import symbolscounterplugin.utils.SymbolsComputeServiceSingleton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;


public class ProjectSymbolsToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ProjectSymbolsToolWindowContent toolWindowContent = new ProjectSymbolsToolWindowContent();
        Content content = ContentFactory.getInstance().createContent(toolWindowContent.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private static class ProjectSymbolsToolWindowContent {

        private final JPanel contentPanel = new JPanel();
        private final JScrollPane scrollPane = new JBScrollPane();

        public ProjectSymbolsToolWindowContent() {
            contentPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0; // Allow horizontal resizing
            gbc.weighty = 1.0; // Allow vertical resizing
            gbc.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically

            contentPanel.setBorder(null);
            contentPanel.add(createScrollPane(), gbc);
        }

        @NotNull
        private JScrollPane createScrollPane() {
            scrollPane.putClientProperty(Constants.UI_ID_PROPERTY, Constants.SYMBOLS_TOOL_WINDOW_SCROLL_PANE_ID);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBorder(null);

            JLabel placeholder = createEmptyViewportPlaceholder();
            scrollPane.setViewportView(placeholder);
            scrollPane.getViewport().addChangeListener(e -> {
                if (scrollPane.getViewport().getView() == null) {
                    scrollPane.setViewportView(placeholder);
                }
            });

            return scrollPane;
        }

        private JLabel createEmptyViewportPlaceholder() {
            JLabel placeholderLabel = new JLabel("Nothing to show :)");
            placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
            placeholderLabel.setForeground(JBColor.GRAY);
            return placeholderLabel;
        }

        public JPanel getContentPanel() {
            return contentPanel;
        }
    }
}