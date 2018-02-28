package com.androhi.androiddrawableviewer.form

import com.androhi.androiddrawableviewer.Constants
import com.androhi.androiddrawableviewer.PluginConfig
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class SettingsDialog(private val project: Project?) : DialogWrapper(project, true) {

    private var mainPanel: JPanel? = null
    private var srcDirText: TextFieldWithBrowseButton? = null
    private var pluginConfig: PluginConfig? = null

    private var checkDrawableMdpi: JCheckBox? = null
    private var checkDrawableHdpi: JCheckBox? = null
    private var checkDrawableXhdpi: JCheckBox? = null
    private var checkDrawableXxhdpi: JCheckBox? = null
    private var checkDrawableXxxhdpi: JCheckBox? = null

    private var checkMipmapMdpi: JCheckBox? = null
    private var checkMipmapHdpi: JCheckBox? = null
    private var checkMipmapXhdpi: JCheckBox? = null
    private var checkMipmapXxhdpi: JCheckBox? = null
    private var checkMipmapXxxhdpi: JCheckBox? = null

    init {
        title = "Settings"
        setResizable(true)

        buildViews()
        init()
    }

    private fun buildViews() {
        if (project == null) return

        pluginConfig = PluginConfig.getInstance(project)

        var savedResDir = pluginConfig?.resDir
        if (savedResDir == null) {
            savedResDir = project.basePath + Constants.DEFAULT_SOURCE_PATH
        }
        srcDirText?.text = savedResDir

        val fileChooserDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
        val selectDir = VirtualFileManager.getInstance().findFileByUrl(savedResDir)
        selectDir?.let {
            fileChooserDescriptor.setRoots(selectDir)
        }
        srcDirText?.addBrowseFolderListener(TextBrowseFolderListener(fileChooserDescriptor, project))
        
        checkDrawableMdpi?.isSelected = pluginConfig?.isDrawableMdpi == true
        checkDrawableHdpi?.isSelected = pluginConfig?.isDrawableHdpi == true
        checkDrawableXhdpi?.isSelected = pluginConfig?.isDrawableXhdpi == true
        checkDrawableXxhdpi?.isSelected = pluginConfig?.isDrawableXxhdpi == true
        checkDrawableXxxhdpi?.isSelected = pluginConfig?.isDrawableXxxhdpi == true

        checkMipmapMdpi?.isSelected = pluginConfig?.isMipmapMdpi == true
        checkMipmapHdpi?.isSelected = pluginConfig?.isMipmapHdpi == true
        checkMipmapXhdpi?.isSelected = pluginConfig?.isMipmapXhdpi == true
        checkMipmapXxhdpi?.isSelected = pluginConfig?.isMipmapXxhdpi == true
        checkMipmapXxxhdpi?.isSelected = pluginConfig?.isMipmapXxxhdpi == true
    }

    override fun createCenterPanel(): JComponent? = mainPanel

    override fun doValidate(): ValidationInfo? {
        val srcDir = srcDirText?.text
        if (srcDir.isNullOrEmpty()) {
            return ValidationInfo("Select source directory.")
        }

        if (hasUncheckedDir()) {
            return ValidationInfo("Check any box.")
        }

        return null
    }

    private fun hasUncheckedDir(): Boolean =
            checkDrawableMdpi?.isSelected != true && checkDrawableHdpi?.isSelected != true &&
                checkDrawableXhdpi?.isSelected != true && checkDrawableXxhdpi?.isSelected != true &&
                checkDrawableXxxhdpi?.isSelected != true

    override fun doOKAction() {
        super.doOKAction()

        pluginConfig?.let {
            it.resDir = null
            it.srcDir = srcDirText?.text

            it.isDrawableMdpi = checkDrawableMdpi?.isSelected == true
            it.isDrawableHdpi = checkDrawableHdpi?.isSelected == true
            it.isDrawableXhdpi = checkDrawableXhdpi?.isSelected == true
            it.isDrawableXxhdpi = checkDrawableXxhdpi?.isSelected == true
            it.isDrawableXxxhdpi = checkDrawableXxxhdpi?.isSelected == true

            it.isMipmapMdpi = checkMipmapMdpi?.isSelected == true
            it.isMipmapHdpi = checkMipmapHdpi?.isSelected == true
            it.isMipmapXhdpi = checkMipmapXhdpi?.isSelected == true
            it.isMipmapXxhdpi = checkMipmapXxhdpi?.isSelected == true
            it.isMipmapXxxhdpi = checkMipmapXxxhdpi?.isSelected == true
        }

        resetContent()
    }

    private fun resetContent() {
        project?.let {
            val drawableViewer = DrawableViewer(it)
            ToolWindowManager.getInstance(it).getToolWindow(Constants.TOOL_WINDOW_ID).contentManager.apply {
                val content = factory.createContent(drawableViewer, null, false)
                removeAllContents(true)
                addContent(content)
            }
        }
    }
}