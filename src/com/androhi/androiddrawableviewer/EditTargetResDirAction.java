package com.androhi.androiddrawableviewer;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

public class EditTargetResDirAction extends AnAction {

    public EditTargetResDirAction() {
        super("Edit directory", "Edit target resource directory", AllIcons.General.Settings);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        PluginConfig pluginConfig = PluginConfig.getInstance(project);
        if (project == null || pluginConfig == null) return;

        String savedResDir = pluginConfig.getResDir();
        if (savedResDir == null) {
            savedResDir = project.getBasePath();
        }
        VirtualFile selectDir = VirtualFileManager.getInstance().findFileByUrl(savedResDir);
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        final VirtualFile file = FileChooser.chooseFile(descriptor, project, selectDir);
        if (file != null) {
            pluginConfig.setResDir(file.getPath());
            resetContent(project);
        }
    }

    private void resetContent(Project project) {
        DrawerViewer drawerViewer = new DrawerViewer(project);
        ContentManager contentManager = ToolWindowManager.getInstance(project)
                .getToolWindow(DrawerViewer.TOOL_WINDOW_ID).getContentManager();
        Content content = contentManager.getFactory().createContent(drawerViewer, null, false);

        contentManager.removeAllContents(true);
        contentManager.addContent(content);
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
