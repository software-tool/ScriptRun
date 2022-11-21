[<< Back](../README.md)

# Script Output

During the execution of scripts output can be set to be shown in the ScriptRun window.

## Text Output

**Any text output** will be shown in the text output window, including:

* `System.out.print()` or
* `System.out.println()` or
* `out.append()`

## HTML

Shows a window with a HTML page.

Example:

```
OUTPUT.setHead("<style>* { font-family: Arial; }</style>");
OUTPUT.addHtml("<h1>Our Meeting</h1>");
OUTPUT.addHtml("<p>Content: Find new solutions");
```

This will set the style `* { font-family: Arial; }` and add page content: `<h1>`, `<p>`.

### OUTPUT.setHead(String text)

Sets the text to be added in the `<head>...</head>` tag.

### OUTPUT.addHtml(String text)

**Appending** any HTML to the `<body>...</body>` tag.