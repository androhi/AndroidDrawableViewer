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

    private boolean drawableMdpi = true;
    private boolean drawableHdpi = true;
    private boolean drawableXhdpi = true;
    private boolean drawableXxhdpi = true;
    private boolean drawableXxxhdpi = true;

    private boolean mipmapMdpi = true;
    private boolean mipmapHdpi = true;
    private boolean mipmapXhdpi = true;
    private boolean mipmapXxhdpi = true;
    private boolean mipmapXxxhdpi = true;

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

    public boolean isDrawableMdpi() {
        return drawableMdpi;
    }

    public void setDrawableMdpi(boolean drawableMdpi) {
        this.drawableMdpi = drawableMdpi;
    }

    public boolean isDrawableHdpi() {
        return drawableHdpi;
    }

    public void setDrawableHdpi(boolean drawableHdpi) {
        this.drawableHdpi = drawableHdpi;
    }

    public boolean isDrawableXhdpi() {
        return drawableXhdpi;
    }

    public void setDrawableXhdpi(boolean drawableXhdpi) {
        this.drawableXhdpi = drawableXhdpi;
    }

    public boolean isDrawableXxhdpi() {
        return drawableXxhdpi;
    }

    public void setDrawableXxhdpi(boolean drawableXxhdpi) {
        this.drawableXxhdpi = drawableXxhdpi;
    }

    public boolean isDrawableXxxhdpi() {
        return drawableXxxhdpi;
    }

    public void setDrawableXxxhdpi(boolean drawableXxxhdpi) {
        this.drawableXxxhdpi = drawableXxxhdpi;
    }

    public boolean isMipmapMdpi() {
        return mipmapMdpi;
    }

    public void setMipmapMdpi(boolean mipmapMdpi) {
        this.mipmapMdpi = mipmapMdpi;
    }

    public boolean isMipmapHdpi() {
        return mipmapHdpi;
    }

    public void setMipmapHdpi(boolean mipmapHdpi) {
        this.mipmapHdpi = mipmapHdpi;
    }

    public boolean isMipmapXhdpi() {
        return mipmapXhdpi;
    }

    public void setMipmapXhdpi(boolean mipmapXhdpi) {
        this.mipmapXhdpi = mipmapXhdpi;
    }

    public boolean isMipmapXxhdpi() {
        return mipmapXxhdpi;
    }

    public void setMipmapXxhdpi(boolean mipmapXxhdpi) {
        this.mipmapXxhdpi = mipmapXxhdpi;
    }

    public boolean isMipmapXxxhdpi() {
        return mipmapXxxhdpi;
    }

    public void setMipmapXxxhdpi(boolean mipmapXxxhdpi) {
        this.mipmapXxxhdpi = mipmapXxxhdpi;
    }
}
