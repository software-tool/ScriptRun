package com.anchorage.docks.misc;

import com.anchorage.docks.containers.subcontainers.DockTabberContainer;
import com.anchorage.docks.stations.DockStation;

public class DockUtilsConfig {

	/**
	 * Modifiy DockStation: Will remain a tabber container (TabPane)
	 */
	public static void ensureIsTab(DockStation station) {
		DockTabberContainer tabberContainer = DockUtilsGet.getTabberRecursive(station);
		tabberContainer.setKeepAsTabber(true);
	}
}
