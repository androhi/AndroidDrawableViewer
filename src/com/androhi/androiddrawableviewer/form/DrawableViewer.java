package com.androhi.androiddrawableviewer.form;

import com.androhi.androiddrawableviewer.*;
import com.androhi.androiddrawableviewer.action.EditTargetResDirAction;
import com.androhi.androiddrawableviewer.model.DrawableModel;
import com.androhi.androiddrawableviewer.util.IconUtils;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.TextTransferable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class DrawableViewer extends SimpleToolWindowPanel implements ActionListener {

    private static final String MENU_ITEM_SHOW = "Show";
    private static final String MENU_ITEM_COPY_DRAWABLE_RES = "Copy Drawable Res";

    private static final String MENU_ITEM_DETAIL = "Detail";

    private File[] drawableMdpiFiles;
    private File[] drawableHdpiFiles;
    private File[] drawableXhdpiFiles;
    private File[] drawableXxhdpiFiles;
    private File[] drawableXxxhdpiFiles;
    private File[] mipmapMdpiFiles;
    private File[] mipmapHdpiFiles;
    private File[] mipmapXhdpiFiles;
    private File[] mipmapXxhdpiFiles;
    private File[] mipmapXxxhdpiFiles;
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

        String baseDrawableDirPath = resDirPath + Constants.PATH_SEPARATOR + Constants.DRAWABLE_PREFIX;
        boolean isDrawableMdpi = config != null && config.isDrawableMdpi();
        boolean isDrawableHdpi = config != null && config.isDrawableHdpi();
        boolean isDrawableXhdpi = config != null && config.isDrawableXhdpi();
        boolean isDrawableXxhdpi = config != null && config.isDrawableXxhdpi();
        boolean isDrawableXxxhdpi = config != null && config.isDrawableXxxhdpi();

        fileNameList = new ArrayList<String>();
        if (isDrawableMdpi) {
            String mdpiPath = baseDrawableDirPath + Constants.DENSITY_MDPI;
            File mdpiDir = new File(mdpiPath);
            drawableMdpiFiles = mdpiDir.listFiles();
            addFileList(drawableMdpiFiles);
        }
        if (isDrawableHdpi) {
            String hdpiPath = baseDrawableDirPath + Constants.DENSITY_HDPI;
            File hdpiDir = new File(hdpiPath);
            drawableHdpiFiles = hdpiDir.listFiles();
            addFileList(drawableHdpiFiles);
        }
        if (isDrawableXhdpi) {
            String xhdpiPath = baseDrawableDirPath + Constants.DENSITY_XHDPI;
            File xhdpiDir = new File(xhdpiPath);
            drawableXhdpiFiles = xhdpiDir.listFiles();
            addFileList(drawableXhdpiFiles);
        }
        if (isDrawableXxhdpi) {
            String xxhdpiPath = baseDrawableDirPath + Constants.DENSITY_XXHDPI;
            File xxhdpiDir = new File(xxhdpiPath);
            drawableXxhdpiFiles = xxhdpiDir.listFiles();
            addFileList(drawableXxhdpiFiles);
        }
        if (isDrawableXxxhdpi) {
            String xxxhdpiPath = baseDrawableDirPath + Constants.DENSITY_XXXHDPI;
            File xxxhdpiDir = new File(xxxhdpiPath);
            drawableXxxhdpiFiles = xxxhdpiDir.listFiles();
            addFileList(drawableXxxhdpiFiles);
        }

        String baseMipmapDirPath = resDirPath + Constants.PATH_SEPARATOR + Constants.MIPMAP_PREFIX;
        boolean isMipmapMdpi = config != null && config.isMipmapMdpi();
        boolean isMipmapHdpi = config != null && config.isMipmapHdpi();
        boolean isMipmapXhdpi = config != null && config.isMipmapXhdpi();
        boolean isMipmapXxhdpi = config != null && config.isMipmapXxhdpi();
        boolean isMipmapXxxhdpi = config != null && config.isMipmapXxxhdpi();

        if (isMipmapMdpi) {
            String mdpiPath = baseMipmapDirPath + Constants.DENSITY_MDPI;
            File mdpiDir = new File(mdpiPath);
            mipmapMdpiFiles = mdpiDir.listFiles();
            addFileList(mipmapMdpiFiles);
        }
        if (isMipmapHdpi) {
            String hdpiPath = baseMipmapDirPath + Constants.DENSITY_HDPI;
            File hdpiDir = new File(hdpiPath);
            mipmapHdpiFiles = hdpiDir.listFiles();
            addFileList(mipmapHdpiFiles);
        }
        if (isMipmapXhdpi) {
            String xhdpiPath = baseMipmapDirPath + Constants.DENSITY_XHDPI;
            File xhdpiDir = new File(xhdpiPath);
            mipmapXhdpiFiles = xhdpiDir.listFiles();
            addFileList(mipmapXhdpiFiles);
        }
        if (isMipmapXxhdpi) {
            String xxhdpiPath = baseMipmapDirPath + Constants.DENSITY_XXHDPI;
            File xxhdpiDir = new File(xxhdpiPath);
            mipmapXxhdpiFiles = xxhdpiDir.listFiles();
            addFileList(mipmapXxhdpiFiles);
        }
        if (isMipmapXxxhdpi) {
            String xxxhdpiPath = baseMipmapDirPath + Constants.DENSITY_XXXHDPI;
            File xxxhdpiDir = new File(xxxhdpiPath);
            mipmapXxxhdpiFiles = xxxhdpiDir.listFiles();
            addFileList(mipmapXxxhdpiFiles);
        }

        Vector<JPanel> panels = new Vector<JPanel>(fileNameList.size());
        for (String fileName : fileNameList) {
            String filePath = "";
            String drawableDensityName;
            String mipmapDensityName;
            JPanel itemPanel = new JPanel();
            JLabel iconLabel = new JLabel();
            JLabel drawableLabel = new JLabel();
            JLabel mipmapLabel = new JLabel();
            GridLayout layout = new GridLayout(3, 1, 0, 4);
            itemPanel.setLayout(layout);
            itemPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

            drawableDensityName = "Drawable: ";
            mipmapDensityName = "Mipmap: ";

            // create model info
            ArrayList<String> drawableDensityList = new ArrayList<String>();
            ArrayList<String> mipmapDensityList = new ArrayList<String>();

            if (drawableMdpiFiles != null) {
                for (File file : drawableMdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        drawableDensityName += Constants.DENSITY_MDPI;
                        filePath = file.getPath();
                        drawableDensityList.add(Constants.DENSITY_MDPI);
                    }
                }
            }
            if (drawableHdpiFiles != null) {
                for (File file : drawableHdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (drawableDensityName.contains("dpi")) drawableDensityName += " / ";
                        drawableDensityName += Constants.DENSITY_HDPI;
                        filePath = file.getPath();
                        drawableDensityList.add(Constants.DENSITY_HDPI);
                    }
                }
            }
            if (drawableXhdpiFiles != null) {
                for (File file : drawableXhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (drawableDensityName.contains("dpi")) drawableDensityName += " / ";
                        drawableDensityName += Constants.DENSITY_XHDPI;
                        filePath = file.getPath();
                        drawableDensityList.add(Constants.DENSITY_XHDPI);
                    }
                }
            }
            if (drawableXxhdpiFiles != null) {
                for (File file : drawableXxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (drawableDensityName.contains("dpi")) drawableDensityName += " / ";
                        drawableDensityName += Constants.DENSITY_XXHDPI;
                        filePath = file.getPath();
                        drawableDensityList.add(Constants.DENSITY_XXHDPI);
                    }
                }
            }
            if (drawableXxxhdpiFiles != null) {
                for (File file : drawableXxxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (drawableDensityName.contains("dpi")) drawableDensityName += " / ";
                        drawableDensityName += Constants.DENSITY_XXXHDPI;
                        filePath = file.getPath();
                        drawableDensityList.add(Constants.DENSITY_XXXHDPI);
                    }
                }
            }

            if (mipmapMdpiFiles != null) {
                for (File file : mipmapMdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        mipmapDensityName += Constants.DENSITY_MDPI;
                        filePath = file.getPath();
                        mipmapDensityList.add(Constants.DENSITY_MDPI);
                    }
                }
            }
            if (mipmapHdpiFiles != null) {
                for (File file : mipmapHdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (mipmapDensityName.contains("dpi")) mipmapDensityName += " / ";
                        mipmapDensityName += Constants.DENSITY_HDPI;
                        filePath = file.getPath();
                        mipmapDensityList.add(Constants.DENSITY_HDPI);
                    }
                }
            }
            if (mipmapXhdpiFiles != null) {
                for (File file : mipmapXhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (mipmapDensityName.contains("dpi")) mipmapDensityName += " / ";
                        mipmapDensityName += Constants.DENSITY_XHDPI;
                        filePath = file.getPath();
                        mipmapDensityList.add(Constants.DENSITY_XHDPI);
                    }
                }
            }
            if (mipmapXxhdpiFiles != null) {
                for (File file : mipmapXxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (mipmapDensityName.contains("dpi")) mipmapDensityName += " / ";
                        mipmapDensityName += Constants.DENSITY_XXHDPI;
                        filePath = file.getPath();
                        mipmapDensityList.add(Constants.DENSITY_XXHDPI);
                    }
                }
            }
            if (mipmapXxxhdpiFiles != null) {
                for (File file : mipmapXxxhdpiFiles) {
                    if (file.getName().equals(fileName)) {
                        if (mipmapDensityName.contains("dpi")) mipmapDensityName += " / ";
                        mipmapDensityName += Constants.DENSITY_XXXHDPI;
                        filePath = file.getPath();
                        mipmapDensityList.add(Constants.DENSITY_XXXHDPI);
                    }
                }
            }

            if (!drawableDensityName.contains("dpi")) drawableDensityName += "-";
            if (!mipmapDensityName.contains("dpi")) mipmapDensityName += "-";

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

                // set name of the drawable density
                drawableLabel.setText(drawableDensityName);
                drawableLabel.setHorizontalTextPosition(JLabel.LEFT);
                drawableLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
                itemPanel.add(drawableLabel);

                // set name of the mipmap density
                mipmapLabel.setText(mipmapDensityName);
                mipmapLabel.setHorizontalTextPosition(JLabel.LEFT);
                mipmapLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
                itemPanel.add(mipmapLabel);

                panels.add(itemPanel);
            }

            if (icon != null) {
                DrawableModel model = new DrawableModel(fileName, resDirPath, drawableDensityList, mipmapDensityList);
                drawableModelList.add(model);
            }
        }

        // create list
        itemList = new JBList(panels);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setLayoutOrientation(JList.VERTICAL);
        itemList.setCellRenderer(new ImageListCellRenderer());
        itemList.addMouseListener(mouseListener);
        itemList.addKeyListener(keyListener);

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

    private void showDetailDialog() {
        DrawableModel drawableModel = drawableModelList.get(itemList.getMinSelectionIndex());
        DetailDisplayDialog dialog = new DetailDisplayDialog(project, drawableModel);
        dialog.show();
    }

    private void copyDrawableId() {
        DrawableModel drawableModel = drawableModelList.get(itemList.getMinSelectionIndex());
        String fileName = drawableModel.getFileName();
        int position = fileName.lastIndexOf(".");
        if (position >= 0) {
            fileName = fileName.substring(0, position);
        }
        final String fileNameWithoutExtension = fileName;
        CopyPasteManager.getInstance().setContents(new TextTransferable("R.drawable." + fileNameWithoutExtension));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (MENU_ITEM_SHOW.equals(cmd)) {
            showDetailDialog();
        } else if (MENU_ITEM_COPY_DRAWABLE_RES.equals(cmd)) {
            copyDrawableId();
        }
    }

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (itemList.getItemsCount() == 0) return;

            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem showMenu = new JMenuItem(MENU_ITEM_SHOW);
            showMenu.addActionListener(DrawableViewer.this);
            popupMenu.add(showMenu);

            JMenuItem copyDrawableIdMenu = new JMenuItem(MENU_ITEM_COPY_DRAWABLE_RES);
            copyDrawableIdMenu.addActionListener(DrawableViewer.this);
            popupMenu.add(copyDrawableIdMenu);

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

    private KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                showDetailDialog();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    };
}
