package com.rwu.fx.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rwu.misc.BooleanModel;
import com.sun.javafx.scene.text.FontHelper;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.tk.Toolkit;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class FxLayoutUtils {
	public static int SPACING_LINES_SMALLER = 3;
	public static int SPACING_COLUMNS_SMALLER = 3;

	public static int SPACING_LINES_DEFAULT = 6;
	public static int SPACING_COLUMNS_DEFAULT = 6;

	public static int TEXTFIELD_WIDTH_MIN = 200;

	public static int SPACING_MORE = 10;

	public static void setDefaultSpacing(HBox box) {
		box.setSpacing(SPACING_LINES_DEFAULT);
	}

	public static void setDefaultSpacing(VBox box) {
		box.setSpacing(SPACING_LINES_DEFAULT);
	}

	public static void setDefaultColumnSpacing(GridPane gridPane) {
		gridPane.setHgap(SPACING_COLUMNS_DEFAULT);
	}

	public static void setDefaultLineSpacing(GridPane gridPane) {
		gridPane.setVgap(SPACING_LINES_DEFAULT);
	}

	public static void setDefaultLineSpacing(VBox box) {
		box.setSpacing(SPACING_LINES_DEFAULT);
	}

	public static void setDefaultSpacing(GridPane gridPane) {
		setDefaultLineSpacing(gridPane);

		FxLayoutUtils.setDefaultColumnSpacing(gridPane);
	}

	public static void setSpacingSmaller(GridPane gridPane) {
		gridPane.setVgap(SPACING_LINES_SMALLER);
		gridPane.setHgap(SPACING_COLUMNS_SMALLER);
	}

	public static void setLineSpacing(GridPane gridPane, int spacing) {
		gridPane.setVgap(spacing);
	}

	public static void setLineSpacing(VBox box, int spacing) {
		box.setSpacing(spacing);
	}

	public static void setFill(Node node) {
		GridPane.setHgrow(node, Priority.ALWAYS);
		GridPane.setVgrow(node, Priority.ALWAYS);

		GridPane.setFillWidth(node, true);
		GridPane.setFillHeight(node, true);
	}

	public static void setFillWidth(Node node) {
		GridPane.setHgrow(node, Priority.ALWAYS);

		GridPane.setFillWidth(node, true);
	}

	public static void ensureSameWidth(List<IControlArray> rows) {
		Map<Integer, Double> widths = new HashMap<>();

		for (IControlArray row : rows) {
			int i = 0;
			for (Control col : row.getControls()) {
				Double current = widths.get(i);
				if (current == null) {
					current = 0d;
				}

				double width = col.getWidth();

				current = Math.max(width, current);
				widths.put(i, current);

				i++;
			}
		}

		for (IControlArray row : rows) {
			int i = 0;
			for (Control col : row.getControls()) {
				Double current = widths.get(i);

				if (current > 1) {
					col.setPrefWidth(current);
					col.setMinWidth(current);
				}

				i++;
			}
		}
	}

	public interface IControlArray {
		public List<Control> getControls();
	}

	public static void ensureFullWidth(Label label) {
		ensureFullWidth(label, -1);
	}

	/**
	 * Calculate width of text and set label width
	 */
	public static void ensureFullWidth(Label label, double fontSize) {
		Font font = label.getFont();
		String text = label.getText();

		label.setPadding(new Insets(0));

		if (fontSize != -1) {
			font = Font.font(font.getName(), fontSize);
		}

		TextLayout layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();

		layout.setContent(text != null ? text : "", FontHelper.getNativeFont(font));
		//layout.setWrapWidth((float) wrappingWidth);

		double width = layout.getBounds().getWidth();
		if (width > 0) {
			double targetWidth = width + 4;

			label.setMinWidth(targetWidth);
		}
	}

	public static void ensureFullHeight(Label label) {
		Font font = label.getFont();
		String text = label.getText();

		TextLayout layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();

		layout.setContent(text != null ? text : "", FontHelper.getNativeFont(font));
		//layout.setWrapWidth((float) wrappingWidth);

		double height = layout.getBounds().getHeight();
		if (height > 0) {
			label.setMinWidth(height + 4);
		}
	}

	public static void setGrowWithParent(Region region, FlowPane flowPane) {
		region.widthProperty().addListener(e -> {
			flowPane.prefWidthProperty().set(region.getWidth() - 80);
		});
	}

	public static Pane getEmptyPane(double width, double height) {
		if (width == -1) {
			width = 1;
		}
		if (height == -1) {
			height = 1;
		}

		Pane pane = new Pane();
		pane.setMinWidth(width);
		pane.setMaxWidth(width);

		pane.setMinHeight(height);
		pane.setMaxHeight(height);

		return pane;
	}

	/**
	 * Minimum width of scroll area when vertical scroll bar visible
	 */
	public static void scrollPaneAutoLayout(ScrollPane scrollPane, int minWidth) {
		BooleanModel firstShow = new BooleanModel(true);

		scrollPane.heightProperty().addListener(e -> {
			if (firstShow.isYes()) {
				firstShow.no();

				return;
			}

			for (Node node : scrollPane.lookupAll(".scroll-bar")) {
				if (node instanceof ScrollBar) {
					ScrollBar scrollBar = (ScrollBar) node;
					if (scrollBar.getOrientation() == Orientation.HORIZONTAL) {
					}

					if (scrollBar.getOrientation() == Orientation.VERTICAL) {
						// Do something with the vertical scroll bar

						boolean visible = scrollBar.isVisible();

						if (visible) {
							scrollPane.setMinWidth(minWidth);
						} else {
							scrollPane.setMinWidth(-1);
						}

						scrollPane.getParent().layout();
					}

				}
			}
		});
	}
}
