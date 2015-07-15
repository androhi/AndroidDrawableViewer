package com.androhi.androiddrawableviewer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class AndroidDrawableViewerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = getEventProject(e);
        new AndroidDrawableViewerDialog(project).show();
    }

}
