package com.uxplore.managers;

import com.uxplore.FSIcon;
import com.uxplore.FSItem;
import com.uxplore.FSItemProperties;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.*;
import uxplore.ExplorerController;

/**
 * a class responsible for performing copy/cut/delete/... operations on files
 * and folders
 *
 * @author Mostafa
 */
public class OperationManager {

    /**
     * item being operated on
     */
    private FSItem x;
    /**
     * path for item to move to in case of copy and cut operations
     */
    private String xPath;

    /**
     * inner class to perform copy on a new thread
     */
    class CopyOperation implements Runnable {

        @Override
        public void run() {
            try {
                Path target = Paths.get(x.getFsItemPath());
                Path tPath = Paths.get(xPath);
                if (Files.isDirectory(target)) {
                    FileUtils.copyDirectoryToDirectory(target.toFile(), tPath.toFile());
                } else {
                    FileUtils.copyFileToDirectory(target.toFile(), tPath.toFile());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * copy item to destination path
     *
     * @param targetItem
     * @param targetPath
     */
    public void copy(FSItem targetItem, String targetPath) {
        this.x = targetItem;
        this.xPath = targetPath;
        CopyOperation cpo = new CopyOperation();
        Thread t = new Thread(cpo);
        t.start();
    }

    /**
     * inner class to perform delete in a different thread
     */
    class DeleteOperation implements Runnable {

        @Override
        public void run() {
            Path filePath = Paths.get(x.getFsItemPath());
            FileUtils.deleteQuietly(filePath.toFile());
        }
    }

    /**
     * delete item
     *
     * @param targetItem
     */
    public void delete(FSItem targetItem) {
        this.x = targetItem;
        DeleteOperation dlo = new DeleteOperation();
        Thread t = new Thread(dlo);
        t.start();
    }

    /**
     * rename item
     *
     * @param targetItem
     * @param newName
     * @return
     */
    public boolean rename(FSItem targetItem, String newName) {
        try {
            Path filePath = Paths.get(targetItem.getFsItemPath());
            Files.move(filePath, filePath.resolveSibling(newName),
                    StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * inner class to perform cut in a different thread
     */
    class CutOperation implements Runnable {

        @Override
        public void run() {
            try {
                Path filePath = Paths.get(x.getFsItemPath());
                Path endPath = Paths.get(xPath + "\\" + x.getItemProperties().getItemName());
                Files.move(filePath, endPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * cut item
     *
     * @param targetItem
     * @param targetPath
     */
    public void cut(FSItem targetItem, String targetPath) {
        this.x = targetItem;
        this.xPath = targetPath;
        CutOperation cto = new CutOperation();
        Thread t = new Thread(cto);
        t.start();
    }

    /**
     * open a file using default program
     *
     * @param file
     * @return
     */
    public boolean openFile(FSItem file) {
        try {
            Desktop.getDesktop().open(new File(file.getFsItemPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * open a folder and return it's contents
     *
     * @param folder
     * @return
     */
    public ArrayList<FSItem> openFolder(FSItem folder) {
        ArrayList<FSItem> contents = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folder.getFsItemPath()));
            for (Path path : directoryStream) {

                DosFileAttributes attrs = Files.readAttributes(path, DosFileAttributes.class);
                FSItemProperties itemProperties = new FSItemProperties();
                FSIcon itemIcon = new FSIcon();

                FileSystemView fsv = FileSystemView.getFileSystemView();
                ImageIcon icon = (ImageIcon) fsv.getSystemIcon(path.toFile());
                itemIcon.setIconHeight((byte) icon.getIconHeight());
                itemIcon.setIconWidth((byte) icon.getIconWidth());
                itemIcon.setIconImage(this.createImage(icon.getImage()));

                itemProperties.setItemCreationDate(attrs.creationTime().toString());
                itemProperties.setItemLastAccessDate(attrs.lastAccessTime().toString());
                itemProperties.setItemName(path.getFileName().toString());
                itemProperties.setItemSize(attrs.size());
                itemProperties.setHidden(attrs.isHidden());

                FSItem item = new FSItem(path.toString());
                item.setItemProperties(itemProperties);
                item.setIcon(itemIcon);
                contents.add(item);
            }
            return contents;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * get root partitions
     *
     * @return
     */
    public ArrayList<FSItem> getRoots() {
        ArrayList<FSItem> result = new ArrayList<>();
        Iterable<Path> roots = FileSystems.getDefault().getRootDirectories();
        for (Path p : roots) {
            BasicFileAttributes attrs = null;
            FSItemProperties itemProperties = new FSItemProperties();
            FSIcon itemIcon = new FSIcon();

            ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(p.toFile());
            itemIcon.setIconHeight((byte) icon.getIconHeight());
            itemIcon.setIconWidth((byte) icon.getIconWidth());
            itemIcon.setIconImage(this.createImage(icon.getImage()));

            try {
                attrs = Files.readAttributes(p, BasicFileAttributes.class);
                itemProperties.setItemCreationDate(attrs.creationTime().toString());
                itemProperties.setItemLastAccessDate(attrs.lastAccessTime().toString());
                itemProperties.setItemName(p.toString());
                itemProperties.setItemSize(attrs.size());
            } catch (FileSystemException ex) {
                itemProperties.setItemName(p.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            FSItem item = new FSItem(p.toString());
            item.setItemProperties(itemProperties);
            item.setIcon(itemIcon);
            result.add(item);
        }
        return result;
    }

    /**
     * transform awt image to javafx image
     *
     * @param image
     * @return
     */
    private javafx.scene.image.Image createImage(java.awt.Image image) {
        if (!(image instanceof RenderedImage)) {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();

            image = bufferedImage;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) image, "png", out);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(OperationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return new javafx.scene.image.Image(in);
    }
}
