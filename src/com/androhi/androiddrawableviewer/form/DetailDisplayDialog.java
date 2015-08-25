package com.androhi.androiddrawableviewer.form;

import com.androhi.androiddrawableviewer.Constants;
import com.androhi.androiddrawableviewer.model.DrawableModel;
import com.androhi.androiddrawableviewer.util.IconUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.util.List;

public class DetailDisplayDialog extends DialogWrapper {

    private JPanel mainPanel;
    private SpringLayout layout;

    public DetailDisplayDialog(@Nullable Project project, DrawableModel drawableModel) {
        super(project, true);

        layout = new SpringLayout();
        mainPanel.setLayout(layout);
        mainPanel.setMinimumSize(new Dimension(360, 160));

        setTitle(drawableModel.getFileName());
        createContent(drawableModel);

        init();
    }

    private void createContent(DrawableModel model) {
        if (model == null) return;

        List<String> densityList = model.getDrawableDensityList();
        String fileName = model.getFileName();
        Component oldComponent = null;

        for (String density : densityList) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Label
            JLabel densityLabel = new JLabel();
            densityLabel.setText(density);
            densityLabel.setHorizontalAlignment(JLabel.CENTER);
            densityLabel.setBorder(new EmptyBorder(0, 0, 4, 0));

            // Image
            JLabel iconLabel = new JLabel();
            String filePath = model.getResourceDirectory() + Constants.PATH_SEPARATOR +
                    Constants.DRAWABLE_PREFIX + density + Constants.PATH_SEPARATOR + fileName;
            Icon icon = IconUtils.createOriginalIcon(filePath);
            iconLabel.setIcon(icon);
            iconLabel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

            panel.add(densityLabel, BorderLayout.PAGE_START);
            panel.add(iconLabel, BorderLayout.CENTER);

            String e2 = oldComponent == null ? SpringLayout.WEST : SpringLayout.EAST;
            Component c2 = oldComponent == null ? mainPanel : oldComponent;
            layout.putConstraint(SpringLayout.NORTH, panel, 8, SpringLayout.NORTH, mainPanel);
            layout.putConstraint(SpringLayout.WEST, panel, 16, e2, c2);
            mainPanel.add(panel);

            oldComponent = panel;
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }
}
