package script.execute.output;

public class HtmlOutput {

    private String template = "<html><head>%head%</head><body>%body%</body></html>";

    private StringBuilder sbHead = new StringBuilder();
    private StringBuilder sbBody = new StringBuilder();

    public void append(String html) {
        sbBody.append(html);
    }

    public void setHead(String head) {
        sbHead.setLength(0);
        sbHead.append(head);
    }

    public boolean hasContent() {
        return sbBody.length() > 0 || sbHead.length() > 0;
    }

    public String getHtml() {
        String result = template.replace("%head%", sbHead);
        result = result.replace("%body%", sbBody);

        return result;
    }
}
