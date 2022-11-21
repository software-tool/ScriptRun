/*
 * Copyright 2015-2016 Alessio Vinerbi. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anchorage.system;

import java.util.ArrayList;
import java.util.List;

import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.node.ui.DockUIPanel;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.docks.stations.DockSubStation;
import com.anchorage.system.interfaces.IDockGlobalListener;
import com.sun.javafx.css.StyleManager;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 *
 * @author Alessio
 */
public class AnchorageSystem {

	private static final List<DockStation> stations;
	private static final Image emptyIconImage;
	private static final Image emptySubstationIconImage;

	private static DockStation currentStationFromDrag;

	private static List<IDockGlobalListener> globalListeners = new ArrayList<>();

	static {
		stations = new ArrayList<>();
		emptyIconImage = new Image("empty.png");
		emptySubstationIconImage = new Image("substation.png");
	}

	public static DockStation createStation() {
		DockStation station = new DockStation();
		stations.add(station);
		return station;
	}

	public static DockStation createCommonStation() {
		DockStation station = new DockStation(true);
		stations.add(station);
		return station;
	}

	public static DockSubStation createSubStation(DockStation parentStation, String title) {
		DockSubStation station = new DockSubStation(new DockUIPanel(title, null, new DockStation(), true, emptySubstationIconImage));
		return station;
	}

	public static DockNode createDock(String title, Node content) {
		return createDock(title, null, content, emptyIconImage);
	}

	public static DockNode createDock(String title, String tooltip, Node content) {
		return createDock(title, tooltip, content, emptyIconImage);
	}

	public static DockNode createDock(String title, String tooltip, Node content, Image icon) {
		DockUIPanel panel = new DockUIPanel(title, tooltip, content, false, icon);
		DockNode container = new DockNode(panel);
		return container;
	}

	public static void installDefaultStyle() {
		StyleManager.getInstance().addUserAgentStylesheet("AnchorFX.css");
	}

	/**
	 * Prepare before drag
	 */
	public static void prepareDraggingZoneFor(DockStation station, DockNode source) {
		currentStationFromDrag = station;
		station.prepareZones(source);
		if (currentStationFromDrag.isSubStation()) {
			return;
		}
		if (station.isCommonStation()) {
			stations.stream().filter(s -> s != station && s.isCommonStation()).forEach(s -> s.prepareZones(source));
		}
	}

	public static void searchTargetNode(double x, double y) {
		if (currentStationFromDrag.isCommonStation() && !currentStationFromDrag.isSubStation()) {
			stations.stream().filter(s -> s.isCommonStation()).forEach(s -> s.searchTargetNode(x, y));
		} else {
			currentStationFromDrag.searchTargetNode(x, y);
		}
	}

	public static void finalizeDragging(Point2D dropTarget) {
		if (currentStationFromDrag.isSubStation()) {
			currentStationFromDrag.closeZones();
			currentStationFromDrag.finalizeDrag(dropTarget);
		} else {
			if (currentStationFromDrag.isCommonStation()) {
				stations.stream().filter(s -> s.isCommonStation()).forEach(s -> s.closeZones());
			} else {
				currentStationFromDrag.closeZones();
			}

			DockStation selectedStation = stations.stream().filter(s -> s.isSelected()).findFirst().orElse(null);
			if (selectedStation != null && currentStationFromDrag.isCommonStation()) {
				selectedStation.finalizeDrag(dropTarget);
			} else {
				currentStationFromDrag.finalizeDrag(dropTarget);
			}
		}
	}

	public static void addGlobalListener(IDockGlobalListener listener) {
		globalListeners.add(listener);
	}

	public static void removeGlobalListener(IDockGlobalListener listener) {
		globalListeners.remove(listener);
	}

	public static List<IDockGlobalListener> getGlobalListeners() {
		return globalListeners;
	}
}
