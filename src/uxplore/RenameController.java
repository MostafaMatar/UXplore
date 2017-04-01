/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uxplore;

import com.uxplore.FSItem;
import com.uxplore.managers.OperationManager;
import com.uxplore.managers.ViewManager;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mostafa Matar
 */
public class RenameController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    protected TextField name;
    
    @FXML
    protected void rename(ActionEvent e) {
        Label selectedItemName = (Label) ExplorerController.selectedTile.getChildren().get(1);
        FSItem selected = new FSItem(ExplorerController.path + "\\" + selectedItemName.getText());
        OperationManager opm = new OperationManager();
        if (Files.isDirectory(Paths.get(selected.getFsItemPath()))) {
            opm.rename(selected, this.name.getText());
        } else {
            int index = selectedItemName.getText().lastIndexOf(".");
            String extension = selectedItemName.getText().substring(index);
            String newName = this.name.getText() + extension;
            opm.rename(selected, newName);
        }
        ExplorerController.items = opm.openFolder(new FSItem(ExplorerController.path));
        ViewManager vm = new ViewManager();
        vm.updateView(ExplorerController.items, new ExplorerController().getInstance().getContainer(), ExplorerController.handler);
        Button okButton= (Button)e.getSource();
        Stage stage=(Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
