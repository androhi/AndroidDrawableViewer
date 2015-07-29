package com.androhi.androiddrawableviewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class IconUtils {

    public static Icon createSmallIcon(String iconFile) {
        return createIcon(iconFile, 24);
    }

    public static Icon createOriginalIcon(String iconFile) {
        return createIcon(iconFile, 0);
    }

    private static Icon createIcon(String iconFile, int scaleSize) {
        File imageFile = new File(iconFile);
        try {
            Image originalImage = ImageIO.read(imageFile);
            if (scaleSize > 0) {
                Image resizedImage = originalImage.getScaledInstance(scaleSize, scaleSize, Image.SCALE_DEFAULT);
                return new ImageIcon(resizedImage);
            } else {
                return new ImageIcon(originalImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
