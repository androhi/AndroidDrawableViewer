package com.androhi.androiddrawableviewer

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
        name = "AndroidDrawableViewer",
        reloadable = true,
        storages = [
            Storage(id = "default", file = "\$PROJECT_FILE\$"),
            Storage(id = "dir", file = "\$PROJECT_CONFIG_DIR\$/drawable_viewer_plugin.xml", scheme = StorageScheme.DIRECTORY_BASED)
        ]
)

class PluginConfig : PersistentStateComponent<PluginConfig> {

    @Deprecated("It will be deleted")
    var resDir: String? = null

    var srcDir: String? = null

    var isDrawableMdpi = false
    var isDrawableHdpi = false
    var isDrawableXhdpi = false
    var isDrawableXxhdpi = false
    var isDrawableXxxhdpi = false

    var isMipmapMdpi = false
    var isMipmapHdpi = false
    var isMipmapXhdpi = false
    var isMipmapXxhdpi = false
    var isMipmapXxxhdpi = false

    override fun getState(): PluginConfig? = this

    override fun loadState(pluginConfig: PluginConfig?) {
        pluginConfig?.let {
            XmlSerializerUtil.copyBean(it, this)
        }
    }

    fun isAvailableDrawable(dirName: String): Boolean = when {
            dirName.contains("-mdpi", false) -> isDrawableMdpi
            dirName.contains("-hdpi", false) -> isDrawableHdpi
            dirName.contains("-xhdpi", false) -> isDrawableXhdpi
            dirName.contains("-xxhdpi", false) -> isDrawableXxhdpi
            dirName.contains("-xxxhdpi", false) -> isDrawableXxxhdpi
            else -> true
    }

    fun isAvailableMipmap(dirName: String): Boolean = when {
        dirName.contains("-mdpi", false) -> isMipmapMdpi
        dirName.contains("-hdpi", false) -> isMipmapHdpi
        dirName.contains("-xhdpi", false) -> isMipmapXhdpi
        dirName.contains("-xxhdpi", false) -> isMipmapXxhdpi
        dirName.contains("-xxxhdpi", false) -> isMipmapXxxhdpi
        else -> true
    }

    companion object {
        fun getInstance(project: Project): PluginConfig = ServiceManager.getService(project, PluginConfig::class.java)
    }
}