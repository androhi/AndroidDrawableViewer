package com.androhi.androiddrawableviewer.model;

import java.util.List;

public class DrawableModel {

    private String fileName;
    private String resourceDirectory;
    private List<String> densityList;
    private int pixelSize;
    private long dataSize;

    public DrawableModel(String fileName, String resDir, List<String> densityList) {
        this.fileName = fileName;
        this.resourceDirectory = resDir;
        this.densityList = densityList;
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

    public List<String> getDensityList() {
        return densityList;
    }

    public void setDensityList(List<String> densityList) {
        this.densityList = densityList;
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
