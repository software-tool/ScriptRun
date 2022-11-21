package script.frame.details.input;

import com.rwu.fx.util.FxIconUtils;
import com.rwu.fx.util.FxLayoutUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import script.frame.intf.IContent;
import script.input.InputConfig;
import script.recent.ScriptRecent;
import script.recent.ScriptValue;

import java.util.ArrayList;
import java.util.List;

public class PaneInputs implements IContent {

    private GridPane gridPane = new GridPane();

    private Label labelInputsRecent = new Label();

    private Button buttonInputsPrev = new Button();
    private Button buttonInputsNext = new Button();

    private List<PaneInput> inputsPanes = new ArrayList<>();

    // Recent
    private List<ScriptRecent> recents;
    private int recentIndex = 0;

    public PaneInputs() {
        init();
        initActions();
    }

    private void init() {
        FxIconUtils.setIconNoStyleWithSize(buttonInputsPrev, "bullet_arrow_left.png", FxIconUtils.ICON_SIZE_MEDIUM);
        FxIconUtils.setIconNoStyleWithSize(buttonInputsNext, "bullet_arrow_right.png", FxIconUtils.ICON_SIZE_MEDIUM);
    }

    public void fill(List<InputConfig> configs, List<ScriptRecent> recents, IInputParent removeHandler, boolean editing) {
        if (recents == null) {
            recents = new ArrayList();
        }
        this.recents = recents;
        this.recentIndex = recents.size()-1;

        int y=0;
        for(InputConfig config : configs) {
            ScriptValue scriptValue = new ScriptValue(y, config.getInputText());

            PaneInput input = new PaneInput(removeHandler, config, scriptValue);
            input.setEditing(editing);

            HBox boxField = input.getBoxField();
            FxLayoutUtils.setFillWidth(boxField);

            gridPane.add(input.getBoxStart(), 0, y);
            gridPane.add(boxField, 1, y);
            gridPane.add(input.getBoxInfo(), 2, y);

            inputsPanes.add(input);

            y++;
        }

        if (!configs.isEmpty()) {
            Pane panePush = new Pane();

            FxLayoutUtils.setFillWidth(panePush);

            gridPane.add(panePush, 3, 0);

            HBox boxRecentControls = new HBox();
            boxRecentControls.setSpacing(6);

            boxRecentControls.getChildren().add(buttonInputsPrev);
            boxRecentControls.getChildren().add(labelInputsRecent);
            boxRecentControls.getChildren().add(buttonInputsNext);

            gridPane.add(boxRecentControls, 4, 0);
        }

        updateRecentText();
    }

    private void initActions() {
        buttonInputsPrev.setOnAction(e -> {
            recentIndex--;

            if (recentIndex < 0) {
                recentIndex = 0;
            }

            applyRecent();
        });

        buttonInputsNext.setOnAction(e -> {
            recentIndex++;

            if (recentIndex >= recents.size()) {
                recentIndex = recents.size()-1;
            }

            applyRecent();
        });
    }

    private void updateRecentText() {
        if (recents.isEmpty()) {
            labelInputsRecent.setText("");
        } else {
            labelInputsRecent.setText((recentIndex+1) + "/" + recents.size());
        }

        buttonInputsPrev.setDisable(recentIndex <= 0);
        buttonInputsNext.setDisable(recentIndex >= (recents.size()-1));
    }

    private void applyRecent() {
        ScriptRecent recent = null;
        if (!recents.isEmpty()) {
            recent = recents.get(recentIndex);
        }

        if (recent != null) {
            int i=0;
            for(PaneInput paneInput : inputsPanes) {
                ScriptValue forIndex = recent.getForIndex(i);

                if (forIndex != null) {
                    paneInput.setValue(forIndex.getStringValue());
                }

                i++;
            }
        }

        updateRecentText();
    }

    public void applyRecent(ScriptRecent scriptRecent) {
        List<ScriptValue> values = scriptRecent.getValues();

        for(ScriptValue scriptValue : values) {
            int index = scriptValue.getIndex();

            if (index < inputsPanes.size()) {
                PaneInput paneInput = inputsPanes.get(index);
                paneInput.setValue(scriptValue.getStringValue());
            }
        }

        recentIndex = recents.indexOf(scriptRecent);

        applyRecent();
    }

    public void reset() {
        gridPane.getChildren().clear();

        inputsPanes.clear();
    }

    public void setEditing(boolean editing) {
        for (PaneInput inputsPane : inputsPanes) {
            inputsPane.setEditing(editing);
        }
    }

    public PaneInput getPaneAt(int index) {
        return inputsPanes.get(index);
    }

    public Node getContent() {
        return gridPane;
    }

}
