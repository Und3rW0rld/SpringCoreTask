package com.uw.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Map;

@Scope("prototype")
@Component
public class StorageImpl implements Storage{

    private  Map<Long, Object> myStorage;

    private long size;

    public StorageImpl(){
        this.size = 0;
    }

    @Autowired
    public void setMyStorage( Map<Long, Object> myStorage ) {
        this.myStorage = myStorage;

    }

    @Override
    public void save(Long key, Object value) {
        size++;
        myStorage.put(key, value);
    }

    @Override
    public Object get(Long key) {
        return myStorage.get(key);
    }

    @Override
    public void remove(Long key) {
        myStorage.remove(key);
    }

    public long getSize(){
        return size;
    }

    public Map<Long, Object> getMyStorage() {
        return myStorage;
    }
}
