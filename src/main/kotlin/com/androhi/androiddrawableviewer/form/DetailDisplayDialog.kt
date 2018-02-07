package com.androhi.androiddrawableviewer.form

import com.androhi.androiddrawableviewer.Constants
import com.androhi.androiddrawableviewer.model.DrawableModel
import com.androhi.androiddrawableviewer.utils.IconUtils
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.border.SoftBevelBorder

class DetailDisplayDialog(project: Project, drawableModel: DrawableModel) : DialogWrapper(project, true) {

    private var mainPanel: JPanel? = null
    private var springLayout: SpringLayout? = null

    init {
        springLayout = SpringLayout()

        mainPanel?.run {
            layout = springLayout
            minimumSize = Dimension(360, 160)
        }

        title = drawableModel.fileName

        createContent(drawableModel)
        init()
    }

    private fun createContent(model: DrawableModel) {
        addDisplayImage(model.resDirPath, model.fileName, Constants.DRAWABLE_PREFIX, model.drawableDensityList)
        addDisplayImage(model.resDirPath, model.fileName, Constants.MIPMAP_PREFIX, model.mipmapDensityList)
    }

    private fun addDisplayImage(baseDir: String, fileName: String, densityPrefix: String, densityList: List<String>) {
        if (densityList.isEmpty()) {
            return
        }

        var oldComponent: Component? = null

        densityList.forEach { density ->
            val densityLabel = JLabel().apply {
                text = density
                horizontalAlignment = JLabel.CENTER
                border = EmptyBorder(0, 0, 4, 0)
            }

            val iconLabel = JLabel().apply {
                val filePath = baseDir + Constants.PATH_SEPARATOR + densityPrefix + density + Constants.PATH_SEPARATOR + fileName
                icon = IconUtils.createOriginalIcon(filePath)
                border = SoftBevelBorder(SoftBevelBorder.LOWERED)
            }

            val panel = JPanel().apply {
                layout = BorderLayout()
                add(densityLabel, BorderLayout.PAGE_START)
                add(iconLabel, BorderLayout.CENTER)
            }

            val e2 = if (oldComponent == null) SpringLayout.WEST else SpringLayout.EAST
            val c2 = if (oldComponent == null) mainPanel else oldComponent
            springLayout?.run {
                putConstraint(SpringLayout.NORTH, panel, 8, SpringLayout.NORTH, mainPanel)
                putConstraint(SpringLayout.WEST, panel, 16, e2, c2)
            }

            mainPanel?.add(panel)
            oldComponent = panel
        }
    }

    override fun createCenterPanel(): JComponent? = mainPanel

    override fun createActions(): Array<Action> = Array(1) { DialogWrapperExitAction("OK", OK_EXIT_CODE) }
}