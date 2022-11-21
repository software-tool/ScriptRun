package script;

import script.execute.output.HtmlOutput;

public class Output {

    private HtmlOutput htmlDefault = new HtmlOutput();

    public void addHtml(String html) {
        htmlDefault.append(html);
    }

    public void setHead(String head) {
        htmlDefault.setHead(head);
    }

    public boolean hasHtml() {
        return htmlDefault.hasContent();
    }

    public String getHtml() {
        return htmlDefault.getHtml();
    }

}
