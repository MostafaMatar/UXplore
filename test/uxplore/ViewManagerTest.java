package test.uxplore;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.*;

import com.uxplore.FSItem;
import com.uxplore.managers.OperationManager;
import com.uxplore.managers.ViewManager;

import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.TilePane;
import uxplore.ExplorerController;

public class ViewManagerTest {
	@BeforeClass
	public static void initToolkit() {
		new JFXPanel();
	}

	@Test
	public void updateViewTest() throws IOException {
		ViewManager vm = new ViewManager();
		OperationManager opm = new OperationManager();
		ArrayList<FSItem> items = opm.getRoots();
		TilePane container = new TilePane();
		vm.updateView(items, container, ExplorerController.handler);
		assertEquals("the container must have 1 item", opm.getRoots().size(), container.getChildren().size());
	}
}
