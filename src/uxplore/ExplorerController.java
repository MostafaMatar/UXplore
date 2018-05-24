/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uxplore;

import com.uxplore.FSItem;
import com.uxplore.managers.OperationManager;
import com.uxplore.managers.ViewManager;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * main controller of the application
 * @author Mostafa
 */
public class ExplorerController implements Initializable {
    /**
     * current path in file system
     */
    public static String path = "";
    /**
     * list of items in current path 
     */
    public static ArrayList<FSItem> items;
    /**
     * holds selected item
     */
    public static VBox selectedTile;
    /**
     * holds item operated on
     */
    public static FSItem operationItem;
    /**
     * true if operation is cut
     */
    public static boolean isCut;
    /**
     * the container of the items displayed
     */
    @FXML
    private TilePane container;
    
    /**
     * the name of the selected item displayed on the footer
     */
    @FXML
    private Label selectedName;
    
    @FXML
    private ImageView backImg;
    @FXML
    private ImageView copyImg;
    @FXML
    private ImageView cutImg;
    @FXML
    private ImageView pasteImg;
    @FXML
    private ImageView openImg;
    @FXML
    private ImageView renameImg;
    @FXML
    private ImageView deleteImg;
    
    public TilePane getContainer(){
        return this.container;
    }
    public void setContainer(TilePane c){
        this.container = c;
    }
    /**
     * @return the selectedName
     */
    public Label getSelectedName() {
        return selectedName;
    }

    /**
     * @param selectedName the selectedName to set
     */
    public void setSelectedName(String selectedName) {
        this.selectedName.setText(selectedName);
    }
    
    private static ExplorerController con;
    public ExplorerController getInstance(){
        return ExplorerController.con;
    }
    
    /**
     * event handler for clicking an item
     */
    public static EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            VBox clickedTile = (VBox) e.getSource();
            Label text = (Label) clickedTile.getChildren().get(1);
            if (e.getClickCount() == 1) {
                if (ExplorerController.selectedTile == null) {
                    clickedTile.setStyle("-fx-background-color:aqua;");
                } else {
                    ExplorerController.selectedTile.setStyle("-fx-background-color:#ffffff;");
                    clickedTile.setStyle("-fx-background-color:aqua;");
                }
                new ExplorerController().getInstance().setSelectedName(text.getText());
                ExplorerController.selectedTile = clickedTile;
            } else {
                OperationManager opm = new OperationManager();
                if(! ExplorerController.path.isEmpty())
                    ExplorerController.path = (ExplorerController.path.endsWith("\\"))? ExplorerController.path : ExplorerController.path+"\\";
                FSItem parent = new FSItem(ExplorerController.path + text.getText());
                if (Files.isDirectory(Paths.get(parent.getFsItemPath()))) {
                    ExplorerController.items.clear();
                    ExplorerController.items = opm.openFolder(parent);
                    ExplorerController.path = parent.getFsItemPath();
                    ViewManager vm = new ViewManager();
                    vm.updateView(ExplorerController.items, new ExplorerController().getInstance().getContainer(), this);
                } else {
                    FSItem file = new FSItem(parent.getFsItemPath());
                    opm.openFile(file);
                }
            }
        }
    };

    /**
     * runs at first initialization of application
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ExplorerController.operationItem = null;
        ExplorerController.con = this;
        this.container.setStyle("-fx-background-color:#ffffff;");
        ExplorerController.items = new ArrayList<>();
        OperationManager opm = new OperationManager();
        ExplorerController.items = opm.getRoots();
        Tooltip.install(copyImg, new Tooltip("Copy"));
        Tooltip.install(pasteImg, new Tooltip("Paste"));
        Tooltip.install(cutImg, new Tooltip("Cut"));
        Tooltip.install(openImg, new Tooltip("Open"));
        Tooltip.install(deleteImg, new Tooltip("Delete"));
        Tooltip.install(renameImg, new Tooltip("Rename"));
        Tooltip.install(backImg, new Tooltip("Back"));
        ViewManager vm = new ViewManager();
        vm.updateView(ExplorerController.items, this.container, ExplorerController.handler);
    }

    /**
     * event handler for pressing back
     * @param e 
     */
    @FXML
    protected void goBack(MouseEvent e) {
        if (!ExplorerController.path.equals("")) {
            OperationManager opm = new OperationManager();
            ExplorerController.items.clear();
            Path p = Paths.get(ExplorerController.path).getParent();
            if (p == null) {
                ExplorerController.items = opm.getRoots();
                ExplorerController.path = "";
            } else {
                ExplorerController.items = opm.openFolder(new FSItem(p.toString()));
                ExplorerController.path = p.toString();
            }
            new ExplorerController().getInstance().setSelectedName(ExplorerController.path.substring(ExplorerController.path.lastIndexOf("\\")+1));
            ViewManager vm = new ViewManager();
            vm.updateView(ExplorerController.items, this.container, ExplorerController.handler);
        }
    }

    /**
     * event handler for pressing copy
     * @param e 
     */
    @FXML
    protected void copy(MouseEvent e) {
        Label selectedItemName = (Label) ExplorerController.selectedTile.getChildren().get(1);
        if (!selectedItemName.getText().equals("")) {
            ExplorerController.operationItem = new FSItem(ExplorerController.path + "\\" + selectedItemName.getText());
            ExplorerController.operationItem.getItemProperties().setItemName(selectedItemName.getText());
        }
        ImageView source = (ImageView) e.getSource();
        if (source.getId().equals("copyImg")) {
            ExplorerController.isCut = false;
        } else {
            ExplorerController.isCut = true;
        }
    }

    /**
     * event handler for pressing paste
     * @param e 
     */
    @FXML
    protected void paste(MouseEvent e) {
        OperationManager opm = new OperationManager();
        if (ExplorerController.isCut) {
            opm.cut(ExplorerController.operationItem, ExplorerController.path);
        } else {
            opm.copy(ExplorerController.operationItem, ExplorerController.path);
        }
    }
    
    /**
     * event handler for pressing delete
     * @param e 
     */
    @FXML
    protected void delete(MouseEvent e){
        this.showDeleteDialog();
    }
    
    /**
     * event handler for pressing open
     * @param e 
     */
    @FXML
    protected void open(MouseEvent e){
        Label selectedItemName = (Label) ExplorerController.selectedTile.getChildren().get(1);
        String old = ExplorerController.path.equals("")? "" : ExplorerController.path ;
        System.out.println(ExplorerController.path);
        if(!old.equals(""))
            old = old.endsWith("\\") ? old : (old+"\\");
        FSItem selected=new FSItem(old + selectedItemName.getText());
        OperationManager opm=new OperationManager(); 
        if (Files.isDirectory(Paths.get(selected.getFsItemPath()))) {
            ExplorerController.items.clear();
            ExplorerController.items=opm.openFolder(selected);
            ExplorerController.path = old + selectedItemName.getText();
            ViewManager vm=new ViewManager();
            vm.updateView(ExplorerController.items, this.container, ExplorerController.handler);
        } else {
            opm.openFile(selected);
        }
    }
    
    /**
     * handler for pressing rename
     * @param e 
     */
    @FXML
    protected void rename(MouseEvent e){
        
        this.showRenameDialog();
    }

    /**
     * shows the dialog for performing rename operation
     */
    private void showRenameDialog() {
        Parent root=null;    
        try {
            root = FXMLLoader.load(getClass().getResource("rename.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();            
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * shows the dialog for performing delete operation
     */
    private void showDeleteDialog() {
        Parent root=null;    
        try {
            root = FXMLLoader.load(getClass().getResource("delete.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();            
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    protected void close(ActionEvent e) {
        System.exit(0);
    }
}
