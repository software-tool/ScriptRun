package com.anchorage.system.interfaces;

import com.anchorage.docks.containers.StageFloatable;
import com.anchorage.docks.node.DockNode;

/**
 * Global listener for AnchorFx-System
 * 
 * @author richard-lllll
 */
public interface IDockGlobalListener {

	/**
	 * A node was closed/undocked using the close button
	 * 
	 * @param node The node closed
	 * @param stageBefore The stage the dock node was contained in BEFORE closing. May not be valid any more, but kept here as reference to know which stage was closed.
	 * @param floatingBefore True if the dock node was in a StageFloatable before (see other parameter)
	 */
	public void closingDockNodeFinished(DockNode node, StageFloatable stageBefore, boolean floatingBefore);

	/**
	 * A StageFloatable gained the focus
	 */
	public void floatableFocusGained(StageFloatable floatable);
}
