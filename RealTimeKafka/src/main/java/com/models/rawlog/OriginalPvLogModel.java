package com.models.rawlog;

import com.enums.TranslatorLevel;
import com.models.BaseModel;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:43
 **/

public class OriginalPvLogModel extends BaseModel<String> {

    private static OriginalPvLogModel originalPvLogModel;
    private OriginalPvLogModel(){}

    public static OriginalPvLogModel getOriginalPvLogModel(){
        if (originalPvLogModel==null){
            synchronized (OriginalPvLogModel.class){
                if(originalPvLogModel == null)
                    originalPvLogModel = new OriginalPvLogModel();
            }
        }
        return originalPvLogModel;
    }
    @Override
    public TranslatorLevel getModelLevel() {
        return TranslatorLevel.OriginalPvLogBean;
    }
}
