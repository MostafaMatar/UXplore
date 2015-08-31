package com.uxplore;

/**
 * a class representing properties of a file /folder.
 * @author Mostafa
 */
public class FSItemProperties {

    /**
     * name of a file/folder
     */
    private String itemName;
    /**
     * size of file/folder
     */
    private String itemSize;
    /**
     * creation date of file/folder
     */
    private String itemCreationDate;
    /**
     * last access date of file/folder
     */
    private String itemLastAccessDate;
    /**
     * true if item is hidden
     */
    private boolean hidden;

    /**
     * gets item name
     * @return 
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * sets item name
     * @param itemName 
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * gets item size
     * @return 
     */
    public String getItemSize() {
        return itemSize;
    }

    /**
     * sets item size
     * @param itemSize 
     */
    public void setItemSize(long itemSize) {
        double s=(itemSize)/(1024.0*1024.0*1024.0);
        this.itemSize=Double.toString(s);
    }

    /**
     * gets creation date for item
     * @return 
     */
    public String getItemCreationDate() {
        return itemCreationDate;
    }

    /**
     * sets creation date for item
     * @param itemCreationDate 
     */
    public void setItemCreationDate(String itemCreationDate) {
        this.itemCreationDate = itemCreationDate;
    }

    /**
     * gets last access date for item
     * @return 
     */
    public String getItemLastAccessDate() {
        return itemLastAccessDate;
    }

    /**
     * sets last access date for item
     * @param itemLastAccessDate 
     */
    public void setItemLastAccessDate(String itemLastAccessDate) {
        this.itemLastAccessDate = itemLastAccessDate;
    }
    
    /**
     * returns true if item is hidden
     * @return 
     */
    public boolean isHidden(){
        return this.hidden;
    }
    
    /**
     * set to true if item is hidden
     * @param hidden 
     */
    public void setHidden(boolean hidden){
        this.hidden=hidden;
    }
}
