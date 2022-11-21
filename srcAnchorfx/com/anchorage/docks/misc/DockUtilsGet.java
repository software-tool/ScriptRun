package com.anchorage.docks.misc;

import java.util.ArrayList;
import java.util.List;

import com.anchorage.docks.containers.subcontainers.DockSplitterContainer;
import com.anchorage.docks.containers.subcontainers.DockTabberContainer;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;

/**
 * Util class to retrieve the content of stations/containers
 * 
 * @author richard-lllll
 */
public class DockUtilsGet {

	/**
	 * Find first tabber in the station (recursive)
	 */
	public static DockTabberContainer getTabberRecursive(DockStation station) {
		ObservableList<Node> children = station.getChildren();

		for (Node child : children) {
			DockTabberContainer found = getTabberRecursive(child);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	/**
	 * Find first splitter in the station (not recursive)
	 */
	public static DockSplitterContainer getSubcontainerSplitter(DockStation station) {
		ObservableList<Node> children = station.getChildren();

		for (Node node : children) {
			if (node instanceof DockSplitterContainer) {
				DockSplitterContainer splitter = (DockSplitterContainer) node;
				return splitter;
			}
		}

		return null;
	}

	/**
	 * Get all dock nodes in tabber
	 */
	public static List<DockNode> getTabContents(DockTabberContainer dockContainer) {
		List<DockNode> dockNodes = new ArrayList<DockNode>();

		ObservableList<Tab> tabs = dockContainer.getTabs();

		for (Tab tab : tabs) {
			dockNodes.add((DockNode) tab.getContent());
		}

		return dockNodes;
	}

	public static DockNode getParentDockNode(Node node) {
		if (node == null) {
			return null;
		}

		Node current = node;

		while (current != null) {
			if (current instanceof DockNode) {
				return (DockNode) current;
			}

			current = current.getParent();
		}

		return null;
	}

	public static DockTabberContainer getTabberRecursive(Node node) {
		if (node instanceof DockTabberContainer) {
			DockTabberContainer dockContainer = (DockTabberContainer) node;
			return dockContainer;
		}

		if (node instanceof DockSplitterContainer) {
			// Tabber can be inside of splitter

			DockSplitterContainer splitter = (DockSplitterContainer) node;

			ObservableList<Node> splitterChildren = splitter.getItems();
			for (Node splitterChild : splitterChildren) {
				DockTabberContainer found = getTabberRecursive(splitterChild);
				if (found != null) {
					return found;
				}
			}
		}

		return null;
	}

}
