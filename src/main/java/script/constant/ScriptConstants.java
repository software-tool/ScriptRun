package script.constant;

public class ScriptConstants {

    public static String INPUT_GET = " public static String get(int index) { return in.get(index); }; ";

    public static String STATIC_INPUT = "class INPUT { public static var in = null; "+ INPUT_GET +" }; ";

    public static String OUTPUT_SET_HEAD = " public static String setHead(String head) { return out.setHead(head); }; ";
    public static String OUTPUT_ADD_HTML = " public static String addHtml(String html) { return out.addHtml(html); }; ";

    public static String STATIC_OUTPUT = " class OUTPUT { public static var out = null; " + OUTPUT_SET_HEAD + OUTPUT_ADD_HTML + " }; ";

}
