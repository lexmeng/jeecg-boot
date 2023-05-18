package org.jeecg.modules.publishlist.logic;

import org.jeecg.modules.publishlist.entity.DependentComponent;
import org.jeecg.modules.publishlist.exception.BussinessException;

public class DependentComponentPlaceholder implements PlaceholderAble<DependentComponent>{

    public String getPlaceholderContent(String placeholder, DependentComponent dependentComponent){
        String result;
        if(placeholder.equals("${componentCnName}")){
            result = dependentComponent.getComponentCnName();
        }else if(placeholder.equals("${componentEnName}")){
            result = dependentComponent.getComponentEnName();
        }else{
            throw new BussinessException("依赖组件的占位符错误！");
        }

        if(result == null) result = "";
        return result;
    }
}
