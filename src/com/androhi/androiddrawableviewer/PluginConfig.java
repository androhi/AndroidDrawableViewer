package com.androhi.androiddrawableviewer;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "AndroidDrawableViewer",
        reloadable = true,
        storages = {
                @Storage(id = "default", file = "$PROJECT_FILE$"),
                @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/drawable_viewer_plugin.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)

public class PluginConfig implements PersistentStateComponent<PluginConfig> {

    private String resDir;
    private boolean mdpi = false;
    private boolean hdpi = false;
    private boolean xhdpi = false;
    private boolean xxhdpi = false;
    private boolean xxxhdpi = false;

    @Nullable
    @Override
    public PluginConfig getState() {
        return this;
    }

    @Override
    public void loadState(PluginConfig pluginConfig) {
        XmlSerializerUtil.copyBean(pluginConfig, this);
    }

    @Nullable
    public static PluginConfig getInstance(Project project) {
        return ServiceManager.getService(project, PluginConfig.class);
    }

    public void setResDir(String path) {
        this.resDir = path;
    }

    public String getResDir() {
        return this.resDir;
    }

    public boolean isMdpi() {
        return mdpi;
    }

    public void setMdpi(boolean mdpi) {
        this.mdpi = mdpi;
    }

    public boolean isHdpi() {
        return hdpi;
    }

    public void setHdpi(boolean hdpi) {
        this.hdpi = hdpi;
    }

    public boolean isXhdpi() {
        return xhdpi;
    }

    public void setXhdpi(boolean xhdpi) {
        this.xhdpi = xhdpi;
    }

    public boolean isXxhdpi() {
        return xxhdpi;
    }

    public void setXxhdpi(boolean xxhdpi) {
        this.xxhdpi = xxhdpi;
    }

    public boolean isXxxhdpi() {
        return xxxhdpi;
    }

    public void setXxxhdpi(boolean xxxhdpi) {
        this.xxxhdpi = xxxhdpi;
    }
}
