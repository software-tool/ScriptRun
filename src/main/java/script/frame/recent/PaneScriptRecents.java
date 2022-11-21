package script.frame.recent;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxLayoutUtils;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.tbee.javafx.scene.layout.MigPane;
import script.constant.MigLayoutConstants;
import script.frame.intf.IContent;
import script.lang.K;
import script.recent.ScriptRecent;
import script.recent.ScriptRecentManager;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PaneScriptRecents implements IContent, IScriptRecentContainer {

    private File file;
    private VBox pane = new VBox();

    private MigPane grid = new MigPane(MigLayoutConstants.LAYOUT_NO_GAP);

    public PaneScriptRecents(File file) {
        this.file = file;

        fill();
    }

    public Label getTitle() {
        Label labelTitle = new Label(file.getName());
        FxTooltip.setTooltip(labelTitle, file.getAbsolutePath());

        return labelTitle;
    }

    public void addHeader(PaneScriptRecent paneScriptRecent) {
        paneScriptRecent.addHeader(grid);
    }

    public void fillInTable(PaneScriptRecent paneScriptRecent) {
        paneScriptRecent.fillInTable(grid);
    }

    @Override
    public void fill() {
        pane.getChildren().clear();

        List<ScriptRecent> entries = ScriptRecentManager.getRecents(file);
        for (ScriptRecent entry : entries) {
            PaneScriptRecent current = new PaneScriptRecent(this, entry, false);
            pane.getChildren().add(current.getContent());
        }
    }

    public MigPane getGridTable() {
        return grid;
    }

    @Override
    public Node getContent() {
        return pane;
    }

    public static void applyGrouped(IScriptRecentContainer container, VBox parent, List<ScriptRecent> recents) {
        parent.getChildren().clear();

        Map<File, List<ScriptRecent>> groupedByFile = ScriptRecentManager.getGroupedByFile(recents);

        for(Map.Entry<File, List<ScriptRecent>> entry : groupedByFile.entrySet()) {
            File file = entry.getKey();
            List<ScriptRecent> values = entry.getValue();

            PaneScriptRecents scriptRecents = new PaneScriptRecents(file);

            MigPane titlePane = new MigPane();

            // Title
            Label labelTitle = scriptRecents.getTitle();
            titlePane.getChildren().add(labelTitle);

            // Clean
            ImageView imageView = FxIconUtils.getImageView("bin_closed.png");
            imageView.getStyleClass().add("pane-recent-remove-for-file");
            imageView.setFitWidth(18);
            imageView.setFitHeight(18);
            titlePane.getChildren().add(imageView);

            FxTooltip.setTooltip(imageView, Ln.get(K.DeleteRecentOfFile));

            imageView.setOnMouseClicked(e -> {
                ScriptRecentManager.removeAllOfFile(file);

                container.fill();
            });

            boolean first = true;
            for (ScriptRecent scriptRecent : values) {
                PaneScriptRecent pane = new PaneScriptRecent(container, scriptRecent, false);

                if (first) {
                    scriptRecents.addHeader(pane);
                }

                scriptRecents.fillInTable(pane);

                first = false;
            }

            parent.getChildren().add(titlePane);

            // Table

            Node table = scriptRecents.getGridTable();
            FxLayoutUtils.setFillWidth(table);

            parent.getChildren().add(table);
        }
    }
}
