package org.nbfalcon.intellijMCTerra.util;

public class Util {
    public static<T> T firstNonNull(T... objects) {
        for (T object : objects) {
            if (object != null) return object;
        }
        return null;
    }
}
