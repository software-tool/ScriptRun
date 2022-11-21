package com.anchorage.docks.node.interfaces;

import com.anchorage.docks.containers.StageFloatable;
import com.anchorage.docks.node.DockNode;

/**
 * Listener for actions on dock nodes
 */
public interface IDockNodeListener {

	/**
	 * Dock node was undocked
	 */
	public void dockNodeUndocked(DockNode dockNode);

	/**
	 * Method called before stage is closed
	 */
	public void dockNodeBeforeCloseStage(StageFloatable stageFloatable);

	/**
	 * Method called after dock node/stage was closed with "x"-Icon
	 */
	public void afterDockNodeClosed(StageFloatable stageFloatable);
}
