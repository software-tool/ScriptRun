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
package com.anchorage.docks.containers.subcontainers;

import com.anchorage.docks.containers.common.DockCommons;
import com.anchorage.docks.containers.interfaces.DockContainer;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.node.ui.DockUIPanel;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;

/**
 * @author Alessio
 */
public final class DockTabberContainer extends TabPane implements DockContainer {

	private DockContainer container;

	private boolean keepAsTabber = false;

	@Override
	public void putDock(DockNode node, DockNode.DockPosition position, double percentage) {
		addAsTab(node);
		node.ensureVisibility();
	}

	public Tab addAsTab(DockNode node) {
		DockUIPanel content = node.getContent();
		Label tabLabel = new Label(content.getTitle());

		Tab newTab = new Tab(content.getTitle());
		getTabs().add(newTab);
		newTab.setContent(node);
		newTab.setText("");
		newTab.setGraphic(tabLabel);

		if (content.getTooltip() != null) {
			tabLabel.setTooltip(new Tooltip(content.getTooltip()));
		}

		node.installDragEventManager(newTab.getGraphic());
		node.setParentContainer(this);
		newTab.closableProperty().bind(node.closeableProperty());
		newTab.setOnCloseRequest(event -> {
			if (node.getCloseRequestHandler() == null || node.getCloseRequestHandler().canClose()) {
				node.undock();
			}

			// Consume event in any cases (also prevents closing if canClose() == false)
			event.consume();
		});
		return newTab;
	}

	private void createSplitter(DockNode node, DockNode.DockPosition position) {
		DockContainer currentContainer = container;
		DockSplitterContainer splitter = DockCommons.createSplitter(this, node, position, 0.5);
		int indexOf = currentContainer.indexOf(this);
		currentContainer.insertNode(splitter, indexOf);
		currentContainer.removeNode(this);
		container = splitter;
	}

	/**
	 * Returns tab of node
	 * 
	 * @return Tab found, can be null
	 */
	public Tab getTabByNode(DockNode node) {
		return getTabs().stream().filter(t -> t.getContent() == node).findFirst().orElse(null);
	}

	@Override
	public boolean isDockVisible(DockNode node) {
		Tab nodeTab = getTabByNode(node);
		return getSelectionModel().getSelectedItem() == nodeTab;
	}

	@Override
	public void putDock(DockNode node, DockNode nodeTarget, DockNode.DockPosition position, double percentage) {
		if (position != DockNode.DockPosition.CENTER) {
			createSplitter(node, position);
		} else {
			if (node.getParentContainer() != this) {
				putDock(node, position, percentage);
			}
		}
	}

	@Override
	public int indexOf(Node node) {
		int index = 0;
		boolean found = false;
		for (Tab tab : getTabs()) {
			if (tab.getContent() == node) {
				found = true;
				break;
			}
			index++;
		}
		return (found) ? index : -1;
	}

	@Override
	public void undock(DockNode node) {
		int index = indexOf(node);
		Tab tab = getTabs().get(index);
		getTabs().remove(tab);
		node.setParentContainer(null);

		if (!keepAsTabber && getTabs().size() == 1) {
			DockNode remainingNode = (DockNode) getTabs().get(0).getContent();
			getTabs().remove(0);
			int indexInsideParent = getParentContainer().indexOf(this);
			getParentContainer().insertNode(remainingNode, indexInsideParent);
			getParentContainer().removeNode(this);
		}
	}

	@Override
	public void insertNode(Node node, int index) {
		// NOTHING
	}

	@Override
	public void removeNode(Node node) {
		// NOTHING
	}

	@Override
	public DockContainer getParentContainer() {
		return container;
	}

	@Override
	public void setParentContainer(DockContainer container) {
		this.container = container;
	}

	public void manageDragOnSameNode(DockNode node, DockNode.DockPosition position) {
		if (getTabByNode(node) != null && getTabs().size() == 2) {
			DockNode otherNode = (getTabs().get(0).getContent() == node) ? (DockNode) getTabs().get(1).getContent() : (DockNode) getTabs().get(0).getContent();
			node.undock();
			node.dock(otherNode, position);
		} else if (getTabByNode(node) != null && getTabs().size() > 2) {
			node.undock();
			DockContainer currentContainer = container;
			DockSplitterContainer splitter = DockCommons.createSplitter(this, node, position, 0.5);
			int indexOf = currentContainer.indexOf(this);
			currentContainer.insertNode(splitter, indexOf);
			currentContainer.removeNode(this);
			container = splitter;
			node.stationProperty().get().add(node);
		}
	}

	public void ensureVisibility(DockNode node) {
		Tab tabNode = getTabByNode(node);
		getSelectionModel().select(tabNode);
	}

	/**
	 * If true: This container will remain a tabber container, on modification of dock nodes contained
	 */
	public void setKeepAsTabber(boolean keepAsTabber) {
		this.keepAsTabber = keepAsTabber;
	}

}