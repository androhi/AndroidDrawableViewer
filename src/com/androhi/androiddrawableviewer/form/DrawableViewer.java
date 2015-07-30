package com.androhi.androiddrawableviewer.form;

import com.androhi.androiddrawableviewer.*;
import com.androhi.androiddrawableviewer.action.EditTargetResDirAction;
import com.androhi.androiddrawableviewer.model.DrawableModel;
import com.androhi.androiddrawableviewer.util.IconUtils;
import com.intellij.designer.designSurface.feedbacks.LineMarginBorder;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.*;

public class DrawableViewer extends SimpleToolWindowPanel implements ActionListener {

    private static final String MENU_ITEM_SHOW = "Show";
    private static final String MENU_ITEM_DETAIL = "Detail";

    private File[] drawableHdpiFiles;
    private File[] drawableXhdpiFiles;
    private File[] drawableXxhdpiFiles;
    private File[] drawableXxxhdpiFiles;
    private ArrayList<String> fileNameList;
    private ArrayList<DrawableModel> drawableModelList;
    private JBList itemList;

    private Project project;

    public DrawableViewer(final Project project) {
        super(true, true);
        this.project = project;

        drawableModelList = new ArrayList<DrawableModel>();

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

        String resDirPath = projectPath + Constants.DEFAULT_RESOURCE_PATH;
        PluginConfig config = PluginConfig.getInstance(project);
        if (config != null) {
            String savedResDirPath = config.getResDir();
            if (savedResDirPath != null) {
                resDirPath = savedResDirPath;
            }
        }

        String baseDirPath = resDirPath + Constants.PATH_SEPARATOR + Constants.DRAWABLE_PREFIX;
        String hdpiPath = baseDirPath + Constants.DRAWABLE_HDPI;
        String xhdpiPath = baseDirPath + Constants.DRAWABLE_XHDPI;
        String xxhdpiPath = baseDirPath + Constants.DRAWABLE_XXHDPI;
        String xxxhdpiPath = baseDirPath + Constants.DRAWABLE_XXXHDPI;

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
            itemPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

            // create model info
            ArrayList<String> densityList = new ArrayList<String>();

            if (drawableHdpiFiles != null) {
                for (File file : drawableHdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        dirName += Constants.DRAWABLE_HDPI;
                        filePath = file.getPath();
                        densityList.add(Constants.DRAWABLE_HDPI);
                    }
                }
            }
            if (drawableXhdpiFiles != null) {
                for (File file : drawableXhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (dirName.length() > 0) dirName += " / ";
                        dirName += Constants.DRAWABLE_XHDPI;
                        filePath = file.getPath();
                        densityList.add(Constants.DRAWABLE_XHDPI);
                    }
                }
            }
            if (drawableXxhdpiFiles != null) {
                for (File file : drawableXxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (dirName.length() > 0) dirName += " / ";
                        dirName += Constants.DRAWABLE_XXHDPI;
                        filePath = file.getPath();
                        densityList.add(Constants.DRAWABLE_XXHDPI);
                    }
                }
            }
            if (drawableXxxhdpiFiles != null) {
                for (File file : drawableXxxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (dirName.length() > 0) dirName += " / ";
                        dirName += Constants.DRAWABLE_XXXHDPI;
                        filePath = file.getPath();
                        densityList.add(Constants.DRAWABLE_XXXHDPI);
                    }
                }
            }

            // create row of the list
            Icon icon = IconUtils.createSmallIcon(filePath);
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

            if (icon != null) {
                DrawableModel model = new DrawableModel(fileName, resDirPath, densityList);
                drawableModelList.add(model);
            }
        }

        // create list
        itemList = new JBList(panels);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setLayoutOrientation(JList.VERTICAL);
        itemList.setCellRenderer(new ImageListCellRenderer());
        itemList.addMouseListener(mouseListener);

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
        return (name.endsWith(Constants.PNG_SUFFIX) || name.endsWith(Constants.JPEG_SUFFIX));
    }

    private boolean isSameFile(String name) {
        for (String fileName : fileNameList) {
            if (fileName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //String cmd = e.getActionCommand();
        DrawableModel drawableModel = drawableModelList.get(itemList.getMinSelectionIndex());
        DetailDisplayDialog dialog = new DetailDisplayDialog(project, drawableModel);
        dialog.show();
    }

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem showMenu = new JMenuItem(MENU_ITEM_SHOW);
            showMenu.addActionListener(DrawableViewer.this);
            popupMenu.add(showMenu);

            // todo: detailクリックで詳細ビューダイアログを表示する

            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };
}
