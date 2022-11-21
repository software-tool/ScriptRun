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
package com.anchorage.docks.containers;

import static com.anchorage.docks.containers.common.AnchorageSettings.FLOATING_NODE_DROPSHADOW_RADIUS;
import static com.anchorage.docks.containers.common.AnchorageSettings.FLOATING_NODE_MINIMUM_HEIGHT;
import static com.anchorage.docks.containers.common.AnchorageSettings.FLOATING_NODE_MINIMUM_WIDTH;

import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.system.AnchorageSystem;
import com.anchorage.system.interfaces.IDockGlobalListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Floatable: Area to move around dock nodes
 */
public class StageFloatable extends Stage {

	/** Number of pixels at the borders where resize is triggered */
	private static final int RESIZE_BORDER_WIDTH = 4;

	private DockNode node;
	private StackPane transparentRootPanel;
	private StackPane stackPanelContainer;
	private Window owner;
	private double startX;
	private double startWidth;
	private double startY;
	private double startHeight;
	private ImageView imageView;

	private ChangeListener<Boolean> focusedListener = null;

	/** State if resizing of the stage is performed */
	private ResizeState resizeState = new ResizeState();

	private boolean cssApplied = false;

	public StageFloatable(DockNode node, Window owner, double startX, double startY) {
		super();
		this.node = node;
		this.owner = owner;
		buildUI(startX, startY);

		initListeners();
	}

	private void setupMouseEvents() {
		EventHandler<MouseEvent> eventsHandler = event -> {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				startWidth = getWidth();
				startX = getX();
				startHeight = getHeight();
				startY = getY();
			}

			if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				if (resizeState.NE()) {
					// Do not close with cursor of resizing

					event.consume();
				}
			}

			if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
				// Mouse moved: Change cursor to indicate if resizing is possible

				boolean sizeRight = valueInRange(event.getX(), stackPanelContainer.getWidth() - Math.max(stackPanelContainer.getPadding().getLeft(), RESIZE_BORDER_WIDTH),
						stackPanelContainer.getWidth());
				boolean sizeLeft = valueInRange(event.getX(), 0, Math.max(stackPanelContainer.getPadding().getRight(), RESIZE_BORDER_WIDTH));
				boolean sizeTop = valueInRange(event.getY(), 0, Math.max(stackPanelContainer.getPadding().getTop(), RESIZE_BORDER_WIDTH));
				boolean sizeBottom = valueInRange(event.getY(), stackPanelContainer.getHeight() - Math.max(stackPanelContainer.getPadding().getBottom(), RESIZE_BORDER_WIDTH),
						stackPanelContainer.getHeight());

				changeCursor(sizeLeft, sizeRight, sizeTop, sizeBottom);
				getScene().setCursor(resizeState.cursor);
			}

			if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && resizeState.isResizing()) {
				// Dragging: Apply resizing

				double width = calcuateWidth(event);
				double height = calcuateHeight(event);

				boolean updateWidth = false;
				boolean updateHeight = false;

				if (resizeState.SE()) {
					updateWidth = true;
					updateHeight = true;
				}

				if (resizeState.NE()) {
					updateWidth = true;
				}
				if (resizeState.E()) {
					updateWidth = true;
				}

				if (resizeState.S() || resizeState.SW()) {
					updateHeight = true;
				}

				if (updateWidth && changeWidth(event)) {
					setWidth(width);
				}
				if (updateHeight && changeHeight(event)) {
					setHeight(height);
				}

				boolean changesX = resizeState.W() || resizeState.NW() || resizeState.SW();
				boolean changesY = resizeState.N() || resizeState.NE() || resizeState.NW();

				if (changesX) {
					double newX = event.getScreenX() - FLOATING_NODE_DROPSHADOW_RADIUS;
					double newWidth = startX - newX + startWidth;
					if (newWidth > FLOATING_NODE_MINIMUM_WIDTH) {
						setX(newX);
						setWidth(newWidth);
					}

					if (resizeState.NW() && changeHeight(event)) {
						setHeight(height);
					}
				}

				if (changesY) {
					double newY = event.getScreenY() - FLOATING_NODE_DROPSHADOW_RADIUS;
					double newHeight = startY - newY + startHeight;
					if (newHeight > FLOATING_NODE_MINIMUM_HEIGHT) {
						setY(newY);
						setHeight(newHeight);
					}
				}
			}
		};

		stackPanelContainer.addEventFilter(MouseEvent.MOUSE_PRESSED, eventsHandler);
		stackPanelContainer.addEventFilter(MouseEvent.MOUSE_MOVED, eventsHandler);
		stackPanelContainer.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventsHandler);
		stackPanelContainer.addEventFilter(MouseEvent.MOUSE_RELEASED, eventsHandler);
	}

	private boolean changeHeight(MouseEvent event) {
		return event.getScreenY() - getY() + FLOATING_NODE_DROPSHADOW_RADIUS > FLOATING_NODE_MINIMUM_HEIGHT;
	}

	private boolean changeWidth(MouseEvent event) {
		return event.getScreenX() - getX() + FLOATING_NODE_DROPSHADOW_RADIUS > FLOATING_NODE_MINIMUM_WIDTH;
	}

	private double calcuateHeight(MouseEvent event) {
		return event.getScreenY() - getY() + FLOATING_NODE_DROPSHADOW_RADIUS;
	}

	private double calcuateWidth(MouseEvent event) {
		return event.getScreenX() - getX() + FLOATING_NODE_DROPSHADOW_RADIUS;
	}

	public boolean inResizing() {
		return (getScene().getCursor() != null && getScene().getCursor() != Cursor.DEFAULT);
	}

	private void changeCursor(boolean sizeLeft, boolean sizeRight, boolean sizeTop, boolean sizeBottom) {
		Cursor cursor = Cursor.DEFAULT;
		ResizeMode mode = null;

		if (sizeLeft) {
			if (sizeTop) {
				cursor = Cursor.NW_RESIZE;

				mode = ResizeMode.NW;
			} else if (sizeBottom) {
				cursor = Cursor.SW_RESIZE;

				mode = ResizeMode.SW;
			} else {
				cursor = Cursor.W_RESIZE;

				mode = ResizeMode.W;
			}
		} else if (sizeRight) {
			if (sizeTop) {
				cursor = Cursor.NE_RESIZE;

				mode = ResizeMode.NE;
			} else if (sizeBottom) {
				cursor = Cursor.SE_RESIZE;

				mode = ResizeMode.SE;
			} else {
				cursor = Cursor.E_RESIZE;

				mode = ResizeMode.E;
			}
		} else if (sizeTop) {
			cursor = Cursor.N_RESIZE;

			mode = ResizeMode.N;
		} else if (sizeBottom) {
			cursor = Cursor.S_RESIZE;

			mode = ResizeMode.S;
		}

		resizeState.cursor = cursor;
		resizeState.mode = mode;
	}

	private boolean valueInRange(double value, double min, double max) {
		return (value >= min && value <= max);
	}

	private void buildUI(double startX, double startY) {
		initOwner(owner);

		setX(startX - FLOATING_NODE_DROPSHADOW_RADIUS);
		setY(startY - FLOATING_NODE_DROPSHADOW_RADIUS);
		createContainerPanel();

		//initStyle(StageStyle.UNDECORATED);
		initStyle(StageStyle.TRANSPARENT);

		Scene scene = new Scene(transparentRootPanel, node.getWidth() + FLOATING_NODE_DROPSHADOW_RADIUS * 2, node.getHeight() + FLOATING_NODE_DROPSHADOW_RADIUS * 2,
				Color.TRANSPARENT);
		setOnShown(e -> {
			setWidth(getWidth() + stackPanelContainer.getPadding().getLeft() + stackPanelContainer.getPadding().getRight());
			setHeight(getHeight() + stackPanelContainer.getPadding().getTop() + stackPanelContainer.getPadding().getBottom());
		});
		setScene(scene);
	}

	private void initListeners() {
		focusedListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
				// Inform listeners about focus change
				for (IDockGlobalListener listener : AnchorageSystem.getGlobalListeners()) {
					listener.floatableFocusGained(StageFloatable.this);
				}
			}
		};
		focusedProperty().addListener(focusedListener);
	}

	private void createContainerPanel() {
		WritableImage ghostImage = node.snapshot(new SnapshotParameters(), null);

		imageView = new ImageView(ghostImage);

		stackPanelContainer = new StackPane(imageView);
		transparentRootPanel = new StackPane(stackPanelContainer);
		transparentRootPanel.setPadding(new Insets(FLOATING_NODE_DROPSHADOW_RADIUS));
		transparentRootPanel.setStyle("-fx-background-color:rgba(0,0,0,0);");
		stackPanelContainer.getStyleClass().add("docknode-floating-stack-container-panel");
		stackPanelContainer.setEffect(new DropShadow(BlurType.GAUSSIAN, new Color(0, 0, 0, 0.6), FLOATING_NODE_DROPSHADOW_RADIUS, 0.2, 0, 0));
		stackPanelContainer.relocate(FLOATING_NODE_DROPSHADOW_RADIUS, FLOATING_NODE_DROPSHADOW_RADIUS);
	}

	public void move(double x, double y) {
		setX(x);
		setY(y);
	}

	public void makeNodeActiveOnFloatableStage() {
		DockStation station = node.stationProperty().get(); // save the station
		node.undock();
		node.stationProperty().set(station); // resume station
		stackPanelContainer.getChildren().remove(imageView);
		stackPanelContainer.getChildren().add(node);
		if (node.resizableProperty().get()) {
			setupMouseEvents();
		}
	}

	public void closeStage() {
		// Reset focused listener
		if (focusedListener != null) {
			focusedProperty().removeListener(focusedListener);
			focusedListener = null;
		}

		transparentRootPanel.getChildren().removeAll();
		setScene(null);
		hide();
	}

	public Insets getPaddingOffset() {
		return stackPanelContainer.getPadding();
	}

	public boolean getCssApplied() {
		return cssApplied;
	}

	public void setCssApplied(boolean cssApplied) {
		this.cssApplied = cssApplied;
	}

	/**
	 * The current state of resizing
	 * 
	 * @author richard-lllll
	 */
	protected class ResizeState {

		public ResizeMode mode = null;
		public Cursor cursor = null;

		public boolean isResizing() {
			return mode != null;
		}

		public boolean E() {
			return cursorEquals(ResizeMode.E);
		}

		public boolean W() {
			return cursorEquals(ResizeMode.W);
		}

		public boolean N() {
			return cursorEquals(ResizeMode.N);
		}

		public boolean S() {
			return cursorEquals(ResizeMode.S);
		}

		public boolean NW() {
			return cursorEquals(ResizeMode.NW);
		}

		public boolean SW() {
			return cursorEquals(ResizeMode.SW);
		}

		public boolean NE() {
			return cursorEquals(ResizeMode.NE);
		}

		public boolean SE() {
			return cursorEquals(ResizeMode.SE);
		}

		private boolean cursorEquals(ResizeMode other) {
			Cursor cursor = StageFloatable.this.getScene().getCursor();

			if (other == ResizeMode.E) {
				return cursor == Cursor.E_RESIZE;
			}
			if (other == ResizeMode.W) {
				return cursor == Cursor.W_RESIZE;
			}
			if (other == ResizeMode.N) {
				return cursor == Cursor.N_RESIZE;
			}
			if (other == ResizeMode.S) {
				return cursor == Cursor.S_RESIZE;
			}
			if (other == ResizeMode.NW) {
				return cursor == Cursor.NW_RESIZE;
			}
			if (other == ResizeMode.SW) {
				return cursor == Cursor.SW_RESIZE;
			}
			if (other == ResizeMode.NE) {
				return cursor == Cursor.NE_RESIZE;
			}
			if (other == ResizeMode.SE) {
				return cursor == Cursor.SE_RESIZE;
			}

			return false;
		}
	}

	/**
	 * Directions of resizing
	 * 
	 * @author richard-lllll
	 */
	protected enum ResizeMode {
		// West
		W,
		// East
		E,

		// North
		N, NW, NE,
		// South
		S, SW, SE,
	}
}
