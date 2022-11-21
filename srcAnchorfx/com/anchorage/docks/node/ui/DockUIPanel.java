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
package com.anchorage.docks.node.ui;

import java.util.Objects;

import com.anchorage.docks.node.DockNode;

import javafx.beans.property.StringProperty;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Component to shown dock
 * 
 * Adds: - Icon - Title - Tooltip - CommandsBox
 * 
 * @author Alessio
 */
public final class DockUIPanel extends Pane {

	/** Height of the bar panels in pixels */
	public static final double BAR_HEIGHT = 25;

	/**
	 * If true, new docks will show the bar panels; set to false to hide bar panels
	 * by default
	 */
	public static boolean SHOW_NEW_BAR_PANELS = true;

	private Node nodeContent;
	private Label titleLabel;
	private String title;
	private String tooltip;
	private Pane barPanel;
	private StackPane contentPanel;
	private DockCommandsBox commandsBox;
	private DockNode node;

	private boolean substationType;
	private boolean hideTitle = false;

	private ImageView iconView;

	/**
	 * Konstruktor
	 */
	public DockUIPanel(String title, Node _nodeContent, boolean _substationType, Image imageIcon) {
		this(title, null, _nodeContent, _substationType, imageIcon);
	}

	/**
	 * Konstruktor, with Tooltip
	 */
	public DockUIPanel(String title, String tooltip, Node _nodeContent, boolean _substationType, Image imageIcon) {
		this.title = title;
		this.tooltip = tooltip;

		// Deactivated: Use AnchorageSystem.installDefaultStyle() instead
		// getStylesheets().add("AnchorFX.css");

		substationType = _substationType;
		Objects.requireNonNull(_nodeContent);
		Objects.requireNonNull(title);
		nodeContent = _nodeContent;
		buildNode(title, imageIcon);

		if (!SHOW_NEW_BAR_PANELS) {
			hideBarPanel();
		}
	}

	public Node getNodeForDraggingManagement() {
		return barPanel;
	}

	public void setIcon(Image icon) {
		Objects.requireNonNull(icon);
		iconView.setImage(icon);
	}

	private void makeCommands() {
		commandsBox = new DockCommandsBox(node);
		barPanel.getChildren().add(commandsBox);
		commandsBox.layoutXProperty()
				.bind(barPanel.prefWidthProperty().subtract(commandsBox.getChildren().size() * 30 + 10));
		commandsBox.setLayoutY(0);
		titleLabel.prefWidthProperty().bind(commandsBox.layoutXProperty().subtract(10));
	}

	public void setDockNode(DockNode node) {
		this.node = node;
		makeCommands();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;

		if (!hideTitle) {
			titleLabel.setText(title);
		}
	}

	public void setHideTitle() {
		this.hideTitle = true;

		titleLabel.setText(null);
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getTooltip() {
		return tooltip;
	}

	/**
	 * Hide the top bar
	 */
	public void hideBarPanel() {
		this.barPanel.setVisible(false);

		contentPanel.relocate(0, 0);

		contentPanel.prefHeightProperty().bind(heightProperty());
	}

	/**
	 * Show the top bar
	 */
	public void showBarPanel() {
		this.barPanel.setVisible(true);

		contentPanel.relocate(0, BAR_HEIGHT);

		contentPanel.prefHeightProperty().bind(heightProperty().subtract(BAR_HEIGHT));
	}

	public void destroy() {
		contentPanel.getChildren().clear();

		nodeContent = null;
	}

	private void buildNode(String title, Image iconImage) {
		Objects.requireNonNull(iconImage);
		Objects.requireNonNull(title);
		barPanel = new Pane();
		String titleBarStyle = (!substationType) ? "docknode-title-bar" : "substation-title-bar";
		barPanel.getStyleClass().add(titleBarStyle);
		barPanel.setPrefHeight(BAR_HEIGHT);
		barPanel.relocate(0, 0);
		barPanel.prefWidthProperty().bind(widthProperty());

		titleLabel = new Label(title);
		String titleTextStyle = (!substationType) ? "docknode-title-text" : "substation-title-text";
		iconView = new ImageView(iconImage);
		iconView.setFitWidth(15);
		iconView.setFitHeight(15);
		iconView.setPreserveRatio(false);
		iconView.setSmooth(true);
		iconView.relocate(1, (BAR_HEIGHT - iconView.getFitHeight()) / 2);
		titleLabel.getStyleClass().add(titleTextStyle);
		barPanel.getChildren().addAll(iconView, titleLabel);
		titleLabel.relocate(25, 5);
		contentPanel = new StackPane();
		contentPanel.getStyleClass().add("docknode-content-panel");
		contentPanel.relocate(0, BAR_HEIGHT);
		contentPanel.prefWidthProperty().bind(widthProperty());
		contentPanel.prefHeightProperty().bind(heightProperty().subtract(BAR_HEIGHT));
		contentPanel.getChildren().add(nodeContent);

		contentPanel.setCache(true);
		contentPanel.setCacheHint(CacheHint.SPEED);

		if (nodeContent instanceof Pane) {
			Pane nodeContentPane = (Pane) nodeContent;
			nodeContentPane.setMinHeight(USE_COMPUTED_SIZE);
			nodeContentPane.setMinWidth(USE_COMPUTED_SIZE);
			nodeContentPane.setMaxWidth(USE_COMPUTED_SIZE);
			nodeContentPane.setMaxHeight(USE_COMPUTED_SIZE);
		}
		getChildren().addAll(barPanel, contentPanel);
	}

	public StackPane getContentContainer() {
		return contentPanel;
	}

	/**
	 * Get the value of nodeContent
	 *
	 * @return the value of nodeContent
	 */
	public Node getNodeContent() {
		return nodeContent;
	}

	public boolean isMenuButtonEnable() {
		return commandsBox.isMenuButtonEnable();
	}

	public void setNodeContent(Node nodeContent) {
		contentPanel.getChildren().remove(this.nodeContent);

		this.nodeContent = nodeContent;

		contentPanel.getChildren().add(nodeContent);
	}

}
