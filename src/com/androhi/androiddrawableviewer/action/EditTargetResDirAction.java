package com.androhi.androiddrawableviewer.action;

import com.androhi.androiddrawableviewer.form.SettingsDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class EditTargetResDirAction extends AnAction {

    public EditTargetResDirAction() {
        super("Edit directory", "Edit target resource directory", AllIcons.General.Settings);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        SettingsDialog dialog = new SettingsDialog(project);
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}
