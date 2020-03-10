package conti.ies.comp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Common {
    private static Common INSTANCE;

    public synchronized static Common getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Common();
        }
        return INSTANCE;
    }

    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.create();

//    static {
//        gson = builder.create();
//    }
    public static Gson myGson() {
        return gson;
    }

    public static <T> Iterable<T> checkIsEmpty(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    public static List safe( List other ) {
        return other == null ? Collections.EMPTY_LIST : other;
    }

    private final static Pattern LTRIM = Pattern.compile("^\\s+");

    public static String ltrim(String s) {
        return LTRIM.matcher(s).replaceAll("");
    }

    public static StringBuilder replaceAll(StringBuilder sb, String regex, String replacement)
    {
        String aux = sb.toString();
        aux = aux.replaceAll(regex, replacement);
        sb.setLength(0);
        sb.append(aux);
        return sb;
    }

}
