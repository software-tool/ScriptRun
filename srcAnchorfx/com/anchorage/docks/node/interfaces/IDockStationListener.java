package com.anchorage.docks.node.interfaces;

import com.anchorage.docks.node.DockNode;

/**
 * Listener for actions on dock stations
 */
public interface IDockStationListener {

	/**
	 * Dock of a node is finished
	 */
	public void dockFinished(DockNode dockNode);

	/**
	 * Dock was put into the station
	 */
	public void putDockFinished(DockNode dockNode);
}
