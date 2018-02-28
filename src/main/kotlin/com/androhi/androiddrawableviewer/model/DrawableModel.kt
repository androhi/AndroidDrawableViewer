package com.androhi.androiddrawableviewer.model

import com.androhi.androiddrawableviewer.Constants.Companion.DRAWABLE_PREFIX
import com.androhi.androiddrawableviewer.Constants.Companion.DRAWABLE_RESOURCE_NAME
import com.androhi.androiddrawableviewer.Constants.Companion.MIPMAP_PREFIX
import com.androhi.androiddrawableviewer.Constants.Companion.MIPMAP_RESOURCE_NAME
import com.androhi.androiddrawableviewer.Constants.Companion.PATH_SEPARATOR
import java.io.File

class DrawableModel private constructor(val fileName: String, private val filePathList: List<String>) {
    var drawableDensityList = mutableListOf<String>()
    var mipmapDensityList = mutableListOf<String>()

    init {
        filePathList.forEach { filePath ->

            // generate drawable density list
            val pathArray = filePath.split(PATH_SEPARATOR)
            val drawableDirName = pathArray.find { it.startsWith(DRAWABLE_RESOURCE_NAME) }
            val drawableDensity = if (drawableDirName?.contains("-", false) == true) {
                drawableDirName.replace(DRAWABLE_PREFIX, "", false)
            } else {
                drawableDirName
            }
            drawableDensity?.let {
                drawableDensityList.add(it)
            }

            // generate mipmap density list
            val mipmapDirName = pathArray.find { it.startsWith(MIPMAP_RESOURCE_NAME) }
            val mipmapDensity = if (mipmapDirName?.contains("-", false) == true) {
                mipmapDirName.replace(MIPMAP_PREFIX, "", false)
            } else {
                mipmapDirName
            }
            mipmapDensity?.let {
                mipmapDensityList.add(it)
            }
        }
    }

    fun hasDrawable() = drawableDensityList.isNotEmpty()

    fun hasMipmap() = mipmapDensityList.isNotEmpty()

    fun getLowDensityFilePath(): String {
        return filePathList[0]
    }

    fun getTargetDensityFilePath(density: String): String {
        return filePathList.first { it.contains(density, false) }
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

    override fun toString(): String {
        return fileName + " => drawable:[${getSupportedDrawableDensityName()}]" + " mipmap:[${getSupportedMipmapDensityName()}]"
    }

    companion object {
        fun create(fileName: String, allFilePathArray: List<File>?): DrawableModel {
            val filePathList = mutableListOf<String>()

            allFilePathArray?.forEach { file ->
                val filePath = file.path
                val pathArray = filePath.split(PATH_SEPARATOR)
                if (fileName != pathArray[pathArray.size - 1]) {
                    return@forEach
                }
                filePathList.add(filePath)
            }

            return DrawableModel(fileName, filePathList)
        }
    }
}