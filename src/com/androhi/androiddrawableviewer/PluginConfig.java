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
}
