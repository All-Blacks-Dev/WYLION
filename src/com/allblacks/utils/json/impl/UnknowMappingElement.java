package com.allblacks.utils.json.impl;

import com.allblacks.utils.json.JSONElement;

import java.io.Serializable;

@JSONElement
public class UnknowMappingElement implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6682075037179103623L;
    protected String id;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
