package script.frame.recent;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxLayoutUtils;
import com.rwu.misc.DateUtils;
import com.rwu.misc.StringUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.tbee.javafx.scene.layout.MigPane;
import script.constant.MigLayoutConstants;
import script.controller.ControllerScripts;
import script.data.Script;
import script.frame.intf.IContent;
import script.lang.K;
import script.recent.ScriptRecent;
import script.recent.ScriptRecentManager;
import script.recent.ScriptValue;

public class PaneScriptRecent implements IContent {

    private IScriptRecentContainer parent;

    private BorderPane pane = new BorderPane();
    private HBox paneInfo = new HBox();

    private GridPane gridParameters = new GridPane();

    private ScriptRecent scriptRecent;

    private Label labelDay = new Label();
    private Label labelTime = new Label();

    private Button buttonRemove = new Button();

    public PaneScriptRecent(IScriptRecentContainer parent, ScriptRecent scriptRecent, boolean showFullPath) {
        this.parent = parent;
        this.scriptRecent = scriptRecent;

        //String filepath = showFullPath ? scriptRecent.getFile().getAbsolutePath() : scriptRecent.getFile().getName();
        //String path = StringUtils.getReducedText(filepath, 120, 1);

        labelDay.setText(DateUtils.formatDateOnly(scriptRecent.getTime()));
        labelTime.setText(DateUtils.formatTimeOnly(scriptRecent.getTime()));

        buttonRemove.setOnAction(e -> {
            ScriptRecentManager.removeRecent(scriptRecent);

            parent.fill();
        });

        FxIconUtils.setIconNoStyleWithSize(buttonRemove, "bin_closed.png", 20);
    }

    public void addHeader(MigPane grid) {
        Label dummy1 = new Label();
        Label dummy2 = new Label(Ln.get(K.Date));
        Label dummy3 = new Label(Ln.get(K.Time));

        addInCell(grid, dummy1, true, true, false);
        addInCell(grid, dummy2, false, true, false);
        addInCell(grid, dummy3, false, true, false);

        for (ScriptValue value : scriptRecent.getValues()) {
            Label labelName = new Label();
            labelName.setText(value.getIndex()+"");

            addInCell(grid, labelName, false, true, false);
        }

        addInCell(grid, new Label(), false, false, true);
    }

    public void fillInTable(MigPane grid) {
        addRunScriptCell(grid);

        addInCell(grid, labelDay);

        addInCell(grid, labelTime);

        for (ScriptValue value : scriptRecent.getValues()) {
            Label labelValue = new Label();

            String valueReduced = StringUtils.getReducedText(value.getStringValue(), 120, 3);
            labelValue.setText(valueReduced);

            addInCell(grid, labelValue);
        }

        addRemoveRecentCell(grid);
    }

    private void addInCell(MigPane table, Node content) {
        addInCell(table, content, false, false, false);
    }

    /**
     * Cell to run script
     */
    private void addRunScriptCell(MigPane table) {
        Pane pane = new Pane();

        ImageView imageView = FxIconUtils.getImageView("bullet_arrow_right.png");

        pane.getStyleClass().add("pane-recent-cell");
        pane.getStyleClass().add("pane-recent-cell-execute");
        pane.getStyleClass().add("pane-recent-cell-border-left");

        pane.getChildren().add(imageView);

        pane.setOnMouseClicked(e -> {
            ControllerScripts.openDetails(new Script(scriptRecent.getFile()), scriptRecent);
        });

        table.add(pane, MigLayoutConstants.RECENT_CELL);
    }

    private void addRemoveRecentCell(MigPane table) {
        MigPane pane = new MigPane();

        ImageView imageView = FxIconUtils.getImageView("broom.png");
        imageView.setFitWidth(18);
        imageView.setFitHeight(18);

        pane.getStyleClass().add("pane-recent-cell");
        pane.getStyleClass().add("pane-recent-cell-remove");
        pane.getStyleClass().add("pane-recent-cell-border-top");

        pane.add(imageView, "gapleft 1, gaptop 1, gapright 1");

        pane.setOnMouseClicked(e -> {
            ScriptRecentManager.removeRecent(scriptRecent);

            parent.fill();
        });

        FxTooltip.setTooltip(pane, Ln.get(K.DeleteRecent));

        table.add(pane, MigLayoutConstants.RECENT_CELL + ", wrap");
    }

    private void addInCell(MigPane table, Node content, boolean borderLeft, boolean borderTop, boolean end) {
        Pane pane = new Pane();

        if (!end) {
            pane.getStyleClass().add("pane-recent-cell");
        }

        if (borderTop && borderLeft) {
            pane.getStyleClass().add("pane-recent-cell-border-top-left");
        } else {
            if (borderLeft) {
                pane.getStyleClass().add("pane-recent-cell-border-left");
            }
            if (borderTop) {
                pane.getStyleClass().add("pane-recent-cell-border-top");
            }
        }

        if (content instanceof Label) {
            Label label = ((Label)content);
            label.getStyleClass().add("recent-label-in-cell");
        }

        pane.getChildren().add(content);

        pane.setOnMouseClicked(e -> {
            ControllerScripts.openDetails(new Script(scriptRecent.getFile()), scriptRecent);
        });

        if (end) {
            table.add(pane, "wrap");
        } else {
            table.add(pane, MigLayoutConstants.RECENT_CELL);
        }
    }

    public Node getContent() {
        Pane panePushMain = new Pane();

        int y=0;

        for(ScriptValue value : scriptRecent.getValues()) {
            Label labelName = new Label();
            Label labelValue = new Label();

            String valueReduced = StringUtils.getReducedText(value.getStringValue(), 120, 3);

            labelName.setText(value.getIndex()+"");
            labelValue.setText(valueReduced);

            labelName.setPadding(new Insets(0, 0, 0, 10));
            labelValue.setPadding(new Insets(0, 0, 0, 10));

            gridParameters.add(labelName, 0, y);
            gridParameters.add(labelValue, 1, y);

            y++;
        }

        pane.getStyleClass().add("pane-recent");

        pane.setLeft(paneInfo);
        pane.setCenter(gridParameters);
        pane.setRight(buttonRemove);

        paneInfo.getChildren().add(labelDay);
        paneInfo.getChildren().add(labelTime);
        //paneInfo.getChildren().add(labelPath);

        FxLayoutUtils.setFillWidth(panePushMain);

        pane.setOnMouseClicked(e -> {
            ControllerScripts.openDetails(new Script(scriptRecent.getFile()), scriptRecent);
        });

        return pane;
    }

    public ScriptRecent getScriptRecent() {
        return scriptRecent;
    }
}
