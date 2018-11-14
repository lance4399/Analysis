package com.models;

import com.enums.TranslatorLevel;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:36
 **/

public abstract class BaseModel<T> {
    protected T data;
    public abstract TranslatorLevel getModelLevel();
    public T getData(){
        return data;
    }
    public void setData(T t){
        this.data = t;
    }
}
