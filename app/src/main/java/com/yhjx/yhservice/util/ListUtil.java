package com.yhjx.yhservice.util;

import java.util.Collection;

public class ListUtil {

    public ListUtil() {
    }

    public static <T> boolean isEmpty(Collection<T> var0) {
        return var0 == null || var0.size() == 0;
    }

    public static <T> boolean isNotEmpty(Collection<T> var0) {
        return !isEmpty(var0);
    }

    public static <T> int size(Collection<T> var0) {
        return isEmpty(var0) ? 0 : var0.size();
    }
}
