package ru.spacearena.engine.parse;

import java.util.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class Arguments {

    private final Map<String,List<String>> values;

    public static final String NO_OPT = "";

    public Arguments(Map<String,List<String>> values) {
        this.values = values;
    }

    public String getValue(String opt) {
        return join(getValues(opt), " ");
    }

    public List<String> getValues(String opt) {
        final List<String> l = values.get(opt);
        return l == null ? Collections.<String>emptyList() : l;
    }

    public int getInt(String opt, int def) {
        try {
            return Integer.parseInt(getValue(opt));
        } catch (NumberFormatException nfe) {
            return def;
        }
    }

    public float getFloat(String opt, float def) {
        try {
            return Float.parseFloat(getValue(opt));
        } catch (NumberFormatException nfe) {
            return def;
        }
    }

    public boolean has(String opt) {
        return values.containsKey(opt);
    }

    public static String join(List<String> l, String sep) {
        final StringBuilder sb = new StringBuilder();
        for (String s : l) {
            if (sb.length() > 0) {
                sb.append(sep);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static Arguments parse(String[] values) {
        final Map<String,List<String>> parsedArgs = new HashMap<String, List<String>>();
        String currentName = "";
        for (String arg: values) {
            final boolean isOpt = arg.startsWith("-");
            if (isOpt) {
                currentName = arg.substring(1);
            }
            List<String> l = parsedArgs.get(currentName);
            if (l == null) {
                l = new ArrayList<String>();
                parsedArgs.put(currentName, l);
            }
            if (!isOpt) {
                l.add(arg);
            }
        }
        return new Arguments(parsedArgs);
    }
}
