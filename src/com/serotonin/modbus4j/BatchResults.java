package com.serotonin.modbus4j;

import java.util.HashMap;
import java.util.Map;

public class BatchResults<K> {
    private Map<K, Object> data = new HashMap<K, Object>();
    
    public void addResult(K key, Object value) {
        data.put(key, value);
    }
    
    public Object getValue(K key) {
        return data.get(key);
    }
    
    public String toString() {
        return data.toString();
    }
}
