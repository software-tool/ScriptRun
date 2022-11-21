package script.input;

public class InputSelectionItemConfig {

    private String keyword;
    private String text;

    public InputSelectionItemConfig(String keyword, String text) {
        this.keyword = keyword;
        this.text = text;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getText() {
        return text;
    }
}
