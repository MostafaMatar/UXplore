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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * main controller of the apllication
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
    public static TilePane container;
    
    /**
     * event handler for clicking an item
     */
    public static EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent e) {
            VBox clickedTile = (VBox) e.getSource();
            if (e.getClickCount() == 1) {
                if (ExplorerController.selectedTile == null) {
                    clickedTile.setStyle("-fx-background-color:aqua;");
                } else {
                    ExplorerController.selectedTile.setStyle("-fx-background-color:#ffffff;");
                    clickedTile.setStyle("-fx-background-color:aqua;");
                }
                ExplorerController.selectedTile = clickedTile;
            } else {
                Label text = (Label) clickedTile.getChildren().get(1);
                OperationManager opm = new OperationManager();
                FSItem parent = new FSItem(ExplorerController.path + text.getText());
                if (Files.isDirectory(Paths.get(parent.getFsItemPath()))) {
                    ExplorerController.items.clear();
                    ExplorerController.items = opm.openFolder(parent);
                    ExplorerController.path = parent.getFsItemPath();
                    ViewManager vm = new ViewManager();
                    vm.updateView(ExplorerController.items, ExplorerController.container, this);
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
        ExplorerController.container.setStyle("-fx-background-color:#ffffff;");
        ExplorerController.items = new ArrayList<>();
        OperationManager opm = new OperationManager();
        ExplorerController.items = opm.getRoots();

        ViewManager vm = new ViewManager();
        vm.updateView(ExplorerController.items, ExplorerController.container, handler);
    }

    /**
     * event handler for pressing back
     * @param e 
     */
    @FXML
    protected void goBack(ActionEvent e) {
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
            ViewManager vm = new ViewManager();
            vm.updateView(ExplorerController.items, ExplorerController.container, this.handler);
        }
    }

    /**
     * event handler for pressing copy
     * @param e 
     */
    @FXML
    protected void copy(ActionEvent e) {
        Label selectedItemName = (Label) ExplorerController.selectedTile.getChildren().get(1);
        if (!selectedItemName.getText().equals("")) {
            ExplorerController.operationItem = new FSItem(ExplorerController.path + "\\" + selectedItemName.getText());
            ExplorerController.operationItem.getItemProperties().setItemName(selectedItemName.getText());
        }
        Button source = (Button) e.getSource();
        if (source.getText().equals("copy")) {
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
    protected void paste(ActionEvent e) {
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
    protected void delete(ActionEvent e){
        this.showDeleteDialog();
    }
    
    /**
     * event handler for pressing open
     * @param e 
     */
    @FXML
    protected void open(ActionEvent e){
        Label selectedItemName = (Label) ExplorerController.selectedTile.getChildren().get(1);
        FSItem selected=new FSItem(ExplorerController.path + "\\" + selectedItemName.getText());
        OperationManager opm=new OperationManager(); 
        if (Files.isDirectory(Paths.get(selected.getFsItemPath()))) {
            ExplorerController.items.clear();
            ExplorerController.items=opm.openFolder(selected);
            ExplorerController.path+="\\"+selectedItemName.getText();
            ViewManager vm=new ViewManager();
            vm.updateView(ExplorerController.items, ExplorerController.container, this.handler);
        } else {
            opm.openFile(selected);
        }
    }
    
    /**
     * handler for pressing rename
     * @param e 
     */
    @FXML
    protected void rename(ActionEvent e){
        
        this.showRenameDialog();
    }

    /**
     * shows the dialog for performing rename operation
     */
    private void showRenameDialog() {
        Parent root=null;    
        try {
            root = FXMLLoader.load(getClass().getResource("rename.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();            
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    /**
     * shows the dialog for performing delete operation
     */
    private void showDeleteDialog() {
        Parent root=null;    
        try {
            root = FXMLLoader.load(getClass().getResource("delete.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ExplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();            
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
