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
        createDrawableModelList()
        return createScrollPane(createPanels())
    }

    private fun createDrawableModelList() {
        val pluginConfig = PluginConfig.getInstance(project)
        val srcDir = pluginConfig.srcDir ?: project.basePath + Constants.DEFAULT_SOURCE_PATH
        val imageFileList = getNewFileList(srcDir, pluginConfig)
        val fileNameList = imageFileList.map { it.name }.filter { isImageFile(it) }.distinct()
        fileNameList.forEach { fileName ->
            val model = DrawableModel.create(fileName, imageFileList)
            drawableModelList.add(model)
        }
        drawableModelList.sortBy { it.fileName }
    }

    private fun isImageFile(fileName: String): Boolean =
            fileName.endsWith(Constants.PNG_SUFFIX) || fileName.endsWith(Constants.JPEG_SUFFIX)

    private fun getNewFileList(path: String, config: PluginConfig): List<File> {
        val targetDir = File(path)
        if (!targetDir.exists()) {
            return listOf()
        }

        // find each flavor drawable. for example, "app/src/main/res" or "app/src/debug/res"
        val parentDirList = targetDir.listFiles()
        val imageFileList = mutableListOf<File>()
        // search app/src directory
        parentDirList.forEach {
            // search app/src/[flavor] directory
            if (it.isDirectory) {
                val resDir = it.listFiles { dir, name -> dir.isDirectory && name == "res" }
                if (resDir.isNotEmpty()) {

                    // search app/src/[flavor]/res/drawable-[any] directory
                    val drawableDirList = resDir.first().listFiles { dir, name -> dir.isDirectory && name.startsWith("drawable") }
                    drawableDirList.forEach {
                        if (it.isDirectory && config.isAvailableDrawable(it.name)) {
                            imageFileList.addAll(it.listFiles())
                        }
                    }

                    // search app/src/[flavor]/res/mipmap-[any] directory
                    val mipmapDirList = resDir.first().listFiles { dir, name -> dir.isDirectory && name.startsWith("mipmap") }
                    mipmapDirList.forEach {
                        if (it.isDirectory && config.isAvailableMipmap(it.name)) {
                            imageFileList.addAll(it.listFiles())
                        }
                    }
                }
            }
        }

        return imageFileList
    }

    private fun createPanels(): Vector<JPanel> {
        val drawableDensityName = "Drawable: "
        val mipmapDensityName = "Mipmap: "

        val panels = Vector<JPanel>(drawableModelList.size)
        drawableModelList.forEach { model ->
            val itemPanel = JPanel().apply {
                layout = GridLayout(3, 1, 0, 4)
                border = EmptyBorder(10, 20, 10, 20)
            }

            IconUtils.createSmallIcon(model.getLowDensityFilePath())?.let {
                val iconLabel = JLabel().apply {
                    icon = it
                    text = model.fileName
                    horizontalAlignment = JLabel.LEFT
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 14)
                    iconTextGap = 12
                }
                itemPanel.add(iconLabel)

                val drawableLabel = JLabel().apply {
                    text = "$drawableDensityName${model.getSupportedDrawableDensityName()}"
                    horizontalTextPosition = JLabel.LEFT
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
                }
                itemPanel.add(drawableLabel)

                val mipmapLabel = JLabel().apply {
                    text = "$mipmapDensityName${model.getSupportedMipmapDensityName()}"
                    horizontalTextPosition = JLabel.LEFT
                    font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
                }
                itemPanel.add(mipmapLabel)

                panels.add(itemPanel)
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