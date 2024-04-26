package com.uw.util;

public interface Storage {
    void save( Long key, Object value);
    Object get(Long key);
    void remove(Long key);
    long getSize();
}
