package com.androhi.androiddrawableviewer.form;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class ImageListCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof JPanel) {
            setLabelColor(((JPanel) value), isSelected);
            Component component = (Component) value;
            Color normalBackground = index % 2 == 0 ? JBColor.border() : JBColor.background();
            component.setBackground(isSelected ? UIManager.getColor("List.selectionBackground") : normalBackground);
            return component;
        } else {
            return new JLabel("");
        }
    }

    private void setLabelColor(JPanel panel, boolean isSelected) {
        Component[] components = panel.getComponents();
        for (Component cmp : components) {
            cmp.setForeground(isSelected ? UIManager.getColor("List.selectionForeground") : JBColor.foreground());
        }
    }
}
