package script.frame.recent;

import com.rwu.application.lang.Ln;
import com.rwu.fx.util.FxBorderUtils;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxLayoutUtils;
import com.rwu.fx.util.FxScrollUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import script.controller.ControllerPages;
import script.frame.common.UiCommon;
import script.frame.intf.IContent;
import script.lang.K;
import script.recent.ScriptRecent;
import script.recent.ScriptRecentManager;

import java.util.List;

public class PaneScriptRecentsAll implements IContent, IScriptRecentContainer {

    private GridPane titlePane = new GridPane();

    private Label labelTitle = new Label();
    private Button buttonClose = new Button();

    private VBox main = new VBox();

    private VBox gridPaneRecents = new VBox();

    public PaneScriptRecentsAll() {
        main.getChildren().add(titlePane);
        main.getChildren().add(gridPaneRecents);

        Pane panePush = new Pane();
        FxLayoutUtils.setFillWidth(panePush);

        titlePane.add(labelTitle, 0, 0);
        titlePane.add(panePush, 1, 0);
        titlePane.add(buttonClose, 2, 0);

        FxIconUtils.setIconNoStyleWithSize(buttonClose, "close.png");

        buttonClose.setOnAction(e -> {
            ControllerPages.closeCurrentPage();
        });

        main.setPadding(UiCommon.getContentDefaultInsets());

        initLang();
        fill();
    }

    private void initLang() {
        labelTitle.getStyleClass().add("titles");

        labelTitle.setText(Ln.get(K.PageRecents));
    }

    @Override
    public void fill() {
        List<ScriptRecent> recentsLimited = ScriptRecentManager.getRecentsLimited(20);
        PaneScriptRecents.applyGrouped(this, gridPaneRecents, recentsLimited);

        /*List<File> entriesOther = RecentFileManager.getRecentEntries();
        for (File entry : entriesOther) {
            PaneRecent current = new PaneRecent(this, entry);
            paneOther.getChildren().add(current.getContent());
        }*/
    }

    @Override
    public Node getContent() {
        ScrollPane scrollPane = new ScrollPane(main);
        FxScrollUtils.setFitTrue(scrollPane);
        FxBorderUtils.setBorderDisabled(scrollPane);

        return scrollPane;
    }
}
