package com.androhi.androiddrawableviewer;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;

public class ImageListCellRenderer implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof JPanel) {
            Component component = (Component) value;
            component.setForeground(JBColor.white);
            component.setBackground(isSelected ? UIManager.getColor("Table.focusCellForeground") : JBColor.background());
            return component;
        } else {
            return new JLabel("");
        }
    }
}
