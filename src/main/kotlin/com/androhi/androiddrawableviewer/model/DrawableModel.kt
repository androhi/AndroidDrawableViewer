package com.androhi.androiddrawableviewer.model

import com.androhi.androiddrawableviewer.Constants.Companion.DRAWABLE_PREFIX
import com.androhi.androiddrawableviewer.Constants.Companion.DRAWABLE_RESOURCE_NAME
import com.androhi.androiddrawableviewer.Constants.Companion.MIPMAP_PREFIX
import com.androhi.androiddrawableviewer.Constants.Companion.MIPMAP_RESOURCE_NAME
import com.androhi.androiddrawableviewer.Constants.Companion.PATH_SEPARATOR
import java.io.File

class DrawableModel private constructor(val fileName: String, val filePathList: List<String>) {
    var drawableDensityMap = mutableMapOf<String, MutableList<String>>()
    var mipmapDensityMap = mutableMapOf<String, MutableList<String>>()

    init {
        filePathList.forEach { filePath ->

            // generate drawable density list
            val pathArray = filePath.split(PATH_SEPARATOR)
            val drawableDirName = pathArray.find { it.startsWith(DRAWABLE_RESOURCE_NAME) }
            drawableDirName?.let {
                val drawableFlavor = pathArray[pathArray.indexOf(it) - 2]
                val drawableDensity = when {
                    it.contains("-", false) -> it.replace(DRAWABLE_PREFIX, "", false)
                    else -> it
                }
                val densityList = drawableDensityMap[drawableFlavor]
                densityList?.add(drawableDensity) ?: drawableDensityMap.put(drawableFlavor, mutableListOf(drawableDensity))
            }

            // generate mipmap density list
            val mipmapDirName = pathArray.find { it.startsWith(MIPMAP_RESOURCE_NAME) }
            mipmapDirName?.let {
                val mipmapFlavor = pathArray[pathArray.indexOf(it) - 2]
                val mipmapDensity = when {
                    it.contains("-", false) -> it.replace(MIPMAP_PREFIX, "", false)
                    else -> it
                }
                val densityList = mipmapDensityMap[mipmapFlavor]
                densityList?.add(mipmapDensity) ?: mipmapDensityMap.put(mipmapFlavor, mutableListOf(mipmapDensity))
            }
        }
    }

    fun getLowDensityFilePath(): String {
        return filePathList[0]
    }

    fun getSupportedDrawableDensityName(): String =
            if (drawableDensityMap.isEmpty()) {
                "-"
            } else {
                drawableDensityMap.map { "${it.key}:[${it.value.joinToString("/")}]" }.joinToString(", ")
            }

    fun getSupportedMipmapDensityName(): String =
            if (mipmapDensityMap.isEmpty()) {
                "-"
            } else {
                mipmapDensityMap.map { "${it.key}:[${it.value.joinToString("/")}]" }.joinToString(", ")
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