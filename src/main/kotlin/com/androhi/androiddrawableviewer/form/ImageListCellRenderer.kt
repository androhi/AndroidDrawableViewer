package com.androhi.androiddrawableviewer.form

import com.intellij.ui.JBColor
import java.awt.Component
import javax.swing.*

class ImageListCellRenderer : ListCellRenderer<Any> {

    override fun getListCellRendererComponent(list: JList<out Any>?, value: Any?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component =
        when (value) {
            is JPanel -> {
                setLabelColor(value, isSelected)
                val normalColor = if (index % 2 == 0) JBColor.border() else JBColor.background()
                val selectedColor = UIManager.getColor("List.selectionBackground")
                value.apply {
                    background = if (isSelected) selectedColor else normalColor
                }
            }
            else -> {
                JLabel("")
            }
        }

    private fun setLabelColor(panel: JPanel, isSelected: Boolean) =
            panel.components.forEach {
                it.foreground = if (isSelected) {
                    UIManager.getColor("List.selectionForeground")
                } else {
                    JBColor.foreground()
                }
            }
}