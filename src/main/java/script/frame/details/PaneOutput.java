package script.frame.details;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import script.frame.intf.IContent;
import script.lang.K;

public class PaneOutput implements IContent {

    private BorderPane pane = new BorderPane();
    private BorderPane paneTop = new BorderPane();

    private Label labelOutput = new Label();
    private TextArea areaOutput = new TextArea();

    public PaneOutput() {
        init();
        initLang();
    }

    private void init() {
        Button buttonClear = new Button();
        buttonClear.setOnAction(e -> {
            clearOutput();
        });

        FxTooltip.setTooltip(buttonClear, Ln.get(K.ClearOutput));

        FxIconUtils.setIconNoStyleWithSize(buttonClear, "broom.png");

        HBox boxButtons = new HBox();
        boxButtons.getChildren().add(buttonClear);

        paneTop.setLeft(labelOutput);
        paneTop.setRight(boxButtons);

        pane.setTop(paneTop);
        pane.setCenter(areaOutput);
    }

    private void initLang() {
        labelOutput.setText(Ln.get(K.Output));
    }

    public void clearOutput() {
        areaOutput.setText(null);
    }

    public void setText(String text) {
        areaOutput.setText(text);
    }

    public void appendText(String text) {
        areaOutput.appendText(text);
    }

    @Override
    public Node getContent() {
        return pane;
    }
}
