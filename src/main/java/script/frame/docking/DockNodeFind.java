package script.frame.docking;

import java.util.List;

import com.anchorage.docks.containers.subcontainers.DockTabberContainer;
import com.anchorage.docks.misc.DockUtilsGet;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;

import javafx.scene.control.Tab;

public class DockNodeFind {

	/**
	 * Current dock node, can be alone or in tabs
	 */
	public static DockNode getCurrentDockNode(DockStation station) {
		DockNode found = null;

		// Fallback to Tabber-Selection
		DockTabberContainer dockContainer = DockUtilsGet.getTabberRecursive(station);
		if (dockContainer != null) {
			Tab tabSelected = dockContainer.getSelectionModel().getSelectedItem();
			if (tabSelected != null) {
				found = (DockNode) tabSelected.getContent();
			}
		}

		if (found == null) {
			// Fallback: first node

			List<DockNode> children = station.getNodes();
			if (!children.isEmpty()) {
				found = children.get(0);
			}
		}

		return found;
	}
}
