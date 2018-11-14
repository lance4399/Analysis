package com.handlers.rawlog2ods;

import com.entity.ods.OdsShmmWebPvDi;
import com.enums.TranslatorLevel;
import com.handlers.Handler;
import com.log_cleaner.EtlODSPvLogCleaner;
import com.models.ods.OdsShmmWebPvDiModel;
import com.models.rawlog.OriginalPvLogModel;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:49
 **/

public class Origin2Ods_PvLogHandler extends Handler<OriginalPvLogModel,OdsShmmWebPvDiModel> {

    private static Origin2Ods_PvLogHandler newOriginalPvLogHandler;

    private Origin2Ods_PvLogHandler(){}
    public static Origin2Ods_PvLogHandler getNewOriginalPvLogHandler(){
        if (newOriginalPvLogHandler==null){
            synchronized (Origin2Ods_PvLogHandler.class){
                if(newOriginalPvLogHandler == null)
                    newOriginalPvLogHandler = new Origin2Ods_PvLogHandler();
            }
        }
        return newOriginalPvLogHandler;
    }

    @Override
    public OdsShmmWebPvDiModel parsing(OriginalPvLogModel input) {
        OdsShmmWebPvDiModel odsShmmWebPvDiModel = new OdsShmmWebPvDiModel();
        odsShmmWebPvDiModel.setData(new EtlODSPvLogCleaner().cleanODSPvlog(input.getData()));
        OdsShmmWebPvDi tmp = new EtlODSPvLogCleaner().cleanODSPvlog(input.getData());
        return odsShmmWebPvDiModel;
    }

    @Override
    public TranslatorLevel getHandlerLevel() {
        return TranslatorLevel.OriginalPvLogBean;
    }


}
