package hndt.radiolibs.util;

import java.io.Serializable;
import java.util.HashMap;

public class ThreadVariable implements Serializable {

    private static final ThreadLocal<HashMap<Long, String>> threadLocal = new ThreadLocal<>();

    public ThreadVariable() {
        threadLocal.set(new HashMap<Long, String>());
    }

    public static void set(Long key, String value) {
        if (threadLocal.get() == null) {
            threadLocal.set(new HashMap<Long, String>());
        }
        threadLocal.get().put(key, value);
    }
}
