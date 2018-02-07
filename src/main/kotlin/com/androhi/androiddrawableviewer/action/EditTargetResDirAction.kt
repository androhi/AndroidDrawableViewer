package com.androhi.androiddrawableviewer.action

import com.androhi.androiddrawableviewer.form.SettingsDialog
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EditTargetResDirAction : AnAction("Edit directory", "Edit target resource directory", AllIcons.General.Settings) {

    override fun actionPerformed(anActionEvent: AnActionEvent?) {
        val project = anActionEvent?.project
        project?.let {
            SettingsDialog(it).show()
        }
    }

    override fun update(e: AnActionEvent?) {
        super.update(e)
    }
}