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
package com.anchorage.docks.node;

import static com.anchorage.docks.containers.common.AnchorageSettings.FLOATING_NODE_DROPSHADOW_RADIUS;

import java.util.Objects;

import com.anchorage.docks.containers.NodeDraggingPreview;
import com.anchorage.docks.containers.StageFloatable;
import com.anchorage.docks.containers.interfaces.DockContainableComponent;
import com.anchorage.docks.containers.interfaces.DockContainer;
import com.anchorage.docks.containers.subcontainers.DockTabberContainer;
import com.anchorage.docks.node.interfaces.DockNodeCloseRequestHandler;
import com.anchorage.docks.node.interfaces.DockNodeCreationListener;
import com.anchorage.docks.node.interfaces.IDockNodeListener;
import com.anchorage.docks.node.ui.DockUIPanel;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.docks.stations.DockSubStation;
import com.anchorage.system.AnchorageSystem;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DockNode extends StackPane implements DockContainableComponent {

	/** Mouse distance to be move before drag is started (for tabs) */
	private static final double DRAG_THRESHOLD_PX_TAB_HEADER = 14;

	/** No mouse distance to apply dragging (for stage title bar) */
	private static final double DRAG_THRESHOLD_NONE = 0;

	private DockUIPanel content;

	private BooleanProperty floatableProperty;
	private BooleanProperty closeableProperty;
	private BooleanProperty resizableProperty;
	private BooleanProperty maximizableProperty;
	private ObjectProperty<DockStation> station;

	private ReadOnlyBooleanWrapper floatingProperty;
	private ReadOnlyBooleanWrapper draggingProperty;
	private ReadOnlyBooleanWrapper maximizingProperty;

	private ReadOnlyObjectWrapper<DockContainer> container;
	private StageFloatable stageFloatable;

	private double floatingStateCoordinateX;
	private double floatingStateCoordinateY;
	private double floatingStateWidth;
	private double floatingStateHeight;

	private DockNodeCloseRequestHandler closeRequestHandler;
	private IDockNodeListener dockNodeListener;

	private Point2D dragWindowOffset;
	private Point2D dragStartPosition;

	private NodeDraggingPreview nodePreview;

	private boolean maximizeOnDoubleClick = false;

	private DockNode() {
		station = new SimpleObjectProperty<>(null);
		floatableProperty = new SimpleBooleanProperty(true);
		closeableProperty = new SimpleBooleanProperty(true);
		resizableProperty = new SimpleBooleanProperty(true);
		maximizableProperty = new SimpleBooleanProperty(true);
		floatingProperty = new ReadOnlyBooleanWrapper(false);
		draggingProperty = new ReadOnlyBooleanWrapper(false);
		maximizingProperty = new ReadOnlyBooleanWrapper(false);
		container = new ReadOnlyObjectWrapper<>(null);

		//    floatingProperty.addListener((observable, oldValue, newValue) -> {
		//      if(newValue) {
		//        installNullDragManager(
		//            content.getNodeForDraggingManagement());
		//      } else {
		//        installDragEventManager(
		//            content.getNodeForDraggingManagement());
		//      }
		//    }
		//    );
	}

	public DockNode(DockUIPanel node) {
		this();

		this.content = node;

		buildUI(node);

		callCreationCallBack();
		installDragEventManager(content.getNodeForDraggingManagement());
	}

	private void showDraggedNodePreview(double x, double y) {
		if (nodePreview != null) {
			nodePreview.closeStage();
		}
		nodePreview = new NodeDraggingPreview(this, stationProperty().get().getScene().getWindow(), x, y);
		nodePreview.show();
	}

	public void installNullDragManager(Node n) {
		n.setOnMouseDragged(null);
	}

	public void installDragEventManager(Node n) {
		n.setOnMouseClicked(event -> {
			if (maximizeOnDoubleClick && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
				maximizeOrRestore();
			}
		});

		n.setOnMouseDragged(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (maximizingProperty().get()) {
					return;
				}

				boolean dragging = draggingProperty().get();
				boolean isFloating = floatingProperty.get();

				if (!dragging) {
					// Dragging not started

					Point2D mousePosition = new Point2D(event.getScreenX(), event.getScreenY());
					if (dragStartPosition == null) {
						dragStartPosition = new Point2D(mousePosition.getX(), mousePosition.getY());

						highlightSource(event, true);

						return;
					}

					double threshold = DRAG_THRESHOLD_PX_TAB_HEADER;

					Object sourceObj = event.getSource();
					if (sourceObj instanceof Node) {
						Node node = (Node) sourceObj;
						ObservableList<String> styles = node.getStyleClass();

						boolean isTitleBar = styles.contains("docknode-title-bar");
						if (isTitleBar) {
							// Is title bar: no threshold

							threshold = DRAG_THRESHOLD_NONE;
						}
					}

					if (mousePosition.distance(dragStartPosition) < threshold) {
						// Do not drag if distance from mouse down is not high enough

						return;
					}

					if (dragWindowOffset == null) {
						if (isFloating && stageFloatable != null) {
							// Floating in stage: Use position of stage
							dragWindowOffset = stageFloatable.getScene().getRoot().screenToLocal(event.getScreenX(), event.getScreenY());
						} else {
							dragWindowOffset = content.screenToLocal(event.getScreenX(), event.getScreenY());

							// Add some position correction to make is a bit nicer
							dragWindowOffset = dragWindowOffset.add(0, 10);
						}
					}

					// Start dragging
					enableDragging();

					double posX = event.getScreenX() - dragWindowOffset.getX();
					double posY = event.getScreenY() - dragWindowOffset.getY();

					if (isFloating) {
						// Floatable

						moveFloatable(posX, posY);
					} else {
						// Preview

						showDraggedNodePreview(posX, posY);
					}

					if (!maximizingProperty().get()) {
						AnchorageSystem.prepareDraggingZoneFor(stationProperty().get(), this);
					}
				} else {
					// Now dragging

					double floatableX = event.getScreenX() - dragWindowOffset.getX();
					double floatableY = event.getScreenY() - dragWindowOffset.getY();

					if (isFloating) {
						// Floatable

						moveFloatable(floatableX, floatableY);
					} else {
						// Preview

						nodePreview.setX(event.getScreenX() - dragWindowOffset.getX());
						nodePreview.setY(event.getScreenY() - dragWindowOffset.getY());
					}

					stationProperty().get().searchTargetNode(event.getScreenX(), event.getScreenY());
					AnchorageSystem.searchTargetNode(event.getScreenX(), event.getScreenY());
				}
			}
		});

		n.setOnMouseReleased(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (draggingProperty().get() && !maximizingProperty().get()) {
					if (nodePreview != null) {
						nodePreview.closeStage();
						nodePreview = null;
					}

					double dropTargetX = event.getScreenX() - dragWindowOffset.getX();
					double dropTargetY = event.getScreenY() - dragWindowOffset.getY();

					Point2D dropTarget = new Point2D(dropTargetX, dropTargetY);

					AnchorageSystem.finalizeDragging(dropTarget);
				}

				dragWindowOffset = null;

				// Reset highlighting
				highlightSource(event, false);
				dragStartPosition = null;
			}
		});
	}

	/**
	 * Give feedback to user: Drag possible
	 */
	private void highlightSource(MouseEvent event, boolean highlighted) {
		Object source = event.getSource();
		if (source instanceof Node) {
			Node node = (Node) source;

			boolean isTitleBar = node.getStyleClass().contains("docknode-title-bar");
			if (isTitleBar) {
				// Do not highlight title bar
				return;
			}

			if (highlighted) {
				Glow glow = new Glow();
				glow.setLevel(0.6);
				node.setEffect(glow);
			} else {
				node.setEffect(null);
			}
		}
	}

	private void callCreationCallBack() {
		if (content.getNodeContent() instanceof DockNodeCreationListener) {
			((DockNodeCreationListener) content.getNodeContent()).onDockNodeCreated(this);
		}
	}

	public void ensureVisibility() {
		if (container.get() instanceof DockTabberContainer) {
			((DockTabberContainer) container.get()).ensureVisibility(this);
		}
	}

	public DockNodeCloseRequestHandler getCloseRequestHandler() {
		return closeRequestHandler;
	}

	public void setCloseRequestHandler(DockNodeCloseRequestHandler handler) {
		Objects.requireNonNull(handler);
		closeRequestHandler = handler;
	}

	public void setDockNodeListener(IDockNodeListener dockNodeListener) {
		this.dockNodeListener = dockNodeListener;
	}

	public BooleanProperty floatableProperty() {
		return floatableProperty;
	}

	public BooleanProperty closeableProperty() {
		return closeableProperty;
	}

	public BooleanProperty resizableProperty() {
		return resizableProperty;
	}

	public BooleanProperty maximizableProperty() {
		return maximizableProperty;
	}

	public ReadOnlyBooleanProperty floatingProperty() {
		return floatingProperty.getReadOnlyProperty();
	}

	public ReadOnlyBooleanProperty draggingProperty() {
		return draggingProperty.getReadOnlyProperty();
	}

	public ReadOnlyBooleanProperty maximizingProperty() {
		return maximizingProperty.getReadOnlyProperty();
	}

	public ReadOnlyObjectProperty<DockContainer> containerProperty() {
		return container.getReadOnlyProperty();
	}

	public void setIcon(Image icon) {
		content.setIcon(icon);
	}

	public void restore() {
		if (draggingProperty.get()) {
			closeFloatingStage();
		}
		disableDragging();
	}

	public boolean isDockVisible() {
		return containerProperty().get() == null || floatingProperty.get() || container.get().isDockVisible(this);
	}

	public boolean isDockDocked() {
		return getParentContainer() != null;
	}

	/**
	 * If this DockNode is in a stage: Close stage
	 */
	public void closeFloatingStage() {
		if (stageFloatable != null) {
			if (dockNodeListener != null) {
				// Listener
				dockNodeListener.dockNodeBeforeCloseStage(stageFloatable);
			}

			stageFloatable.closeStage();
			stageFloatable = null;
		}
		floatingProperty.set(false);
	}

	public StageFloatable getFloatableStage() {
		return stageFloatable;
	}

	private void buildUI(DockUIPanel panel) {
		getChildren().add(panel);
		panel.setDockNode(this);
	}

	public void moveFloatable(double x, double y) {
		if (!maximizingProperty.get()) {
			stageFloatable.move(x, y);
		}
	}

	private void makeGhostFloatable(Window owner, double x, double y) {
		if (!floatingProperty.get()) {
			stageFloatable = new StageFloatable(this, owner, x, y);
			stageFloatable.show();
			makeTransparent();
		}
	}

	public void enableDraggingOnPosition(double x, double y) {
		draggingProperty.set(true);
		makeGhostFloatable(station.get().getScene().getWindow(), x, y);
		if (!maximizingProperty().get()) {
			AnchorageSystem.prepareDraggingZoneFor(station.get(), this);
		}
	}

	public void enableDragging() {
		draggingProperty.set(true);
	}

	public void disableDragging() {
		draggingProperty.set(false);
		makeOpaque();
	}

	private void makeTransparent() {
		content.setOpacity(0.4);
	}

	private void makeOpaque() {
		content.setOpacity(1);
	}

	public void makeNodeActiveOnFloatableStage(Window owner, double x, double y) {
		disableDragging();
		if (!floatingProperty.get()) {
			if (floatableProperty.get()) {
				if (stageFloatable == null) {
					makeGhostFloatable(owner, x, y);
				}
				stageFloatable.makeNodeActiveOnFloatableStage();
				floatingProperty.set(true);
			} else {
				closeFloatingStage();
			}
		}
	}

	/**
	 * Get the value of station
	 *
	 * @return the value of station
	 */
	public ObjectProperty<DockStation> stationProperty() {
		return station;
	}

	@Override
	public DockContainer getParentContainer() {
		return container.get();
	}

	@Override
	public void setParentContainer(DockContainer container) {
		this.container.set(container);
	}

	public void dockAsFloating(Window owner, DockStation station, double x, double y, double width, double height) {
		if (stationProperty().get() != null) {
			ensureVisibility();
			return;
		}

		station.add(this);
		stageFloatable = new StageFloatable(this, owner, x, y);
		stageFloatable.makeNodeActiveOnFloatableStage();
		stageFloatable.show();
		stageFloatable.setWidth(width);
		stageFloatable.setHeight(height);
		floatingProperty.set(true);
		this.station.set(station);
	}

	public void dock(DockStation station, DockPosition position) {
		if (stationProperty().get() != null) {
			ensureVisibility();
			return;
		}
		station.add(this);
		station.putDock(this, position, 0.5);
		this.station.set(station);
	}

	public void dock(DockStation station, DockPosition position, double percentage) {
		if (stationProperty().get() != null) {
			ensureVisibility();
			return;
		}
		station.add(this);
		station.putDock(this, position, percentage);
		this.station.set(station);
	}

	public void dock(DockNode nodeTarget, DockPosition position) {
		dock(nodeTarget, position, 0.5);
	}

	public void dock(DockNode nodeTarget, DockPosition position, double percentage) {
		if (stationProperty().get() != null) {
			ensureVisibility();
			return;
		}
		nodeTarget.stationProperty().get().add(this);
		nodeTarget.getParentContainer().putDock(this, nodeTarget, position, percentage);
		station.set(nodeTarget.station.get());
	}

	public void dock(DockSubStation subStation, DockPosition position) {
		if (stationProperty().get() != null) {
			ensureVisibility();
			return;
		}
		subStation.putDock(this, position, 0.5);
	}

	public void dock(DockSubStation subStation, DockPosition position, double percentage) {
		if (stationProperty().get() != null) {
			ensureVisibility();
			return;
		}
		subStation.putDock(this, position, percentage);
	}

	public void undock() {
		if (stationProperty().get() == null) {
			return;
		}

		boolean isFloating = floatingProperty.get();
		restore();
		if (getParentContainer() != null) {
			getParentContainer().undock(this);
			station.get().remove(this);
		} else if (isFloating) {
			closeFloatingStage();
			station.get().remove(this);
			station.set(null);
		}

		if (dockNodeListener != null) {
			dockNodeListener.dockNodeUndocked(this);
		}
	}

	public DockUIPanel getContent() {
		return content;
	}

	public void destroy() {
		// Close
		undock();

		if (closeRequestHandler != null) {
			closeRequestHandler = null;
		}

		dockNodeListener = null;

		// Clean up content
		if (content != null) {
			content.destroy();
		}

		content = null;
	}

	public void afterDockNodeClosed() {
		if (dockNodeListener != null) {
			dockNodeListener.afterDockNodeClosed(stageFloatable);
		}
	}

	@Override
	public String toString() {
		return content.getTitle();
	}

	public Bounds getSceneBounds() {
		return localToScene(getBoundsInLocal());
	}

	public Bounds getScreenBounds() {
		return localToScreen(getBoundsInLocal());
	}

	public boolean checkForTarget(double x, double y) {
		Scene scene = getScene();
		//		if (scene == null) {
		//			// Cannot find scene
		//			return false;
		//		}

		Point2D screenToScenePoint = scene.getRoot().screenToLocal(x, y);
		Bounds sceneBounds = getSceneBounds();

		if (screenToScenePoint == null) {
			// Workaround, can be null after dock in floatable
			return false;
		}

		return sceneBounds.contains(screenToScenePoint.getX(), screenToScenePoint.getY());
	}

	public boolean insideTabContainer() {
		return container.get() instanceof DockTabberContainer;
	}

	public void maximizeOrRestore() {
		if (maximizingProperty.get()) {
			restoreLayout();
		} else {
			maximizeLayout();
		}
	}

	public void restoreLayout() {
		if (maximizableProperty.get()) {
			if (floatingProperty.get()) {
				stageFloatable.setX(floatingStateCoordinateX);
				stageFloatable.setY(floatingStateCoordinateY);
				stageFloatable.setWidth(floatingStateWidth);
				stageFloatable.setHeight(floatingStateHeight);
				maximizingProperty.set(false);
			} else if (station.get().restore(this)) {
				maximizingProperty.set(false);
			}
		}
	}

	/**
	 * Position StageFloatable in the center of the first Screen
	 * 
	 * @param parentStage Optional, sets the window frame to find the Screen
	 */
	public void centerStageFloatableOnScreen(Stage parentStage) {
		if (stageFloatable == null) {
			return;
		}

		Rectangle2D rectangleOfOrigin = null;
		if (parentStage != null) {
			rectangleOfOrigin = new Rectangle2D(parentStage.getX(), parentStage.getY(), parentStage.getWidth(), parentStage.getHeight());
		} else {
			rectangleOfOrigin = new Rectangle2D(stageFloatable.getX(), stageFloatable.getY(), stageFloatable.getWidth(), stageFloatable.getHeight());
		}

		// Get current screen of the stage
		ObservableList<Screen> screens = Screen.getScreensForRectangle(rectangleOfOrigin);
		if (screens.isEmpty()) {
			return;
		}

		// Change stage properties
		Rectangle2D bounds = screens.get(0).getBounds();

		double width = stageFloatable.getWidth();
		double height = stageFloatable.getHeight();

		double x = (bounds.getWidth() - width) / 2;
		double y = (bounds.getHeight() - height) / 2;

		stageFloatable.setX(x);
		stageFloatable.setY(y);
	}

	private void moveStateToFullScreen() {
		// Get current screen of the stage
		ObservableList<Screen> screens = Screen
				.getScreensForRectangle(new Rectangle2D(stageFloatable.getX(), stageFloatable.getY(), stageFloatable.getWidth(), stageFloatable.getHeight()));
		// Change stage properties
		Rectangle2D bounds = screens.get(0).getBounds();
		stageFloatable.setX(bounds.getMinX() - FLOATING_NODE_DROPSHADOW_RADIUS);
		stageFloatable.setY(bounds.getMinY() - FLOATING_NODE_DROPSHADOW_RADIUS);
		stageFloatable.setWidth(bounds.getWidth() + FLOATING_NODE_DROPSHADOW_RADIUS * 2);
		stageFloatable.setHeight(bounds.getHeight() + FLOATING_NODE_DROPSHADOW_RADIUS * 2);
	}

	public void maximizeLayout() {
		if (maximizableProperty.get()) {
			if (floatingProperty.get()) {
				floatingStateCoordinateX = stageFloatable.getX();
				floatingStateCoordinateY = stageFloatable.getY();
				floatingStateWidth = stageFloatable.getWidth();
				floatingStateHeight = stageFloatable.getHeight();
				moveStateToFullScreen();
				maximizingProperty.set(true);
			} else if (station.get().maximize(this)) {
				maximizingProperty.set(true);
			}
		}
	}

	public boolean isMenuButtonEnable() {
		return content.isMenuButtonEnable();
	}

	public void setMaximizeOnDoubleClick(boolean maximizeOnDoubleClick) {
		this.maximizeOnDoubleClick = maximizeOnDoubleClick;
	}

	public enum DockPosition {
		LEFT, RIGHT, TOP, BOTTOM, CENTER
	}
}
