package com.androhi.androiddrawableviewer.form

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class AndroidDrawableViewerToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val drawableViewer = DrawableViewer(project)
        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(drawableViewer, null, false)
        contentManager.addContent(content)
    }
}