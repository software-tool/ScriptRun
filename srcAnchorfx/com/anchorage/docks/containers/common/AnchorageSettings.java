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
package com.anchorage.docks.containers.common;

import java.net.URL;

import javafx.scene.image.Image;

/**
 * Some settings for the docking
 *
 * @author Alessio
 */
public class AnchorageSettings {
	public static final int FLOATING_NODE_DROPSHADOW_RADIUS = 10;
	public static final int FLOATING_NODE_MINIMUM_WIDTH = 150;
	public static final int FLOATING_NODE_MINIMUM_HEIGHT = 150;

	private static boolean dockingPositionPreview = true;

	/**
	 * If true: Use simplified drop zones (smaller circles, fewer circles)
	 */
	public static boolean DROP_ZONES_SIMLIFIED = true;

	/**
	 * Modify behaviour of dock zones: If true, execute Stage.initOwner(Window) for dock zones
	 * 
	 * Setting this to false will prevent the dock zones (and its window) to come to the front when user is dragging a StageFloatable
	 */
	public static boolean INIT_OWNER_FOR_DOCK_ZONES = true;

	/**
	 * Icon of StageFloatable
	 */
	public static Image DOCK_ZONES_ICON = null;

	/**
	 * Text of window/taskbar (in operating system) when a StageFloatable is dragged
	 */
	public static String DOCK_ZONES_TEXT = null;

	/**
	 * CSS to be applied to dock zones
	 */
	public static URL DOCK_ZONES_CSS = null;

	public static void setDockingPositionPreview(boolean value) {
		dockingPositionPreview = value;
	}

	public static boolean isDockingPositionPreview() {
		return dockingPositionPreview;
	}

}
