package com.androhi.androiddrawableviewer.form

import com.androhi.androiddrawableviewer.Constants
import com.androhi.androiddrawableviewer.PluginConfig
import com.androhi.androiddrawableviewer.action.EditTargetResDirAction
import com.androhi.androiddrawableviewer.model.DrawableModel
import com.androhi.androiddrawableviewer.utils.IconUtils
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBList
import com.intellij.util.ui.TextTransferable
import java.awt.Font
import java.awt.GridLayout
import java.awt.event.*
import java.io.File
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class DrawableViewer(private val project: Project) : SimpleToolWindowPanel(true, true), ActionListener {

    private var drawableMdpiFileList: Array<File>? = null
    private var drawableHdpiFileList: Array<File>? = null
    private var drawableXhdpiFileList: Array<File>? = null
    private var drawableXxhdpiFileList: Array<File>? = null
    private var drawableXxxhdpiFileList: Array<File>? = null
    private var mipmapMdpiFileList: Array<File>? = null
    private var mipmapHdpiFileList: Array<File>? = null
    private var mipmapXhdpiFileList: Array<File>? = null
    private var mipmapXxhdpiFileList: Array<File>? = null
    private var mipmapXxxhdpiFileList: Array<File>? = null

    private val fileNameList = mutableListOf<String>()
    private val drawableModelList = mutableListOf<DrawableModel>()
    private var items = JBList(emptyList<JPanel>())

    private var previousSelectedIndex = 0

    init {
        setToolbar(createToolbarPanel())
        setContent(createContentPanel())
    }

    private fun createToolbarPanel(): JComponent {
        val actionGroup = DefaultActionGroup().apply {
            add(EditTargetResDirAction())
        }
        return ActionManager.getInstance()
                .createActionToolbar("Android Drawable Viewer", actionGroup, true)
                .component
    }

    private fun createContentPanel(): JScrollPane {
        val pluginConfig = PluginConfig.getInstance(project)
        val resDirPath = pluginConfig.resDir ?: project.basePath + Constants.DEFAULT_RESOURCE_PATH
        val baseDrawableDirPath = resDirPath + Constants.PATH_SEPARATOR + Constants.DRAWABLE_PREFIX
        val baseMipmapDirPath = resDirPath + Constants.PATH_SEPARATOR + Constants.MIPMAP_PREFIX

        // drawable
        if (pluginConfig.isDrawableMdpi) {
            drawableMdpiFileList = getFileList(baseDrawableDirPath + Constants.DENSITY_MDPI)
            saveFileName(drawableMdpiFileList)
        }

        if (pluginConfig.isDrawableHdpi) {
            drawableHdpiFileList = getFileList(baseDrawableDirPath + Constants.DENSITY_HDPI)
            saveFileName(drawableHdpiFileList)
        }

        if (pluginConfig.isDrawableXhdpi) {
            drawableXhdpiFileList = getFileList(baseDrawableDirPath + Constants.DENSITY_XDPI)
            saveFileName(drawableXhdpiFileList)
        }

        if (pluginConfig.isDrawableXxhdpi) {
            drawableXxhdpiFileList = getFileList(baseDrawableDirPath + Constants.DENSITY_XXDPI)
            saveFileName(drawableXxhdpiFileList)
        }

        if (pluginConfig.isDrawableXxxhdpi) {
            drawableXxxhdpiFileList = getFileList(baseDrawableDirPath + Constants.DENSITY_XXXDPI)
            saveFileName(drawableXxxhdpiFileList)
        }

        // mipmap
        if (pluginConfig.isMipmapMdpi) {
            mipmapMdpiFileList = getFileList(baseMipmapDirPath + Constants.DENSITY_MDPI)
            saveFileName(mipmapMdpiFileList)
        }

        if (pluginConfig.isMipmapHdpi) {
            mipmapHdpiFileList = getFileList(baseMipmapDirPath + Constants.DENSITY_HDPI)
            saveFileName(mipmapHdpiFileList)
        }

        if (pluginConfig.isMipmapXhdpi) {
            mipmapXhdpiFileList = getFileList(baseMipmapDirPath + Constants.DENSITY_XDPI)
            saveFileName(mipmapXhdpiFileList)
        }

        if (pluginConfig.isMipmapXxhdpi) {
            mipmapXxhdpiFileList = getFileList(baseMipmapDirPath + Constants.DENSITY_XXDPI)
            saveFileName(mipmapXxhdpiFileList)
        }

        if (pluginConfig.isMipmapXxxhdpi) {
            mipmapXxxhdpiFileList = getFileList(baseMipmapDirPath + Constants.DENSITY_XXXDPI)
            saveFileName(mipmapXxxhdpiFileList)
        }

        return createScrollPane(createPanels(resDirPath))
    }

    private fun getFileList(targetDirPath: String): Array<File> {
        val targetDir = File(targetDirPath)
        return try {
            if (targetDir.exists()) {
                targetDir.listFiles()
            } else {
                emptyArray()
            }
        } catch (e: SecurityException) {
            emptyArray()
        }
    }

    private fun saveFileName(fileList: Array<File>?) =
            fileList?.map { it.name }?.filter { isImageFile(it) && isNotSavedFile(it) }?.let {
                fileNameList.addAll(it)
            }

    private fun isImageFile(fileName: String): Boolean =
            fileName.endsWith(Constants.PNG_SUFFIX) || fileName.endsWith(Constants.JPEG_SUFFIX)

    private fun isNotSavedFile(fileName: String): Boolean =
            fileNameList.find { it == fileName } == null

    private fun createPanels(resDirPath: String): Vector<JPanel> {
        var drawableDensityName = "Drawable: "
        var mipmapDensityName = "Mipmap: "

        // FIXME: I must refactor. I want to use sealed class.
        val panels = Vector<JPanel>(fileNameList.size)
        fileNameList.forEach { fileName ->
            val itemPanel = JPanel().apply {
                layout = GridLayout(3, 1, 0, 4)
                border = EmptyBorder(10, 20, 10, 20)
            }
            val model = DrawableModel(resDirPath, fileName)

            drawableMdpiFileList?.find { it.name == fileName }?.let {
                model.drawableDensityList.add(Constants.DENSITY_MDPI)
            }

            drawableHdpiFileList?.find { it.name == fileName }?.let {
                model.drawableDensityList.add(Constants.DENSITY_HDPI)
            }

            drawableXhdpiFileList?.find { it.name == fileName }?.let {
                model.drawableDensityList.add(Constants.DENSITY_XDPI)
            }

            drawableXxhdpiFileList?.find { it.name == fileName }?.let {
                model.drawableDensityList.add(Constants.DENSITY_XXDPI)
            }

            drawableXxxhdpiFileList?.find { it.name == fileName }?.let {
                model.drawableDensityList.add(Constants.DENSITY_XXXDPI)
            }

            mipmapMdpiFileList?.find { it.name == fileName }?.let {
                model.mipmapDensityList.add(Constants.DENSITY_MDPI)
            }

            mipmapHdpiFileList?.find { it.name == fileName }?.let {
                model.mipmapDensityList.add(Constants.DENSITY_HDPI)
            }

            mipmapXhdpiFileList?.find { it.name == fileName }?.let {
                model.mipmapDensityList.add(Constants.DENSITY_XDPI)
            }

            mipmapXxhdpiFileList?.find { it.name == fileName }?.let {
                model.mipmapDensityList.add(Constants.DENSITY_XXDPI)
            }

            mipmapXxxhdpiFileList?.find { it.name == fileName }?.let {
                model.mipmapDensityList.add(Constants.DENSITY_XXXDPI)
            }

            if (model.hasDrawable().not()) {
                drawableDensityName += "-"
            }

            if (model.hasMipmap().not()) {
                mipmapDensityName += "-"
            }

            IconUtils.createSmallIcon(model.getLowDensityFilePath())?.let {
                val iconLabel = JLabel().apply {
                    icon = it
                    text = fileName
                    horizontalAlignment = JLabel.LEFT
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 14)
                    iconTextGap = 12
                }
                itemPanel.add(iconLabel)

                val drawableLabel = JLabel().apply {
                    text = model.getSupportedDrawableDensityName()
                    horizontalTextPosition = JLabel.LEFT
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
                }
                itemPanel.add(drawableLabel)

                val mipmapLabel = JLabel().apply {
                    text = model.getSupportedMipmapDensityName()
                    horizontalTextPosition = JLabel.LEFT
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
                }
                itemPanel.add(mipmapLabel)

                panels.add(itemPanel)
                drawableModelList.add(model)
            }
        }

        return panels
    }

    private fun createScrollPane(panels: Vector<JPanel>): JScrollPane {
        items = JBList(panels.toMutableList())
        items.selectionMode = ListSelectionModel.SINGLE_SELECTION
        items.layoutOrientation = JList.VERTICAL
        items.cellRenderer = ImageListCellRenderer()
        items.addMouseListener(object: MouseListener {
            override fun mouseReleased(e: MouseEvent?) {
                // nothing
            }

            override fun mouseEntered(e: MouseEvent?) {
                // nothing
            }

            override fun mouseClicked(e: MouseEvent?) {
                if (items.itemsCount == 0) {
                    return
                }

                val selectedIndex = items.selectedIndex
                if (previousSelectedIndex == selectedIndex) {
                    showPopupMenu(e)
                }
                previousSelectedIndex = selectedIndex
            }

            override fun mouseExited(e: MouseEvent?) {
                // nothing
            }

            override fun mousePressed(e: MouseEvent?) {
                // nothing
            }
        })
        items.addKeyListener(object: KeyListener {
            override fun keyTyped(e: KeyEvent?) {
                // nothing
            }

            override fun keyPressed(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_ENTER) {
                    showDetailDialog()
                }
            }

            override fun keyReleased(e: KeyEvent?) {
                // nothing
            }
        })
        return ScrollPaneFactory.createScrollPane(items)
    }

    override fun actionPerformed(e: ActionEvent?) {
        val actionCommand = e?.actionCommand

        when (actionCommand) {
            MENU_ITEM_SHOW -> showDetailDialog()
            MENU_ITEM_COPY_DRAWABLE_RES -> copyDrawableId()
        }
    }

    private fun showPopupMenu(event: MouseEvent?) {
        val showMenu = JMenuItem(MENU_ITEM_SHOW).apply {
            addActionListener(this@DrawableViewer)
        }
        val copyDrawableIdMenu = JMenuItem(MENU_ITEM_COPY_DRAWABLE_RES).apply {
            addActionListener(this@DrawableViewer)
        }
        val popupMenu = JPopupMenu().apply {
            add(showMenu)
            add(copyDrawableIdMenu)
            // todo: detailクリックで詳細ビューダイアログを表示する
        }

        event?.let {
            popupMenu.show(it.component, it.x, it.y)
        }
    }

    private fun showDetailDialog() {
        items.minSelectionIndex.let { index: Int ->
            DetailDisplayDialog(project, drawableModelList[index]).show()
        }
    }

    private fun copyDrawableId() {
        items.minSelectionIndex.let { index: Int ->
            var fileName = drawableModelList[index].fileName
            val periodPosition = fileName.lastIndexOf(".")
            if (periodPosition >= 0) {
                fileName = fileName.substring(0, periodPosition)
            }
            val fileNameWithoutExtension = fileName
            CopyPasteManager.getInstance().setContents(TextTransferable("R.drawable." + fileNameWithoutExtension))
        }
    }

    companion object {
        const val MENU_ITEM_SHOW = "Show"
        const val MENU_ITEM_COPY_DRAWABLE_RES = "Copy Drawable Res"
        const val MENU_ITEM_DETAIL = "Detail"
    }
}