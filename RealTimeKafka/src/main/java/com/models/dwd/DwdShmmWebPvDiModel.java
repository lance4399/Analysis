package com.models.dwd;

import com.entity.dwd.DwdShmmWebPvDi;
import com.enums.TranslatorLevel;
import com.models.BaseModel;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 11:09
 **/

public class DwdShmmWebPvDiModel extends BaseModel<DwdShmmWebPvDi> {

    @Override
    public TranslatorLevel getModelLevel() {
        return TranslatorLevel.DwdShmmWebPvDi;
    }
}

