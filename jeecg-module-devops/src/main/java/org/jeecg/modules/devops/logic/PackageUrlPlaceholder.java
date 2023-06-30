package org.jeecg.modules.devops.logic;

import org.jeecg.modules.devops.entity.PackageUrl;
import org.jeecg.modules.devops.exception.BussinessException;

public class PackageUrlPlaceholder implements PlaceholderAble<PackageUrl>{

    public String getPlaceholderContent(String placeholder, PackageUrl packageUrl){
        String result;
        if(placeholder.equals("${storageType}")){
            result = packageUrl.getStorageType();
        }else if(placeholder.equals("${packageUrl}")) {
            result = packageUrl.getPackageUrl();
        }else{
            throw new BussinessException("产品包url的占位符错误！");
        }

        if(result == null) result = "";
        return result;
    }

}
