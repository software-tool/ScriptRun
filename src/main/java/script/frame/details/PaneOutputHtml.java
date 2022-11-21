package script.frame.details;

import com.rwu.application.lang.Ln;
import com.rwu.fx.tooltip.FxTooltip;
import com.rwu.fx.util.FxIconUtils;
import com.rwu.log.Log;
import com.rwu.misc.SystemUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import script.frame.intf.IContent;
import script.lang.K;
import script.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class PaneOutputHtml implements IContent {

    private BorderPane pane = new BorderPane();
    private BorderPane paneTop = new BorderPane();

    private Label labelOutputHtml = new Label();
    private WebView areaOutputHtml = null;

    private String html = null;

    public PaneOutputHtml() {
        init();
        initLang();
    }

    public void init() {
        HBox boxButtons = new HBox();

        Button buttonWriteToFile = new Button();
        buttonWriteToFile.setOnAction(e -> {
            try {
                File tmpFile = File.createTempFile("ScriptRun", ".html");

                FileUtils.writeToFile(tmpFile, html);
                SystemUtils.openFile(tmpFile);
            } catch (IOException ex) {
                Log.error("Error writing HTML to temp file", ex);
            }
        });
        boxButtons.getChildren().add(buttonWriteToFile);

        FxTooltip.setTooltip(buttonWriteToFile, Ln.get(K.SaveToTmpFile));

        FxIconUtils.setIconNoStyleWithSize(buttonWriteToFile, "file_extension_html.png");

        paneTop.setLeft(labelOutputHtml);
        paneTop.setRight(boxButtons);

        pane.setTop(paneTop);

    }

    private void initLang() {
        labelOutputHtml.setText(Ln.get(K.Output));
    }

    public void loadContent(String html) {
        this.html = html;

        if (areaOutputHtml == null) {
            areaOutputHtml = new WebView();

            pane.setCenter(areaOutputHtml);
        }

        areaOutputHtml.getEngine().loadContent(html);
    }

    @Override
    public Node getContent() {
        return pane;
    }
}
