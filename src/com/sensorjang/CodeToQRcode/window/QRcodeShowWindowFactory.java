package com.sensorjang.CodeToQRcode.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QRcodeShowWindowFactory implements ToolWindowFactory {

    public static QRcodeShowWindow qRcodeShowWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        List<String> language_type_list = Collections.unmodifiableList(Arrays.asList("plain", "python", "cpp", "java", "bash", "markdown", "json", "go"));
        qRcodeShowWindow = new QRcodeShowWindow(project, toolWindow, language_type_list);
        ContentFactory contentFactory = toolWindow.getContentManager().getFactory();
        Content content = contentFactory.createContent(qRcodeShowWindow.getContentPanel(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
