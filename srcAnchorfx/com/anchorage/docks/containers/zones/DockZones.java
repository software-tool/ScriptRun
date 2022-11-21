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
package com.anchorage.docks.containers.zones;

import static com.anchorage.docks.containers.common.AnchorageSettings.FLOATING_NODE_DROPSHADOW_RADIUS;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.anchorage.docks.containers.common.AnchorageSettings;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.node.DockNode.DockPosition;
import com.anchorage.docks.node.ui.DockUIPanel;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.docks.stations.DockSubStation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * The visible drop targets
 *
 * @author avinerbi
 */
public final class DockZones extends Stage {

	public static final int OFFSET_IMAGE = 15;

	///////// MAIN STAGE SCENE AND ROOT
	private Scene scene;
	private Pane mainRoot;

	////////////////////////////////////
	/////////// CIRCLE EXTERNAL STAGE
	private Stage circleStage;
	private Scene circleStageScene;
	private Pane circleStageRoot;
	private Circle circleZone;
	////////////////////////////////////

	private static Image dragTopImage;
	private static Image dragBottomImage;
	private static Image dragLeftImage;
	private static Image dragRightImage;
	private static Image dragCenterImage;

	private List<ZoneSelector> selectors;

	///////////////////////////////////////////////
	private DockStation ownerStation;
	private Rectangle currentAreaSelected;

	private DockNode currentNodeTarget;
	private DockNode nodeToMove;
	private DockNode.DockPosition currentPosition;

	private ZoneSelector currentZoneSelector = null;

	private Rectangle rectanglePreview;
	private Timeline opacityAnimationPreview;

	static {
		dragTopImage = new Image("dragtop.png");
		dragBottomImage = new Image("dragbottom.png");
		dragLeftImage = new Image("dragleft.png");
		dragRightImage = new Image("dragright.png");
		dragCenterImage = new Image("dragcenter.png");
	}

	public DockZones(DockStation station, DockNode nodeToMove) {
		this.nodeToMove = nodeToMove;
		this.ownerStation = station;

		if (AnchorageSettings.INIT_OWNER_FOR_DOCK_ZONES) {
			initOwner(ownerStation.getStationWindow());
		}
		initStyle(StageStyle.TRANSPARENT);

		buildUI();

		buildCircleStage();
		makeSelectors();
		createRectangleForPreview();

		setAlwaysOnTop(true);
		circleStage.setAlwaysOnTop(true);
	}

	private void createRectangleForPreview() {
		rectanglePreview = new Rectangle(0, 0, 50, 50);
		rectanglePreview.getStyleClass().add("dockzone-rectangle-preview");
		rectanglePreview.setOpacity(0);

		opacityAnimationPreview = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(rectanglePreview.opacityProperty(), 0.5, Interpolator.LINEAR)));

		opacityAnimationPreview.setAutoReverse(true);
		opacityAnimationPreview.setCycleCount(-1);

		mainRoot.getChildren().add(rectanglePreview);

	}

	private void makeSelectors() {
		selectors = new ArrayList<>();

		double rootWidth = mainRoot.getWidth();
		double rootHeight = mainRoot.getHeight();

		double circleWidth = circleStageRoot.getWidth();
		double circleHeight = circleStageRoot.getHeight();

		if (ownerStation.getChildren().size() > 0) {
			// selectors of station
			addSelector(dragTopImage, DockNode.DockPosition.TOP, true, mainRoot, (rootWidth - dragTopImage.getWidth()) / 2, OFFSET_IMAGE);
			addSelector(dragBottomImage, DockNode.DockPosition.BOTTOM, true, mainRoot, (rootWidth - dragBottomImage.getWidth()) / 2,
					rootHeight - dragBottomImage.getHeight() - OFFSET_IMAGE);
			addSelector(dragLeftImage, DockNode.DockPosition.LEFT, true, mainRoot, OFFSET_IMAGE, (rootHeight - dragLeftImage.getWidth()) / 2);
			addSelector(dragRightImage, DockNode.DockPosition.RIGHT, true, mainRoot, (rootWidth - dragRightImage.getWidth() - OFFSET_IMAGE),
					(rootHeight - dragRightImage.getWidth()) / 2);

			// selectors of node
			if (!AnchorageSettings.DROP_ZONES_SIMLIFIED) {
				addSelector(dragTopImage, DockNode.DockPosition.TOP, false, circleStageRoot, (circleWidth - dragTopImage.getWidth()) / 2, OFFSET_IMAGE);
				addSelector(dragBottomImage, DockNode.DockPosition.BOTTOM, false, circleStageRoot, (circleWidth - dragBottomImage.getWidth()) / 2,
						circleHeight - dragBottomImage.getHeight() - OFFSET_IMAGE);
				addSelector(dragLeftImage, DockNode.DockPosition.LEFT, false, circleStageRoot, OFFSET_IMAGE, (circleHeight - dragLeftImage.getHeight()) / 2);
				addSelector(dragRightImage, DockNode.DockPosition.RIGHT, false, circleStageRoot, circleWidth - dragRightImage.getWidth() - OFFSET_IMAGE,
						(circleHeight - dragRightImage.getHeight()) / 2);
			}
			addSelector(dragCenterImage, DockNode.DockPosition.CENTER, false, circleStageRoot, (circleWidth - dragCenterImage.getWidth()) / 2,
					(circleHeight - dragCenterImage.getHeight()) / 2);

		} else {
			selectors.add(new ZoneSelector(dragCenterImage, DockNode.DockPosition.CENTER, true, mainRoot, (rootWidth - dragCenterImage.getWidth()) / 2,
					(rootHeight - dragCenterImage.getHeight()) / 2));
		}
	}

	private void addSelector(Image image, DockPosition dockPosition, boolean stationZone, Pane parent, double x, double y) {
		ZoneSelector selector = new ZoneSelector(image, dockPosition, stationZone, parent, x, y);

		selectors.add(selector);
	}

	private void buildCircleStage() {
		double circleRadius = getCirleRadius();

		circleStage = new Stage();
		circleStage.initStyle(StageStyle.TRANSPARENT);
		circleStage.initOwner(this);

		circleZone = new Circle(circleRadius);
		circleZone.setCenterX(circleRadius);
		circleZone.setCenterY(circleRadius);
		circleZone.getStyleClass().add("dockzone-circle-container-selectors");

		circleStageRoot = new Pane(circleZone);
		circleStageRoot.setStyle("-fx-background-color:rgba(0,0,0,0);");

		circleStageScene = new Scene(circleStageRoot, circleRadius * 2, circleRadius * 2, Color.TRANSPARENT);

		circleStage.setScene(circleStageScene);

		circleStageRoot.setOpacity(0);
	}

	private void buildUI() {
		// Full size content

		mainRoot = new Pane();
		mainRoot.setStyle("-fx-background-color:rgba(0,0,0,0);");

		scene = new Scene(mainRoot, ownerStation.getWidth(), ownerStation.getHeight(), Color.TRANSPARENT);

		setScene(scene);

		if (AnchorageSettings.DOCK_ZONES_ICON != null) {
			getIcons().add(AnchorageSettings.DOCK_ZONES_ICON);
		}
		if (AnchorageSettings.DOCK_ZONES_TEXT != null) {
			setTitle(AnchorageSettings.DOCK_ZONES_TEXT);
		}
		if (AnchorageSettings.DOCK_ZONES_CSS != null) {
			addCss(AnchorageSettings.DOCK_ZONES_CSS);
		}

		Point2D screenOrigin = ownerStation.localToScreen(ownerStation.getBoundsInLocal().getMinX(), ownerStation.getBoundsInLocal().getMinY());
		if (screenOrigin == null) {
			// Cannot find
			return;
		}

		setX(screenOrigin.getX());
		setY(screenOrigin.getY());
	}

	private void addCss(URL resource) {
		String externalForm = resource.toExternalForm();
		scene.getStylesheets().add(externalForm);
	}

	public void showZones() {
		show();
		circleStage.show();
	}

	public void moveAt(DockNode node) {

		currentNodeTarget = node;

		Bounds screenBounds = node.getScreenBounds();

		if (circleStageRoot.opacityProperty().get() == 0) {
			circleStageRoot.setOpacity(1);
		}

		circleStage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - circleStageRoot.getWidth()) / 2);
		circleStage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - circleStageRoot.getHeight()) / 2);

	}

	public void hideCircleZones() {

		hidePreview();

		currentNodeTarget = null;

		if (currentZoneSelector != null) {
			currentZoneSelector.reset();
		}

		currentZoneSelector = null;

		currentPosition = null;
		circleStageRoot.setOpacity(0);
	}

	private void checkVisibilityConditions() {
		selectors.stream().forEach(z -> z.setZoneDisabled(false));

		if (currentNodeTarget == nodeToMove) {
			// disable border zones
			selectors.stream().filter(z -> !z.isStationZone() && z.getPosition() != DockNode.DockPosition.CENTER).forEach(z -> z.setZoneDisabled(true));
		}
	}

	public boolean searchArea(double x, double y) {

		checkVisibilityConditions();

		ZoneSelector selector = selectors.stream().filter(s -> s.overMe(x, y) && !s.isZoneDisabled()).findFirst().orElse(null);

		highLight(selector);

		//        if (selector != null && selector != currentZoneSelector && currentNodeTarget != null) {
		if (selector != null && selector != currentZoneSelector) {
			currentZoneSelector = selector;
			makePreview(currentZoneSelector, currentNodeTarget);
			currentPosition = currentZoneSelector.getPosition();

		} else if (selector == null) {
			hidePreview();
			currentZoneSelector = null;
			currentPosition = null;
		}
		return selector != null;
	}

	public DockNode getCurrentNodeTarget() {
		return currentNodeTarget;
	}

	public ZoneSelector getCurrentZoneSelector() {
		return currentZoneSelector;
	}

	public DockNode getNodeSource() {
		return nodeToMove;
	}

	public DockNode.DockPosition getCurrentPosition() {
		return currentPosition;
	}

	private void highLight(ZoneSelector selector) {

		selectors.stream().forEach(s -> s.reset());

		if (selector != null) {
			if (selector.isStationZone()) {
				circleStageRoot.setOpacity(0);
			} else {
				circleStageRoot.setOpacity(1);
			}
			selector.highLight();
		} else {
			if (rectanglePreview != null) {
				hidePreview();
			}
			currentNodeTarget = null;
		}
	}

	private DockStation getStation() {
		return ownerStation;
	}

	private void hidePreview() {
		if (AnchorageSettings.isDockingPositionPreview()) {
			stopAnimatePreview();
			rectanglePreview.setOpacity(0);
		}
	}

	private void showPreview(Bounds sceneBounds, ZoneSelector selector) {

		if (selector.getPosition() == DockNode.DockPosition.LEFT) {

			rectanglePreview.setX(sceneBounds.getMinX());
			rectanglePreview.setY(sceneBounds.getMinY());
			rectanglePreview.setWidth(sceneBounds.getWidth() / 2);
			rectanglePreview.setHeight(sceneBounds.getHeight());
		}

		if (selector.getPosition() == DockNode.DockPosition.TOP) {

			rectanglePreview.setX(sceneBounds.getMinX());
			rectanglePreview.setY(sceneBounds.getMinY());
			rectanglePreview.setWidth(sceneBounds.getWidth());
			rectanglePreview.setHeight(sceneBounds.getHeight() / 2);

		}

		if (selector.getPosition() == DockNode.DockPosition.RIGHT) {

			rectanglePreview.setX(sceneBounds.getMinX() + sceneBounds.getWidth() / 2);
			rectanglePreview.setY(sceneBounds.getMinY());
			rectanglePreview.setWidth(sceneBounds.getWidth() / 2);
			rectanglePreview.setHeight(sceneBounds.getHeight());
		}

		if (selector.getPosition() == DockNode.DockPosition.BOTTOM) {

			rectanglePreview.setX(sceneBounds.getMinX());
			rectanglePreview.setY(sceneBounds.getMinY() + sceneBounds.getHeight() / 2);
			rectanglePreview.setWidth(sceneBounds.getWidth());
			rectanglePreview.setHeight(sceneBounds.getHeight() / 2);

		}

		if (selector.getPosition() == DockNode.DockPosition.CENTER) {

			rectanglePreview.setX(sceneBounds.getMinX());
			rectanglePreview.setY(sceneBounds.getMinY());
			rectanglePreview.setWidth(sceneBounds.getWidth());
			rectanglePreview.setHeight(sceneBounds.getHeight());
		}
	}

	private void animatePreview() {
		stopAnimatePreview();
		rectanglePreview.setOpacity(1);
		rectanglePreview.toFront();
		opacityAnimationPreview.play();
	}

	private void stopAnimatePreview() {
		opacityAnimationPreview.stop();
	}

	private void makePreview(ZoneSelector selector, DockNode currentNodeTarget) {

		if (AnchorageSettings.isDockingPositionPreview()) {

			if (selector.isStationZone()) {
				Bounds sceneBounds = ownerStation.getBoundsInParent();
				showPreview(sceneBounds, selector);
			} else {
				if (currentNodeTarget == null || ownerStation.getBoundsInLocal() == null) {
					return;
				}
				Bounds nodeSceneBounds = currentNodeTarget.localToScene(currentNodeTarget.getBoundsInLocal());

				Bounds stationSceneBounds = ownerStation.localToScene(ownerStation.getBoundsInLocal());

				Bounds sceneBounds = new BoundingBox(nodeSceneBounds.getMinX() - stationSceneBounds.getMinX(), nodeSceneBounds.getMinY() - stationSceneBounds.getMinY(),
						nodeSceneBounds.getWidth(), nodeSceneBounds.getHeight());

				if (ownerStation.isSubStation()) {
					DockSubStation subStationNode = ownerStation.getDockNodeForSubStation();

					if (subStationNode.floatingProperty().get()) {
						sceneBounds = new BoundingBox(sceneBounds.getMinX() - FLOATING_NODE_DROPSHADOW_RADIUS - subStationNode.getFloatableStage().getPaddingOffset().getLeft(),
								sceneBounds.getMinY() - FLOATING_NODE_DROPSHADOW_RADIUS - subStationNode.getFloatableStage().getPaddingOffset().getTop() - DockUIPanel.BAR_HEIGHT,
								sceneBounds.getWidth(), sceneBounds.getHeight());
					}
				}
				showPreview(sceneBounds, selector);
			}

			animatePreview();
		}

	}

	private static double getCirleRadius() {
		if (AnchorageSettings.DROP_ZONES_SIMLIFIED) {
			return 60;
		}

		return 100;
	}
}
