package ru.spacearena.engine.graphics.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class ArgumentParser {

    public static Map<String,List<String>> createArgMap(String[] args) {
        final Map<String,List<String>> parsedArgs = new HashMap<String, List<String>>();
        String currentName = null;
        for (String arg: args) {
            final boolean isOpt = arg.startsWith("-");
            if (isOpt) {
                currentName = arg.substring(1);
            }
            List<String> values = parsedArgs.get(currentName);
            if (values == null) {
                values = new ArrayList<String>();
                parsedArgs.put(currentName, values);
            }
            if (!isOpt) {
                values.add(arg);
            }
        }
        return parsedArgs;
    }

    public static String join(List<String> l, String sep) {
        final StringBuilder sb = new StringBuilder();
        for (String s:l) {
            if (sb.length() > 0) {
                sb.append(sep);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static int parseInt(List<String> l, int def) {
        if (l == null || l.isEmpty()) {
            return def;
        }
        return Integer.parseInt(join(l, ""));
    }

    public static int parseFontStyle(List<String> l) {
        if (l == null || l.isEmpty()) {
            return java.awt.Font.PLAIN;
        }
        final String n = l.get(0);
        if ("p".equals(n) || "plain".equals(n)) {
            return java.awt.Font.PLAIN;
        }
        if ("b".equals(n) || "bold".equals(n)) {
            return Font.BOLD;
        }
        if ("i".equals(n) || "italic".equals(n)) {
            return Font.ITALIC;
        }
        throw new IllegalArgumentException("Unknown font style: " + n);
    }

    public static Arguments parseArgs(String[] args) {
        final Map<String,List<String>> map = createArgMap(args);
        final Arguments a = new Arguments();
        a.fontName = join(map.get("font"), " ");
        a.fontSize = parseInt(map.get("size"), 24);
        a.fontStyle = parseFontStyle(map.get("style"));
        a.pad = parseInt(map.get("pad"), 2);
        a.width = parseInt(map.get("width"), 512);
        a.hq = map.containsKey("hq");
        a.mipmap = map.containsKey("mipmap");
        return a;
    }

}
