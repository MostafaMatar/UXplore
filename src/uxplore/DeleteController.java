package uxplore;

import com.uxplore.FSItem;
import com.uxplore.managers.OperationManager;
import com.uxplore.managers.ViewManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Delete confirmation dialog Controller class
 *
 * @author Mostafa Matar
 */
public class DeleteController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * handler for confirming the delete operation
     * @param e 
     */
    @FXML
    protected void confirm(ActionEvent e) {
        OperationManager opm = new OperationManager();
        Label selectedItemName = (Label) ExplorerController.selectedTile.getChildren().get(1);
        if(!ExplorerController.path.endsWith("\\")){
            ExplorerController.path+="\\";
        }
        FSItem selected = new FSItem(ExplorerController.path + selectedItemName.getText());
        System.out.println(selected.getFsItemPath());
        opm.delete(selected);
        System.out.println(ExplorerController.items.remove(selected));
        ViewManager vm = new ViewManager();
        vm.updateView(ExplorerController.items, new ExplorerController().getInstance().getContainer(), ExplorerController.handler);
        this.closeDialog((Button) e.getSource());
    }

    /**
     * handler for canceling deletion
     * @param e 
     */
    @FXML
    protected void deny(ActionEvent e) {
        this.closeDialog((Button) e.getSource());
    }

    /**
     * close the delete dialog
     * @param b 
     */
    private void closeDialog(Button b) {
        Stage stage = (Stage) b.getScene().getWindow();
        stage.close();
    }
}
