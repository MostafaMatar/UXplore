package com.uxplore;

/**
 * The model of the application representing a file or a folder of the file
 * system.
 *
 * @author Mostafa
 */
public class FSItem {

    /**
     * An object containing certain attributes of the file/folder.
     */
    private FSItemProperties itemProperties;
    /**
     * An object representing the icon of the file/folder.
     */
    private FSIcon icon;
    /**
     * A string holding the absolute url to the file/folder in the file system.
     */
    private String fsItemPath;

    /**
     * Constructs an item using a url to it.
     *
     * @param itemPath String represents the path to the item on the file
     * system.
     */
    public FSItem(String itemPath) {
        this.fsItemPath = itemPath;
        this.itemProperties = new FSItemProperties();
    }

    /**
     * A getter for the path of the item.
     *
     * @return String representing the path.
     */
    public String getFsItemPath() {
        return fsItemPath;
    }

    /**
     * sets the path of the item.
     *
     * @param fsItemPath String holding the value of the path
     */
    public void setFsItemPath(String fsItemPath) {
        this.fsItemPath = fsItemPath;
    }

    /**
     * gets the properties object of the item.
     *
     * @return Object representing the properties.
     */
    public FSItemProperties getItemProperties() {
        return itemProperties;
    }

    /**
     *sets the properties object of the item.
     * @param itemProperties object holding the properties.
     */
    public void setItemProperties(FSItemProperties itemProperties) {
        this.itemProperties = itemProperties;
    }

    /**
     * gets the icon of the item.
     * @return object representing the icon.
     */
    public FSIcon getIcon() {
        return icon;
    }

    /**
     * sets the icon of the item.
     * @param icon object representing an icon.
     */
    public void setIcon(FSIcon icon) {
        this.icon = icon;
    }

    /**
     * define the equality rules of 2 items
     * @param obj item to compare
     * @return true if the 2 items are the same
     */
    @Override
    public boolean equals(Object obj) {
        FSItem item=(FSItem)obj;
        return this.fsItemPath.equals(item.fsItemPath);
    }
}
