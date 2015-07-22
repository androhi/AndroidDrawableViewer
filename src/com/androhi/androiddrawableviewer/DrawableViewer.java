package com.androhi.androiddrawableviewer;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class DrawableViewer extends SimpleToolWindowPanel {

    public static final String TOOL_WINDOW_ID = "DrawableViewer";

    private static final String DEFAULT_RESOURCE_PATH = "/app/src/main/res";
    private static final String DRAWABLE_PREFIX = "drawable-";
    private static final String DRAWABLE_HDPI = "hdpi";
    private static final String DRAWABLE_XHDPI = "xhdpi";
    private static final String DRAWABLE_XXHDPI = "xxhdpi";
    private static final String DRAWABLE_XXXHDPI = "xxxhdpi";
    private static final String PATH_SEPARATOR = "/";
    private static final String PNG_SUFFIX = ".png";
    private static final String JPEG_SUFFIX = ".jpg";

    private File[] drawableHdpiFiles;
    private File[] drawableXhdpiFiles;
    private File[] drawableXxhdpiFiles;
    private File[] drawableXxxhdpiFiles;
    private ArrayList<String> fileNameList;

    private Project project;

    public DrawableViewer(final Project project) {
        super(true, true);
        this.project = project;

        setToolbar(createToolbarPanel());
        setContent(createContentPanel());
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new EditTargetResDirAction());
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("AndroidDrawableViewer", actionGroup, true);
        return actionToolbar.getComponent();
    }

    private JScrollPane createContentPanel() {
        String projectPath = project.getBasePath();

        String resDirPath = projectPath + DEFAULT_RESOURCE_PATH;
        PluginConfig config = PluginConfig.getInstance(project);
        if (config != null) {
            String savedResDirPath = config.getResDir();
            if (savedResDirPath != null) {
                resDirPath = savedResDirPath;
            }
        }

        String hdpiPath = resDirPath + PATH_SEPARATOR + DRAWABLE_PREFIX + DRAWABLE_HDPI;
        String xhdpiPath = resDirPath + PATH_SEPARATOR + DRAWABLE_PREFIX + DRAWABLE_XHDPI;
        String xxhdpiPath = resDirPath + PATH_SEPARATOR + DRAWABLE_PREFIX + DRAWABLE_XXHDPI;
        String xxxhdpiPath = resDirPath + PATH_SEPARATOR + DRAWABLE_PREFIX + DRAWABLE_XXXHDPI;

        File hdpiDir = new File(hdpiPath);
        File xhdpiDir = new File(xhdpiPath);
        File xxhdpiDir = new File(xxhdpiPath);
        File xxxhdpiDir = new File(xxxhdpiPath);
        drawableHdpiFiles = hdpiDir.listFiles();
        drawableXhdpiFiles = xhdpiDir.listFiles();
        drawableXxhdpiFiles = xxhdpiDir.listFiles();
        drawableXxxhdpiFiles = xxxhdpiDir.listFiles();

        fileNameList = new ArrayList<String>();
        addFileList(drawableHdpiFiles);
        addFileList(drawableXhdpiFiles);
        addFileList(drawableXxhdpiFiles);
        addFileList(drawableXxxhdpiFiles);

        Vector<JPanel> panels = new Vector<JPanel>(fileNameList.size());
        for (String fileName : fileNameList) {
            String filePath = "";
            String dirName = "";
            JPanel itemPanel = new JPanel();
            JLabel iconLabel = new JLabel();
            JLabel dirLabel = new JLabel();
            GridLayout layout = new GridLayout(2, 1, 0, 4);
            itemPanel.setLayout(layout);
            itemPanel.setBorder(new EmptyBorder(10, 20, 0, 20));

            if (drawableHdpiFiles != null) {
                for (File file : drawableHdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        dirName += DRAWABLE_HDPI;
                        filePath = file.getPath();
                    }
                }
            }
            if (drawableXhdpiFiles != null) {
                for (File file : drawableXhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (dirName.length() > 0) dirName += " / ";
                        dirName += DRAWABLE_XHDPI;
                        filePath = file.getPath();
                    }
                }
            }
            if (drawableXxhdpiFiles != null) {
                for (File file : drawableXxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (dirName.length() > 0) dirName += " / ";
                        dirName += DRAWABLE_XXHDPI;
                        filePath = file.getPath();
                    }
                }
            }
            if (drawableXxxhdpiFiles != null) {
                for (File file : drawableXxxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (dirName.length() > 0) dirName += " / ";
                        dirName += DRAWABLE_XXXHDPI;
                        filePath = file.getPath();
                    }
                }
            }

            // create row of the list
            Icon icon = createIcon(filePath);
            if (icon != null) {
                // set image and name
                iconLabel.setIcon(icon);
                iconLabel.setText(fileName);
                iconLabel.setHorizontalAlignment(JLabel.LEFT);
                iconLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                iconLabel.setIconTextGap(12);
                itemPanel.add(iconLabel);

                // set name of the directory
                dirLabel.setText(dirName);
                dirLabel.setHorizontalTextPosition(JLabel.LEFT);
                dirLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
                itemPanel.add(dirLabel);

                panels.add(itemPanel);
            }
        }

        // create list
        JBList itemList = new JBList(panels);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setLayoutOrientation(JList.VERTICAL);
        itemList.setCellRenderer(new ImageListCellRenderer());

        return ScrollPaneFactory.createScrollPane(itemList);
    }

    private void addFileList(File[] files) {
        if (files == null) return;
        for (File file : files) {
            String fileName = file.getName();
            if (!isImageFile(fileName)) {
                continue;
            }
            if (isSameFile(fileName)) {
                continue;
            }
            fileNameList.add(fileName);
        }
    }

    private boolean isImageFile(String name) {
        return (name.endsWith(PNG_SUFFIX) || name.endsWith(JPEG_SUFFIX));
    }

    private boolean isSameFile(String name) {
        for (String fileName : fileNameList) {
            if (fileName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Icon createIcon(String iconFile) {
        File imageFile = new File(iconFile);
        ImageIcon icon = null;
        try {
            Image image = ImageIO.read(imageFile);
            Image resizedImage = image.getScaledInstance(24, 24, Image.SCALE_DEFAULT);
            icon = new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }
}
