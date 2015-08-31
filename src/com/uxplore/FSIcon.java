package com.uxplore;

import javafx.scene.image.Image;

/**
 * A class holding the data needed to represent an icon for a file/folder.
 *
 * @author Mostafa
 */
public class FSIcon {

    /**
     * An image object holding the icon.
     */
    private Image icon;
    /**
     * a byte holding the height of the icon.
     */
    private byte iconHeight;
    /**
     * a byte holding the width of the icon.
     */
    private byte iconWidth;

    /**
     * gets the image object.
     * @return an image representing the icon.
     */
    public Image getIconImage() {
        return icon;
    }

    /**
     * sets the image object
     * @param iconPath an image icon
     */
    public void setIconImage(Image iconPath) {
        this.icon = iconPath;
    }

    /**
     * gets icon height
     * @return icon height
     */
    public byte getIconHeight() {
        return iconHeight;
    }

    /**
     * sets icon height
     * @param iconHeight height value 
     */
    public void setIconHeight(byte iconHeight) {
        this.iconHeight = iconHeight;
    }

    /**
     * gets icon width
     * @return width value
     */
    
    public byte getIconWidth() {
        return iconWidth;
    }

    /**
     * sets icon width
     * @param iconWidth width value. 
     */
    public void setIconWidth(byte iconWidth) {
        this.iconWidth = iconWidth;
    }
}
