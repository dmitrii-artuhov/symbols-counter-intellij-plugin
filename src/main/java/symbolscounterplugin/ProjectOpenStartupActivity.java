package symbolscounterplugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolscounterplugin.controller.SymbolsToolWindowController;
import symbolscounterplugin.model.JavaSymbolsProvider;
import symbolscounterplugin.utils.Constants;
import symbolscounterplugin.utils.SymbolsPluginUtils;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public class ProjectOpenStartupActivity implements StartupActivity {
    private SymbolsToolWindowController controller = null;
    private static final Logger logger = LoggerFactory.getLogger(ProjectOpenStartupActivity.class);


    public void runActivity(@NotNull Project project) {
        /*
        TODO: there must be a way to properly hook to the event of registering the symbols tool window via code below (but I can't find it)
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(ToolWindowManagerListener.TOPIC, new ToolWindowManagerListener() {
            @Override
            public void toolWindowsRegistered(@NotNull List<String> ids, @NotNull ToolWindowManager toolWindowManager) {
                ToolWindowManagerListener.super.toolWindowsRegistered(ids, toolWindowManager);
            }
        });
         */

        initializeController(project, (scrollPane) -> {
            controller = new SymbolsToolWindowController(new JavaSymbolsProvider(), scrollPane);

            // run initial rendering
            controller.postViewportUpdate(project);

            // register document update callback
            project.getMessageBus().connect().subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
                @Override
                public void after(@NotNull List<? extends VFileEvent> events) {
                    boolean shouldRecomputeSymbols = false;

                    for (var fileEvent : events) {
                        VirtualFile file = fileEvent.getFile();
                        if (file == null) continue;

                        if (file.getName().endsWith(".java")) {
                            shouldRecomputeSymbols = true;
                            System.out.println("Java file changed: " + file.getPath());
                            break;
                        }
                    }

                    if (shouldRecomputeSymbols) {
                        controller.postViewportUpdate(project);
                    }
                }
            });
        });
    }

    private void initializeController(Project project, Consumer<JScrollPane> callback) {
        ToolWindow symbolsToolWindow = ToolWindowManager.getInstance(project).getToolWindow(Constants.SYMBOLS_TOOL_WINDOW_ID);

        if (symbolsToolWindow != null) {
            Content content = symbolsToolWindow.getContentManager().getContent(0);
            if (content != null) {
                JComponent panel = content.getComponent();
                JScrollPane scrollPane = SymbolsPluginUtils.findComponentByProperty(panel, Constants.SYMBOLS_TOOL_WINDOW_SCROLL_PANE_ID, Constants.UI_ID_PROPERTY);

                if (scrollPane == null) {
                    logger.error("Unable to find scroll pane to initialize symbols compute controller");
                    throw new RuntimeException("Unable to find scroll pane to draw to initialize symbols compute controller: scrollPane=" + scrollPane);
                }

                callback.accept(scrollPane);
            }
        }
    }
}