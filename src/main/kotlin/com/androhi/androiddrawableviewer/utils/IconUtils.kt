package com.androhi.androiddrawableviewer.utils

import java.awt.Image
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.Icon
import javax.swing.ImageIcon

class IconUtils {
    companion object {
        fun createSmallIcon(iconFilePath: String): Icon? = createIcon(iconFilePath, 24)

        fun createOriginalIcon(iconFilePath: String): Icon? = createIcon(iconFilePath)

        private fun createIcon(iconFilePath: String, size: Int = 0): Icon? {
            val imageFile = File(iconFilePath)
            return try {
                val originalImage = ImageIO.read(imageFile)
                if (size > 0) {
                    val resizedImage = originalImage.getScaledInstance(size, size, Image.SCALE_DEFAULT)
                    ImageIcon(resizedImage)
                } else {
                    ImageIcon(originalImage)
                }
            } catch (e: IOException) {
                null
            }
        }
    }
}