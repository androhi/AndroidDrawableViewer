package com.androhi.androiddrawableviewer.model;

import java.util.List;

public class DrawableModel {

    private String fileName;
    private String resourceDirectory;
    private List<String> drawableDensityList;
    private List<String> mipmapDensityList;
    private int pixelSize;
    private long dataSize;

    public DrawableModel(String fileName, String resDir,
                         List<String> drawableDensityList, List<String> mipmapDensityList) {
        this.fileName = fileName;
        this.resourceDirectory = resDir;
        this.drawableDensityList = drawableDensityList;
        this.mipmapDensityList = mipmapDensityList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResourceDirectory() {
        return resourceDirectory;
    }

    public void setResourceDirectory(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }

    public List<String> getDrawableDensityList() {
        return drawableDensityList;
    }

    public void setDrawableDensityList(List<String> densityList) {
        this.drawableDensityList = densityList;
    }

    public List<String> getMipmapDensityList() {
        return mipmapDensityList;
    }

    public void setMipmapDensityList(List<String> densityList) {
        this.mipmapDensityList = densityList;
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(int pixelSize) {
        this.pixelSize = pixelSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }
}
