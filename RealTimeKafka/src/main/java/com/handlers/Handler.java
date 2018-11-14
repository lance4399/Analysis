package com.handlers;

import com.enums.TranslatorLevel;
import com.models.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:47
 **/

public abstract class Handler <I extends BaseModel,O extends BaseModel> {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private Handler next;
    public void setNext(Handler handler){
        this.next = handler;
    }

    /**
     * 责任链处理
     * @param input
     * @return
     */
    public final BaseModel handler(I input, TranslatorLevel level) throws NoSuchMethodException {
        BaseModel result = null;
        if(input==null||input.getData()==null||input.getData().equals("")) {
            //throw new TranslatorInteruptException("转换失败");
            System.out.println("转换失败，to '"+input.getClass().getName() +"'  fail !!!!!!!!!");
            return null;
        }

        /**
         * 判断是否得到target
         */
        if(input.getModelLevel() == level){
            result = input;
            return result;
        }

        if(this.getHandlerLevel() == input.getModelLevel()){
            result = this.parsing(input);
            if(this.next != null){
                result = this.next.handler(result,level);
            }
            return result;
        }else{
            if(this.next != null){
                result = this.next.handler(input,level);
            }
            return result;
        }
    }

    /**
     * 解析日志操作并输出
     * @return
     */
    public abstract O parsing (I input);

    /**
     * 处理节点level
     * @return
     */
    abstract public TranslatorLevel getHandlerLevel();

    protected Integer objectToInt(Object s){
        if(s == null)
            return null;
        String value = s.toString();
        if(value.equalsIgnoreCase("null") || value.equalsIgnoreCase("-") || value.equalsIgnoreCase("")){
            return null;
        }else {
            try{
                return Integer.parseInt(value);
            }catch (NumberFormatException e){
                logger.error("objectToInt fail "+e.getMessage());
            }
            return null;
        }
    }

    protected Long objectToLong(Object s){
        if(s == null)
            return null;
        String value = s.toString();
        if(value == null || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("-") || value.equalsIgnoreCase("")){
            return null;
        }else {
            try{
                return Long.parseLong(value);
            }catch (NumberFormatException e){
                logger.error("objectToLong fail "+e.getMessage());
            }
            return null;
        }
    }
}

