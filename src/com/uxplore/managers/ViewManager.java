package com.uxplore.managers;

import com.uxplore.FSItem;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

/**
 * a class that controls the display of the application
 * @author Mostafa
 */
public class ViewManager {

    /**
     * update the list of items displayed to user
     * @param items
     * @param container
     * @param handler 
     */
    public void updateView(ArrayList<FSItem> items, TilePane container, EventHandler<MouseEvent> handler) {
        container.getChildren().clear();
        for (FSItem item : items) {
            if(!item.getItemProperties().isHidden()){
                VBox tile = new VBox();
                tile.setAlignment(Pos.CENTER);
                tile.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
                Label l = new Label(item.getItemProperties().getItemName());
                l.setAlignment(Pos.CENTER);
                l.setCenterShape(true);
                l.setMaxWidth(120);
                l.setMaxHeight(100);
                //l.setWrapText(true);
                ImageView i = new ImageView(item.getIcon().getIconImage());
                i.setFitHeight(32);
                i.setFitWidth(32);
                tile.getChildren().addAll(i, l);
                container.getChildren().add(tile);
            }
        }
    }
}
