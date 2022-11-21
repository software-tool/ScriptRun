package script.frame.execute;

import com.rwu.application.lang.Ln;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import script.frame.intf.IContent;
import script.lang.K;

public class PaneGlobalOutput implements IContent {

    private BorderPane borderPane = new BorderPane();

    private Label labelInfo1 = new Label();
    private Label labelInfo2 = new Label();
    private TextArea textArea = new TextArea();

    @Override
    public Node getContent() {
        if (borderPane.getCenter() == null) {
            initLang();

            VBox boxInfo = new VBox();
            boxInfo.getChildren().add(labelInfo1);
            boxInfo.getChildren().add(labelInfo2);

            boxInfo.setSpacing(4);
            boxInfo.setPadding(new Insets(4, 4, 4, 4));

            borderPane.setTop(boxInfo);
            borderPane.setCenter(textArea);
        }

        return borderPane;
    }

    private void initLang() {
        labelInfo1.setText(Ln.get(K.GlobalOutputInfo1));
        labelInfo2.setText(Ln.get(K.GlobalOutputInfo2));
    }

    public void addOutput(String text) {
        textArea.appendText(text);
    }
}
