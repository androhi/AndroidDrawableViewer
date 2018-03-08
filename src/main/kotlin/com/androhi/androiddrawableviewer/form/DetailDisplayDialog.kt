package com.androhi.androiddrawableviewer.form

import com.androhi.androiddrawableviewer.Constants.Companion.DRAWABLE_RESOURCE_NAME
import com.androhi.androiddrawableviewer.Constants.Companion.MIPMAP_RESOURCE_NAME
import com.androhi.androiddrawableviewer.model.DrawableModel
import com.androhi.androiddrawableviewer.utils.IconUtils
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
import javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
import javax.swing.border.EmptyBorder
import javax.swing.border.SoftBevelBorder

class DetailDisplayDialog(project: Project, drawableModel: DrawableModel) : DialogWrapper(project, true) {

    private var mainPanel: JPanel? = null
    private val subPanel: JPanel = JPanel()
    private var springLayout: SpringLayout? = null

    init {
        springLayout = SpringLayout()

        subPanel.run {
            layout = springLayout
        }

        isAutoAdjustable = false
        setResizable(true)
        setSize(480, 360)

        title = drawableModel.fileName

        createContent(drawableModel)
        init()
    }

    private fun createContent(model: DrawableModel) {
        //addDisplayImage(model, model.fileName, Constants.DRAWABLE_PREFIX, model.drawableDensityList)
        //addDisplayImage(model, model.fileName, Constants.MIPMAP_PREFIX, model.mipmapDensityList)
        addDisplayImage(model)
    }

    // todo: すクローラブルにする。
    private fun addDisplayImage(model: DrawableModel, fileName: String = "", densityPrefix: String = "", densityList: List<String>? = null) {
        if (densityList?.isEmpty() == true) {
            return
        }

        var oldPanel: JPanel? = null
        var panelWidth = HORIZONTAL_PADDING
        var panelHeight = 0

        mainPanel?.add(createScrollPane(), BorderLayout.CENTER)

        model.filePathList.forEach { filePath ->

            val densityLabel = JLabel().apply {
                text = getDensityName(filePath)
                horizontalAlignment = JLabel.CENTER
                border = EmptyBorder(0, 0, 4, 0)
            }

            val iconLabel = JLabel().apply {
                icon = IconUtils.createOriginalIcon(filePath)
                border = SoftBevelBorder(SoftBevelBorder.LOWERED)
            }

            val panel = JPanel().apply {
                layout = BorderLayout()
                add(densityLabel, BorderLayout.PAGE_START)
                add(iconLabel, BorderLayout.CENTER)
            }

            updateContainer(panel, oldPanel)

            panelWidth += (panel.width + HORIZONTAL_PADDING)
            if (panelHeight < panel.height) {
                panelHeight = panel.height
            }

            oldPanel = panel
        }

        setContainerSize(panelWidth, panelHeight)
    }

    private fun getDensityName(filePath: String): String {
        val regex = Regex("$DRAWABLE_RESOURCE_NAME|$MIPMAP_RESOURCE_NAME|-")
        return filePath.split("/")
                .find { it.startsWith(DRAWABLE_RESOURCE_NAME) || it.startsWith(MIPMAP_RESOURCE_NAME) }
                ?.replace(regex, "")
                ?: ""
    }

    private fun createScrollPane(): JBScrollPane =
            JBScrollPane(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS).apply {
                setViewportView(subPanel)
            }

    private fun updateContainer(newPanel: JPanel, oldPanel: JPanel?) {
        val e2 = if (oldPanel == null) SpringLayout.WEST else SpringLayout.EAST
        val c2 = if (oldPanel == null) subPanel else oldPanel
        springLayout?.run {
            putConstraint(SpringLayout.NORTH, newPanel, VERTICAL_PADDING, SpringLayout.NORTH, subPanel)
            putConstraint(SpringLayout.WEST, newPanel, HORIZONTAL_PADDING, e2, c2)
        }
        subPanel.add(newPanel)
        subPanel.doLayout()
    }

    private fun setContainerSize(width: Int, height: Int) {
        subPanel.preferredSize = Dimension(width, height + VERTICAL_PADDING * 2)
    }

    override fun createCenterPanel(): JComponent? = mainPanel

    override fun createActions(): Array<Action> = Array(1) { DialogWrapperExitAction("OK", OK_EXIT_CODE) }

    companion object {
        private const val VERTICAL_PADDING = 8
        private const val HORIZONTAL_PADDING = 16
    }
}