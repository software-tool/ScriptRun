package com.anchorage.docks.misc;

import com.anchorage.docks.containers.subcontainers.DockTabberContainer;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;

public class DockUtilsTabs {

	/**
	 * Returns the dock node for the tab
	 * 
	 * @param tabber Tabber Container
	 * @param tabToFind Tab to find
	 */
	public static DockNode getDockNodeForTab(DockTabberContainer tabber, Tab tabToFind) {
		ObservableList<Tab> tabs = tabber.getTabs();
		for (Tab tab : tabs) {
			if (tab.equals(tabToFind)) {
				return (DockNode) tab.getContent();
			}
		}

		return null;
	}

	public static void setTabsTitle(DockStation station, DockNode dockNode, String title, String tooltip) {
		// Tab
		DockTabberContainer dockContainer = DockUtilsGet.getTabberRecursive(station);
		if (dockContainer != null) {
			Tab tab = dockContainer.getTabByNode(dockNode);
			if (tab != null) {
				Node graphic = tab.getGraphic();
				if (graphic instanceof Label) {
					Label labelTab = (Label) graphic;

					labelTab.setText(title);

					if (tooltip != null) {
						labelTab.setTooltip(new Tooltip(tooltip));
					}
				}
			}
		}

		// Title of the DockNode (if in window)
		dockNode.getContent().setTitle(title);
	}

	public static void selectTabOfDockNode(DockTabberContainer tabberContainer, DockNode dockNode) {
		ObservableList<Tab> tabs = tabberContainer.getTabs();

		for (Tab tab : tabs) {
			Node content = tab.getContent();
			if (!(content instanceof DockNode)) {
				continue;
			}

			DockNode dockNodeCurrent = (DockNode) content;

			if (dockNode.equals(dockNodeCurrent)) {
				tabberContainer.getSelectionModel().select(tab);
				break;
			}
		}
	}
}
