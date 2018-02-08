package com.androhi.androiddrawableviewer.model

import com.androhi.androiddrawableviewer.Constants

data class DrawableModel(val resDirPath: String, val fileName: String) {
    var drawableDensityList = mutableListOf<String>()
    var mipmapDensityList = mutableListOf<String>()
    var pixelSize: Int = 0
    var dataSize: Long = 0

    fun hasDrawable() = drawableDensityList.isNotEmpty()

    fun hasMipmap() = mipmapDensityList.isNotEmpty()

    fun getLowDensityFilePath(): String {
        if (hasDrawable()) {
            return resDirPath + Constants.PATH_SEPARATOR + Constants.DRAWABLE_PREFIX + drawableDensityList[0] + Constants.PATH_SEPARATOR + fileName
        }

        if (hasMipmap()) {
            return resDirPath + Constants.PATH_SEPARATOR + Constants.MIPMAP_PREFIX + mipmapDensityList[0] + Constants.PATH_SEPARATOR + fileName
        }

        return ""
    }

    fun getSupportedDrawableDensityName(): String =
            if (drawableDensityList.isEmpty()) {
                "-"
            } else {
                drawableDensityList.joinToString(" / ")
            }

    fun getSupportedMipmapDensityName(): String =
            if (mipmapDensityList.isEmpty()) {
                "-"
            } else {
                mipmapDensityList.joinToString(" / ")
            }
}